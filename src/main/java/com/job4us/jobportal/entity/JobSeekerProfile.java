package com.job4us.jobportal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobSeekerProfile {
    @Id
    @Column(name = "user_id")
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;

    private String firstName;
    private String lastName;
    private String city;
    private String state;
    private String country;
    private String workAuthorization;
    private String employmentType;
    private String resume;
    @Column(nullable = true, length = 64)
    private String profilePhoto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jobSeekerProfile")
    private List<Skill> skills;
    @Transient
    public String photoImagePath() {
        if(profilePhoto == null || id == null) return null;
        return "/user-file-uploads/candidate/"+id+"/"+profilePhoto;
    }
}
