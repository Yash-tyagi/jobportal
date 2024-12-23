package com.job4us.jobportal.service;

import com.job4us.jobportal.entity.JobSeekerProfile;
import com.job4us.jobportal.entity.RecruiterProfile;
import com.job4us.jobportal.entity.User;
import com.job4us.jobportal.repository.JobSeekerProfileRepository;
import com.job4us.jobportal.repository.RecruiterProfileRepository;
import com.job4us.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;

    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, RecruiterProfileRepository recruiterProfileRepository, JobSeekerProfileRepository jobSeekerProfileRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.encoder = encoder;
    }

    public User addUser(User user) {
        user.setActive(true);
        user.setRegistrationDate(new Date(System.currentTimeMillis()));
        user.setPassword(encoder.encode(user.getPassword()));
        int roleId  = user.getUserRole().getId();
        User newUser = userRepository.save(user);
        if(roleId == 1) {
            //the user is a recruiter
            recruiterProfileRepository.save(RecruiterProfile.builder().user(newUser).build());
        }else {
            jobSeekerProfileRepository.save(JobSeekerProfile.builder().user(newUser).build());
        }
        return newUser;
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return null;
    }

    public User getCurrentUser() {
        String username = getCurrentUsername();
        User user = null;
        if(username != null) {
            user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Could not found user"));
        }
        return user;
    }

    public Object getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = getCurrentUser();
        if(user != null) {
            int userId = user.getId();
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                return recruiterProfileRepository.findById(userId).orElse(new RecruiterProfile());
            }else {
                return jobSeekerProfileRepository.findById(userId).orElse(new JobSeekerProfile());
            }
        }
        return null;
    }

    public boolean isRecruiterProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = getCurrentUser();
        if(user != null) {
            return authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"));
        }
        return false;
    }

}
