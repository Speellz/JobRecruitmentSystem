package com.jobrecruitment.repository.recruiter;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.applicant.Application;
import com.jobrecruitment.model.recruiter.InterviewSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Integer> {

    List<InterviewSchedule> findByRecruiterId(Long recruiterId);

    @Query(value = "SELECT * FROM interview_schedules WHERE CAST(scheduled_at AS date) = :date", nativeQuery = true)
    List<InterviewSchedule> findByDate(@Param("date") LocalDate date);

    Optional<InterviewSchedule> findByApplicantIdAndJobId(Long applicantId, Long jobId);

    List<InterviewSchedule> findByRecruiter_IdAndTimeBefore(Long recruiterId, LocalDateTime time);

    List<InterviewSchedule> findByApplicant_IdAndTimeBefore(Long applicantId, LocalDateTime time);

}
