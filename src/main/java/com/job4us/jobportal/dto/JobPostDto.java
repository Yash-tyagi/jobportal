package com.job4us.jobportal.dto;

import com.job4us.jobportal.entity.JobCompany;
import com.job4us.jobportal.entity.JobLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostDto {
    private Long totalCandidates;
    private Integer id;
    private String jobTitle;
    private JobCompany company;
    private JobLocation location;
}
