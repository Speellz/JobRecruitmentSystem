package com.jobrecruitment.service.applicant;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.applicant.Referral;
import com.jobrecruitment.model.recruiter.JobPosting;
import com.jobrecruitment.repository.UserRepository;
import com.jobrecruitment.repository.applicant.ReferralRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReferralService {

    private final ReferralRepository referralRepository;
    private final UserRepository userRepository;

    public boolean saveReferral(String referrerEmail, User referredUser, JobPosting job) {
        Optional<User> referrerOpt = userRepository.findByEmail(referrerEmail.trim());
        if (referrerOpt.isEmpty() || referrerOpt.get().getId().equals(referredUser.getId())) {
            return false;
        }

        User referrer = referrerOpt.get();

        Referral referral = new Referral();
        referral.setReferrerUser(referrer);
        referral.setReferredUser(referredUser);
        referral.setJob(job);

        referralRepository.save(referral);
        return true;
    }
}
