package com.job4us.jobportal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String jobType;
    private String jobTitle;
    @Column(length = 10000)
    private String jobDescription;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date postedDate;

    private String remote;
    private float salary;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    private JobCompany company;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private JobLocation location;

    @ManyToOne
    @JoinColumn(name = "posted_by_id",referencedColumnName = "id")
    private User user;

    @Transient
    private boolean isActive;

    @Transient
    private boolean isSaved;

    @Transient
    private boolean totalCandidates;

}
