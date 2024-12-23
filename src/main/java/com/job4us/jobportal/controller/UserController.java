package com.job4us.jobportal.controller;

import com.job4us.jobportal.entity.User;
import com.job4us.jobportal.entity.UserRole;
import com.job4us.jobportal.service.UserRoleService;
import com.job4us.jobportal.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {
    private final UserRoleService userRoleService;
    private final UserService userService;

    @Autowired
    public UserController(UserRoleService userRoleService, UserService userService) {
        this.userRoleService = userRoleService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
        return "redirect:/";
    }

    @GetMapping("/register")
    public String register(Model model) {
        List<UserRole> roles = userRoleService.getAllUserRoles();
        model.addAttribute("getAllUserRoles",roles);
        model.addAttribute("user",new User());
        return "register";
    }

    @PostMapping("/register/new")
    public String userRegistration(User user, Model model) {
        if(userService.getUserByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("error","email already registered! try to register with another email.");
            List<UserRole> roles = userRoleService.getAllUserRoles();
            model.addAttribute("getAllUserRoles",roles);
            model.addAttribute("user",new User());
            return "register";
        }
        userService.addUser(user);
        return "redirect:/dashboard";
    }

}
