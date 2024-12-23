package com.job4us.jobportal.entity;

public interface IRecruiterJobs {
    Long getTotalCandidates();
    Integer getJobPostId();
    String getJobTitle();
    Integer getLocationId();
    String getCity();
    String getState();
    String getCountry();
    Integer getCompanyId();
    String getCompanyName();
}
