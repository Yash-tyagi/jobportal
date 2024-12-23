package com.job4us.jobportal.service;

import com.job4us.jobportal.entity.JobPost;
import com.job4us.jobportal.entity.JobSeekerApply;
import com.job4us.jobportal.entity.JobSeekerSave;
import com.job4us.jobportal.entity.User;
import com.job4us.jobportal.repository.JobSeekerSaveRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerSaveService {
    private final JobSeekerSaveRepository jobSeekerSaveRepository;

    public JobSeekerSaveService(JobSeekerSaveRepository jobSeekerSaveRepository) {
        this.jobSeekerSaveRepository = jobSeekerSaveRepository;
    }

    public List<JobSeekerSave> getAppliedJobsBySeeker(User user) {
        return jobSeekerSaveRepository.findByUser(user);
    }

    public List<JobSeekerSave> getAppliedJobsByJob(JobPost jobPost) {
        return  jobSeekerSaveRepository.findByJobPost(jobPost);
    }

    public void saveAJobPost(User user, JobPost jobPost) {
        JobSeekerSave jobSeekerSave = JobSeekerSave.builder()
                .user(user)
                .jobPost(jobPost)
                .build();
        jobSeekerSaveRepository.save(jobSeekerSave);
    }

    public void deleteAllByUser(User user) {
        jobSeekerSaveRepository.deleteAllByUser(user);
    }

    public void deleteAllByJobPost(JobPost jobPost) {
        jobSeekerSaveRepository.deleteAllByJobPost(jobPost);
    }
}
