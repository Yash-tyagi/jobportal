package com.job4us.jobportal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userRoleId")
    private Integer id;
    private String name;
    @OneToMany(targetEntity = User.class, mappedBy = "userRole", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<User> users;
}
