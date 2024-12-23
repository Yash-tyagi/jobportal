package com.job4us.jobportal.controller;

import com.job4us.jobportal.entity.JobSeekerProfile;
import com.job4us.jobportal.entity.Skill;
import com.job4us.jobportal.entity.User;
import com.job4us.jobportal.service.JobSeekerProfileService;
import com.job4us.jobportal.service.UserService;
import com.job4us.jobportal.utils.FileDownloadUtil;
import com.job4us.jobportal.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/job-seeker-profile")
public class JobSeekerProfileController {

    @Value("${spring.user.lob-upload-dir}")
    private String uploadDir;
    private final UserService userService;
    private final JobSeekerProfileService jobSeekerProfileService;

    public JobSeekerProfileController(UserService userService, JobSeekerProfileService jobSeekerProfileService) {
        this.userService = userService;
        this.jobSeekerProfileService = jobSeekerProfileService;
    }

    @GetMapping
    public String jobSeekerProfile(Model model) {
        JobSeekerProfile jobSeekerProfile;
        User user = userService.getCurrentUser();
        jobSeekerProfile = jobSeekerProfileService.getJobSeekerProfile(user.getId());
        List<Skill> skills = jobSeekerProfile.getSkills();
        if(jobSeekerProfile.getSkills().isEmpty()) {
            skills = List.of(new Skill());
            jobSeekerProfile.setSkills(skills);
        }
        model.addAttribute("skills",skills);
        model.addAttribute("profile",jobSeekerProfile);
        return "job-seeker-profile";
    }

    @PostMapping("/addNew")
    public String addNewProfile(JobSeekerProfile jobSeekerProfile,
                                @RequestParam("image")MultipartFile profileImg,
                                @RequestParam("pdf") MultipartFile resume) {
        User user = userService.getCurrentUser();
        jobSeekerProfile.setId(user.getId());
        jobSeekerProfile.setUser(user);

        for(Skill skill : jobSeekerProfile.getSkills()) {
            skill.setJobSeekerProfile(jobSeekerProfile);
        }

        JobSeekerProfile existingProfile = jobSeekerProfileService.getJobSeekerProfile(user.getId());

        if(existingProfile.getProfilePhoto() != null && !existingProfile.getProfilePhoto().trim().isEmpty()){
            jobSeekerProfile.setProfilePhoto(existingProfile.getProfilePhoto());
        }
        if(existingProfile.getResume() != null && !existingProfile.getResume().trim().isEmpty()){
            jobSeekerProfile.setResume(existingProfile.getResume());
        }

        String profileImgName = profileImg.getOriginalFilename();
        String resumeName = resume.getOriginalFilename();

        if(profileImgName != null && !profileImgName.trim().isEmpty())jobSeekerProfile.setProfilePhoto(profileImgName);
        if(resumeName != null  && !resumeName.trim().isEmpty())jobSeekerProfile.setResume(resumeName);


        jobSeekerProfile = jobSeekerProfileService.addProfile(jobSeekerProfile);

        try{
            uploadDir+="/candidate/"+jobSeekerProfile.getId();
            if(!profileImgName.isEmpty()) {
                FileUploadUtil.saveFile(uploadDir,profileImgName,profileImg);
            }
            if(!resumeName.isEmpty()) {
                FileUploadUtil.saveFile(uploadDir,resumeName,resume);
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/dashboard";
    }

    @GetMapping("/{id}")
    public String candidateProfile(@PathVariable Integer id, Model model) {
        JobSeekerProfile jobSeekerProfile = jobSeekerProfileService.getJobSeekerProfile(id);
        model.addAttribute("profile",jobSeekerProfile);
        return "job-seeker-profile";
    }

    @GetMapping("/downloadResume")
    public ResponseEntity<?> downloadResume(@RequestParam("fileName") String fileName, @RequestParam("userID") Integer userId) {
        FileDownloadUtil downloadUtil = new FileDownloadUtil();
        Resource resource = null;
        System.out.println(uploadDir);
        try{
            resource = downloadUtil.getFileAsResource(uploadDir+"/candidate/"+userId,fileName);
        }catch (IOException io) {
            return ResponseEntity.badRequest().build();
        }

        if(resource == null) return new ResponseEntity<>("File Not Found", HttpStatus.NOT_FOUND);

        String contentType="application/octet-stream";
        String headerValue = "inline; fileName=\""+fileName+"\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,headerValue)
                .body(resource);
    }
}
