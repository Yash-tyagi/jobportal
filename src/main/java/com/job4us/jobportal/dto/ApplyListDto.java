package com.job4us.jobportal.dto;

import com.job4us.jobportal.entity.JobPost;
import com.job4us.jobportal.entity.User;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyListDto {
    private User user;
    private JobPost jobPost;
    private String firstName;
    private String lastName;
}
