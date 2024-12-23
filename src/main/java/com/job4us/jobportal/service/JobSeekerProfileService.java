package com.job4us.jobportal.service;

import com.job4us.jobportal.entity.JobSeekerProfile;
import com.job4us.jobportal.entity.User;
import com.job4us.jobportal.repository.JobSeekerProfileRepository;
import com.job4us.jobportal.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class JobSeekerProfileService {
    private final JobSeekerProfileRepository jobSeekerProfileRepository;

    public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository) {
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
    }

    public JobSeekerProfile getJobSeekerProfile(Integer userId) {
        return jobSeekerProfileRepository.findById(userId).orElse(new JobSeekerProfile());
    }

    public JobSeekerProfile addProfile(JobSeekerProfile jobSeekerProfile) {
        return jobSeekerProfileRepository.save(jobSeekerProfile);
    }
}
