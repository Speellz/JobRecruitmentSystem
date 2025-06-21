package com.jobrecruitment.controller.recruiter;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.applicant.Application;
import com.jobrecruitment.model.recruiter.InterviewSchedule;
import com.jobrecruitment.repository.applicant.ApplicationRepository;
import com.jobrecruitment.repository.recruiter.InterviewScheduleRepository;
import com.jobrecruitment.service.common.UserNotificationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Controller
@RequestMapping("/recruiter/interview")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewScheduleRepository interviewScheduleRepository;
    private final ApplicationRepository applicationRepository;
    private final UserNotificationService userNotificationService;

    @GetMapping("/schedule")
    public String showScheduleForm(@RequestParam Integer applicationId,
                                   @RequestParam(required = false) String selectedDate,
                                   @RequestParam(required = false) Integer rescheduleId,
                                   Model model) {

        Application application = applicationRepository.findById(applicationId).orElse(null);
        if (application == null) return "redirect:/recruiter/interview/list";

        LocalDate date = (selectedDate != null) ? LocalDate.parse(selectedDate) : LocalDate.now();
        List<String> bookedSlots = interviewScheduleRepository.findByDate(date).stream()
                .map(i -> i.getTime().toLocalTime().truncatedTo(ChronoUnit.HOURS).toString())
                .toList();

        model.addAttribute("application", application);
        model.addAttribute("selectedDate", date.toString());
        model.addAttribute("bookedSlots", bookedSlots);

        if (rescheduleId != null) {
            model.addAttribute("rescheduleId", rescheduleId);
        }

        return "recruiter/schedule-interview";
    }

    @PostMapping("/schedule")
    public String scheduleInterview(@RequestParam Integer applicationId,
                                    @RequestParam String time,
                                    @RequestParam(required = false) Integer rescheduleId,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        User recruiter = (User) session.getAttribute("loggedUser");

        Application application = applicationRepository.findById(applicationId).orElse(null);
        if (application == null || recruiter == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid operation.");
            return "redirect:/";
        }

        if (rescheduleId != null) {
            interviewScheduleRepository.deleteById(rescheduleId);
        }

        InterviewSchedule interview = new InterviewSchedule();
        interview.setApplicant(application.getApplicant());
        interview.setRecruiter(recruiter);
        interview.setJob(application.getJob());
        interview.setStatus("SCHEDULED");
        interview.setTime(LocalDateTime.parse(time));
        LocalDateTime interviewTime = LocalDateTime.parse(time);
        if (interviewTime.isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "Cannot schedule an interview in the past.");
            return "redirect:/recruiter/interview/schedule/" + applicationId;
        }
        interview.setTime(interviewTime);

        interviewScheduleRepository.save(interview);

        userNotificationService.sendNotification(
                application.getApplicant(),
                "An interview has been scheduled for your application.",
                "/applicant/applications"
        );

        redirectAttributes.addFlashAttribute("success", "Interview scheduled successfully.");
        return "redirect:/recruiter/interview/list";
    }

    @GetMapping("/list")
    public String listInterviews(HttpSession session, Model model) {
        User recruiter = (User) session.getAttribute("loggedUser");
        if (recruiter == null) return "redirect:/auth/login";

        List<InterviewSchedule> interviews = interviewScheduleRepository.findByRecruiterId(recruiter.getId());

        Map<String, List<InterviewSchedule>> interviewMap = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);

        for (InterviewSchedule interview : interviews) {
            String dateKey = interview.getTime().toLocalDate().format(formatter);
            interviewMap.computeIfAbsent(dateKey, k -> new ArrayList<>()).add(interview);
        }

        model.addAttribute("interviewMap", interviewMap);
        return "recruiter/interview-list";
    }

    @GetMapping("/schedule/{applicationId}")
    public String showScheduleForm(@PathVariable Integer applicationId,
                                   @RequestParam(required = false) String selectedDate,
                                   HttpSession session,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        User recruiter = (User) session.getAttribute("loggedUser");
        if (recruiter == null) {
            return "redirect:/login";
        }

        Application application = applicationRepository.findById(applicationId).orElse(null);
        if (application == null) {
            redirectAttributes.addFlashAttribute("error", "Application not found.");
            return "redirect:/recruiter/interview/list";
        }

        LocalDate today = LocalDate.now();
        LocalDate date = selectedDate != null ? LocalDate.parse(selectedDate) : today;
        if (date.isBefore(today)) {
            date = today;
        }

        List<String> bookedSlots = interviewScheduleRepository.findByDate(date).stream()
                .map(i -> i.getTime().toLocalTime().truncatedTo(ChronoUnit.HOURS).toString())
                .toList();

        int currentHour = LocalDateTime.now().getHour();
        if (LocalDateTime.now().getMinute() > 0) {
            currentHour += 1;
        }

        model.addAttribute("application", application);
        model.addAttribute("bookedSlots", bookedSlots);
        model.addAttribute("selectedDate", date.toString());
        model.addAttribute("nowDate", today.toString());
        model.addAttribute("currentHour", currentHour);

        return "recruiter/schedule-interview";
    }

    @GetMapping("/cancel/{id}")
    public String cancelInterview(@PathVariable Integer id, HttpSession session) {
        User recruiter = (User) session.getAttribute("loggedUser");
        InterviewSchedule interview = interviewScheduleRepository.findById(id).orElse(null);

        if (interview != null && recruiter != null && interview.getRecruiter().getId().equals(recruiter.getId())) {
            interviewScheduleRepository.deleteById(id);

            userNotificationService.sendNotification(
                    interview.getApplicant(),
                    "Your interview has been canceled.",
                    "/applicant/applications"
            );
        }

        return "redirect:/recruiter/interview/list";
    }

    @GetMapping("/reschedule/{id}")
    public String rescheduleInterview(@PathVariable Integer id, Model model) {
        InterviewSchedule interview = interviewScheduleRepository.findById(id).orElse(null);
        if (interview == null) return "redirect:/recruiter/interview/list";

        Application application = applicationRepository
                .findByApplicantIdAndJobId(
                        interview.getApplicant().getId().longValue(),
                        interview.getJob().getId().longValue()
                );
        if (application == null) return "redirect:/recruiter/interview/list";

        LocalDate today = LocalDate.now();
        List<String> bookedSlots = interviewScheduleRepository.findByDate(today).stream()
                .map(i -> i.getTime().toLocalTime().truncatedTo(ChronoUnit.HOURS).toString())
                .toList();

        int currentHour = LocalDateTime.now().getHour();
        if (LocalDateTime.now().getMinute() > 0) {
            currentHour += 1;
        }

        model.addAttribute("application", application);
        model.addAttribute("bookedSlots", bookedSlots);
        model.addAttribute("nowDate", today.toString());
        model.addAttribute("selectedDate", today.toString());
        model.addAttribute("currentHour", currentHour);

        interviewScheduleRepository.deleteById(id);

        userNotificationService.sendNotification(
                interview.getApplicant(),
                "Your interview has been marked for rescheduling.",
                "/applicant/applications"
        );

        return "recruiter/schedule-interview";
    }
}