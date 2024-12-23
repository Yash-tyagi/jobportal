package com.job4us.jobportal.repository;

import com.job4us.jobportal.entity.JobPost;
import com.job4us.jobportal.entity.JobSeekerApply;
import com.job4us.jobportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobSeekerApplyRepository extends JpaRepository<JobSeekerApply,Integer> {
    List<JobSeekerApply> findByUser(User user);
    List<JobSeekerApply> findByJobPost(JobPost jobPost);

    void deleteAllByUser(User user);
    void deleteAllByJobPost(JobPost jobPost);
}
