package com.jobrecruitment.service.recruiter;

import com.jobrecruitment.model.Recruiter;
import com.jobrecruitment.repository.recruiter.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecruiterService {

    @Autowired
    private RecruiterRepository recruiterRepository;

    public boolean addRecruiter(Recruiter recruiter) {
        if (recruiterRepository.findByEmail(recruiter.getEmail()).isPresent()) {
            return false;
        }

        recruiterRepository.save(recruiter);
        return true;
    }
}
