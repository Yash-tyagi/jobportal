package com.job4us.jobportal.controller;

import com.job4us.jobportal.dto.ApplyListDto;
import com.job4us.jobportal.dto.JobPostDto;
import com.job4us.jobportal.entity.*;
import com.job4us.jobportal.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class JobSeekerApplyController {

    private final UserService userService;
    private final JobPostService jobPostService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;

    private final JobSeekerProfileService jobSeekerProfileService;

    public JobSeekerApplyController(UserService userService, JobPostService jobPostService, JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobSeekerSaveService, JobSeekerProfileService jobSeekerProfileService) {
        this.userService = userService;
        this.jobPostService = jobPostService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
        this.jobSeekerProfileService = jobSeekerProfileService;
    }

    @GetMapping("/job-details-apply/{id}")
    public String display(@PathVariable int id, Model model) {
        String currentUsername = userService.getCurrentUsername();
        JobPost jobDetails = jobPostService.getAPost(id);
        Object currentUserProfile = null;
        List<JobSeekerApply> jobSeekerApplyList = jobSeekerApplyService.getAppliedJobsByJob(jobDetails);
        List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService.getAppliedJobsByJob(jobDetails);

        if(userService.isRecruiterProfile()) {
            currentUserProfile = (RecruiterProfile) userService.getCurrentUserProfile();
            List<ApplyListDto> applyListDtos = new ArrayList<>();
            for(JobSeekerApply list : jobSeekerApplyList) {
                JobSeekerProfile profile = jobSeekerProfileService.getJobSeekerProfile(list.getUser().getId());
                System.out.println(profile.getFirstName() + " YT " + profile.getLastName());
                applyListDtos.add(new ApplyListDto(list.getUser(),list.getJobPost(),profile.getFirstName(), profile.getLastName()));
            }
            model.addAttribute("applyList", applyListDtos);
        }else {
            currentUserProfile = (JobSeekerProfile) userService.getCurrentUserProfile();
            boolean alreadyApplied = false;
            boolean alreadySaved = false;

            User currentUser = userService.getCurrentUser();

            for (JobSeekerApply applyList : jobSeekerApplyList) {
                if (Objects.equals(applyList.getUser().getId(), currentUser.getId())) {
                    alreadyApplied = true;
                    break;
                }
            }

            for (JobSeekerSave saveList : jobSeekerSaveList) {
                if (Objects.equals(saveList.getUser().getId(), currentUser.getId())) {
                    alreadySaved = true;
                    break;
                }
            }

            model.addAttribute("alreadyApplied", alreadyApplied);
            model.addAttribute("alreadySaved", alreadySaved);
        }

        JobSeekerApply jobSeekerApply = new JobSeekerApply();
        model.addAttribute("applyJob", jobSeekerApply);


        model.addAttribute("user", currentUserProfile);
        model.addAttribute("username", currentUsername);
        model.addAttribute("jobDetails", jobDetails);
        return "job-details";
    }

    @PostMapping("/job-details/apply/{jobId}")
    public String applyForJob(@PathVariable Integer jobId) {
        User user = userService.getCurrentUser();
        JobPost jobPost = jobPostService.getAPost(jobId);
        jobSeekerApplyService.applyForAJobPost(user,jobPost);
        return "redirect:/dashboard";
    }


}
