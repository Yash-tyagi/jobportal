package com.job4us.jobportal.service;

import com.job4us.jobportal.entity.UserRole;
import com.job4us.jobportal.repository.UserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public List<UserRole> getAllUserRoles() {
        return userRoleRepository.findAll();
    }

    public UserRole addUserRole(UserRole role) {
        UserRole newUserRole = userRoleRepository.save(role);
        return newUserRole;
    }

}
