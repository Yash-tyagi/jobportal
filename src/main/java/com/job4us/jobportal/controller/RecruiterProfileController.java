package com.job4us.jobportal.controller;

import com.job4us.jobportal.entity.RecruiterProfile;
import com.job4us.jobportal.entity.User;
import com.job4us.jobportal.repository.RecruiterProfileRepository;
import com.job4us.jobportal.service.RecruiterProfileService;
import com.job4us.jobportal.service.UserService;
import com.job4us.jobportal.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    private final RecruiterProfileService recruiterProfileService;
    private final UserService userService;

    @Value("${spring.user.lob-upload-dir}")
    private String uploadDir;

    @Autowired
    public RecruiterProfileController(RecruiterProfileService recruiterProfileService, UserService userService) {
        this.recruiterProfileService = recruiterProfileService;
        this.userService = userService;
    }


    @GetMapping
    public String recruiterProfile(Model model) {
        RecruiterProfile recruiterProfile = recruiterProfileService.getRecruiterProfile();
        if(recruiterProfile != null) {
            model.addAttribute("profile",recruiterProfile);
        }
        return "recruiter_profile";
    }

    @PostMapping("/addNew")
    public String addNew(RecruiterProfile recruiterProfile,
                         @RequestParam("image")MultipartFile multipartFile, Model model) {
        User user = userService.getCurrentUser();
        if(user != null) {
            recruiterProfile.setUser(user);
            recruiterProfile.setId(user.getId());
        }
        model.addAttribute("profile",recruiterProfile);

        /* Getting the file name from MultiPartFile */
        String fileName = "";
        if(!multipartFile.getOriginalFilename().isEmpty()) {
            fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        }
        System.out.println("file name is " + fileName);

        /* Setting the profile photo name to the recruiter profile */
        recruiterProfile.setProfilePhoto(fileName);

        /* Persisting the recruiter profile in database */
        RecruiterProfile savedUserProfile = recruiterProfileService.addNewProfile(recruiterProfile);

        /* Logic to upload the file on server */
        uploadDir += "/recruiter/"+savedUserProfile.getId();
        System.out.println("upload directory " + uploadDir);
        try {
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        } catch (IOException e) {
            System.out.println("Failed to save the recruiter profile photo :(");
              e.printStackTrace();
        }

        /* After successful upload redirect to dashboard */
        return "redirect:/dashboard";
    }
}
