package com.job4us.jobportal.controller;

import com.job4us.jobportal.dto.JobPostDto;
import com.job4us.jobportal.entity.*;
import com.job4us.jobportal.service.JobPostService;
import com.job4us.jobportal.service.JobSeekerApplyService;
import com.job4us.jobportal.service.JobSeekerSaveService;
import com.job4us.jobportal.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class JobPostActivityController {

    private final UserService userService;
    private final JobPostService jobPostService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;

    public JobPostActivityController(UserService userService, JobPostService jobPostService, JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobSeekerSaveService) {
        this.userService = userService;
        this.jobPostService = jobPostService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

    @GetMapping("/dashboard")
    public String searchJobs(Model model, @RequestParam(name = "job", required = false) String job,
                             @RequestParam(name = "location", required = false) String location,
                             @RequestParam(name = "partTime", required = false) String partTime,
                             @RequestParam(name = "fullTime", required = false) String fullTime,
                             @RequestParam(name = "freelance", required = false) String freelance,
                             @RequestParam(name = "internship", required = false) String internship,
                             @RequestParam(name = "remoteOnly", required = false) String remoteOnly,
                             @RequestParam(name = "officeOnly", required = false) String officeOnly,
                             @RequestParam(name = "partialRemote", required = false) String partialRemote,
                             @RequestParam(name = "today", required = false) boolean today,
                             @RequestParam(name = "days7", required = false) boolean days7,
                             @RequestParam(name = "days30", required = false) boolean days30){

        /* Adding the above checkboxes in model */
        model.addAttribute("job", job);
        model.addAttribute("location",location);
        model.addAttribute("partTime",Objects.equals(partTime,"Part-Time"));
        model.addAttribute("fullTime",Objects.equals(fullTime,"Full-Time"));
        model.addAttribute("freelance",Objects.equals(freelance,"Freelance"));
        model.addAttribute("internship",Objects.equals(internship,"Internship"));
        model.addAttribute("remoteOnly",Objects.equals(remoteOnly,"Remote-Only"));
        model.addAttribute("officeOnly",Objects.equals(officeOnly,"Office-Only"));
        model.addAttribute("partialRemote",Objects.equals(partialRemote,"Partial-Remote"));
        model.addAttribute("today",today);
        model.addAttribute("days7",days7);
        model.addAttribute("days30",days30);

        LocalDate date = null;
        boolean type = true;
        boolean remote = true;
        boolean dateSearchFlag = true;
        List<JobPost> jobPosts = null;

        if(days30) {
            date = LocalDate.now().minusDays(30);
        } else if(days7) {
            date = LocalDate.now().minusDays(7);
        } else if(today){
            date = LocalDate.now();
        } else{
            dateSearchFlag = false;
        }

        if(partTime == null && fullTime == null && freelance == null && internship == null) {
            partTime = "Part-Time";
            fullTime = "Full-Time";
            freelance = "Freelance";
            internship = "Internship";
            type = false;
        }

        if(remoteOnly == null && officeOnly == null && partialRemote == null) {
            remoteOnly = "Remote-Only";
            officeOnly = "Office-Only";
            partialRemote = "Partial-Remote";
            remote = false;
        }

        if(!StringUtils.hasText(job) && !StringUtils.hasText(location) &&
                !dateSearchFlag && !type && !remote) {
        jobPosts = jobPostService.getAll();
        }else {
        jobPosts = jobPostService.search(job,location, Arrays.asList(partTime,fullTime,freelance,internship),
                Arrays.asList(remoteOnly,officeOnly,partialRemote),date);
        }

        User currentUser = userService.getCurrentUser();
        Object currentUserProfile = userService.getCurrentUserProfile();
        String currentUsername = userService.getCurrentUsername();
        //TODO add the condition to check the if profile is of recruiter or job seeker
        if(userService.isRecruiterProfile()) {
            System.out.println("((RecruiterProfile)currentUserProfile).getId() " + ((RecruiterProfile) currentUserProfile).getId());
            List<JobPostDto> recJobPosts = jobPostService.getRecruiterPosts(((RecruiterProfile) currentUserProfile).getId());
            model.addAttribute("jobPosts", recJobPosts);
        } else{
            List<JobSeekerApply> jobSeekerApplyList =jobSeekerApplyService.getAppliedJobsBySeeker(currentUser);
            List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService.getAppliedJobsBySeeker(currentUser);
            for(JobPost jobPost : jobPosts) {
                jobPost.setActive(false);
                jobPost.setSaved(false);
                for(JobSeekerApply jobSeekerApply : jobSeekerApplyList) {
                    if(Objects.equals(jobPost.getId(),jobSeekerApply.getJobPost().getId())){
                        jobPost.setActive(true);
                        break;
                    }
                }

                for(JobSeekerSave jobSeekerSave : jobSeekerSaveList) {
                    if(Objects.equals(jobPost.getId(),jobSeekerSave.getJobPost().getId())) {
                        jobPost.setSaved(true);
                        break;
                    }
                }
            }
            model.addAttribute("jobPosts",jobPosts);
        }

        model.addAttribute("user", currentUserProfile);
        model.addAttribute("username", currentUsername);
        return "dashboard";
    }

    @GetMapping("/dashboard/add")
    public String addJobPage(Model model) {
        model.addAttribute("jobPost",new JobPost());
        model.addAttribute("user",userService.getCurrentUserProfile());
        return "add-jobs";
    }

    @PostMapping("/dashboard/addNew")
    public String addNewJob(JobPost jobPost, Model model) {
        System.out.println(jobPost);
        User user = userService.getCurrentUser();
        if(user != null) jobPost.setUser(user);
        jobPost.setPostedDate(new Date());
        jobPostService.addJobPost(jobPost);
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/edit/{id}")
    public String editJob(@PathVariable int id, Model model) {
        JobPost jobPost = jobPostService.getAPost(id);
        model.addAttribute("jobPost",jobPost);
        model.addAttribute("user",userService.getCurrentUserProfile());
        model.addAttribute("edit",true);
        return "add-jobs";
    }

    @PostMapping("/dashboard/deleteJob/{id}")
    public String deleteJob(@PathVariable int id) {
        jobPostService.deletePost(id);

        return "redirect:/dashboard";
    }

}
