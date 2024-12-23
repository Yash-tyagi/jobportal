package com.job4us.jobportal.controller;

import com.job4us.jobportal.entity.*;
import com.job4us.jobportal.service.JobPostService;
import com.job4us.jobportal.service.JobSeekerApplyService;
import com.job4us.jobportal.service.JobSeekerSaveService;
import com.job4us.jobportal.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class JobSeekerSaveController {

    private final UserService userService;
    private final JobPostService jobPostService;
    private final JobSeekerSaveService jobSeekerSaveService;

    private final JobSeekerApplyService jobSeekerApplyService;

    public JobSeekerSaveController(UserService userService, JobPostService jobPostService, JobSeekerSaveService jobSeekerSaveService, JobSeekerApplyService jobSeekerApplyService) {
        this.userService = userService;
        this.jobPostService = jobPostService;
        this.jobSeekerSaveService = jobSeekerSaveService;
        this.jobSeekerApplyService = jobSeekerApplyService;
    }

    @GetMapping("/saved-jobs")
    public String savedJobs(Model model) {
        JobSeekerProfile currentUserProfile = null;
        if(!userService.isRecruiterProfile()) {
            currentUserProfile = (JobSeekerProfile)userService.getCurrentUserProfile();;
        }
        User currentUser = userService.getCurrentUser();
        List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService.getAppliedJobsBySeeker(currentUser);
        List<JobPost> jobPosts = jobSeekerSaveList.stream().map(JobSeekerSave::getJobPost).collect(Collectors.toList());

        List<JobSeekerApply> jobSeekerApplyList = jobSeekerApplyService.getAppliedJobsBySeeker(currentUser);
        for(JobPost jobPost : jobPosts) {
            boolean isActive = false;
            for(JobSeekerApply jobSeekerApply : jobSeekerApplyList) {
                if(jobPost.getId() == jobSeekerApply.getJobPost().getId()) {
                    isActive = true; break;
                }
            }
            if(isActive) {
                jobPost.setActive(true);
            }
        }

        model.addAttribute("user",currentUserProfile);
        model.addAttribute("jobPosts",jobPosts);
        return "saved-jobs";
    }

    @PostMapping("/job-details/save/{jobId}")
    public String saveAJob(@PathVariable Integer jobId) {
        User user = userService.getCurrentUser();
        JobPost jobPost = jobPostService.getAPost(jobId);
        jobSeekerSaveService.saveAJobPost(user,jobPost);

        return "redirect:/dashboard";
    }

}
