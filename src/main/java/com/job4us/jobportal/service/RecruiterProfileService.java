package com.job4us.jobportal.service;

import com.job4us.jobportal.entity.RecruiterProfile;
import com.job4us.jobportal.repository.RecruiterProfileRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterProfileService {

    private final UserService userService;
    private final RecruiterProfileRepository recruiterProfileRepository;
    public RecruiterProfileService(UserService userService, RecruiterProfileRepository recruiterProfileRepository) {
        this.userService = userService;
        this.recruiterProfileRepository = recruiterProfileRepository;
    }

    public RecruiterProfile getRecruiterProfile() {

        Object obj= userService.getCurrentUserProfile();
        if(obj instanceof RecruiterProfile) {
            return (RecruiterProfile)obj;
        }
        return null;
    }

    public RecruiterProfile addNewProfile(RecruiterProfile recruiterProfile) {
        return recruiterProfileRepository.save(recruiterProfile);
    }
}
