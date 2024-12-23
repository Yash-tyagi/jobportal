package com.job4us.jobportal.repository;

import com.job4us.jobportal.entity.JobPost;
import com.job4us.jobportal.entity.JobSeekerSave;
import com.job4us.jobportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSeekerSaveRepository extends JpaRepository<JobSeekerSave, Integer> {

    public List<JobSeekerSave> findByUser(User user);
    public List<JobSeekerSave> findByJobPost(JobPost jobPost);
    void deleteAllByUser(User user);
    void deleteAllByJobPost(JobPost jobPost);
}
