package com.job4us.jobportal.service;

import com.job4us.jobportal.entity.JobPost;
import com.job4us.jobportal.entity.JobSeekerApply;
import com.job4us.jobportal.entity.User;
import com.job4us.jobportal.repository.JobSeekerApplyRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JobSeekerApplyService {
    private final JobSeekerApplyRepository jobSeekerApplyRepository;

    public JobSeekerApplyService(JobSeekerApplyRepository jobSeekerApplyRepository) {
        this.jobSeekerApplyRepository = jobSeekerApplyRepository;
    }

    public List<JobSeekerApply> getAppliedJobsBySeeker(User user) {
        return jobSeekerApplyRepository.findByUser(user);
    }

    public List<JobSeekerApply> getAppliedJobsByJob(JobPost jobPost) {
        return  jobSeekerApplyRepository.findByJobPost(jobPost);
    }

    public void applyForAJobPost(User user, JobPost jobPost) {
        JobSeekerApply jobSeekerApply = JobSeekerApply.builder()
                .user(user)
                .jobPost(jobPost)
                .coverLetter("")
                .applyDate(new Date(System.currentTimeMillis())
                        .toString())
                .build();

        jobSeekerApplyRepository.save(jobSeekerApply);
    }

    public void deleteAllByUser(User user) {
        jobSeekerApplyRepository.deleteAllByUser(user);
    }

    public void deleteAllByJobPost(JobPost jobPost) {
        jobSeekerApplyRepository.deleteAllByJobPost(jobPost);
    }
}
