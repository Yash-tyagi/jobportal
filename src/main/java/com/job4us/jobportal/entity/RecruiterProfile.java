package com.job4us.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterProfile {
    @Id
    @Column(name = "user_id")
    private Integer id;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;

    private String firstName;
    private String lastName;
    private String city;
    private String state;
    private String country;
    private String company;
    @Column(nullable = true, length = 64)
    private String profilePhoto;
    @Transient
    public String photoImagePath() {
        if(profilePhoto == null || id == null) return null;
        return "/user-file-uploads/recruiter/"+id+"/"+profilePhoto;
    }
}
