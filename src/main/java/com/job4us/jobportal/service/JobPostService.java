package com.job4us.jobportal.service;

import com.job4us.jobportal.dto.JobPostDto;
import com.job4us.jobportal.entity.*;
import com.job4us.jobportal.repository.JobPostRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class JobPostService {
    private final JobPostRepository jobPostRepository;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;

    public JobPostService(JobPostRepository jobPostRepository, JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobSeekerSaveService) {
        this.jobPostRepository = jobPostRepository;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

    public JobPost addJobPost(JobPost jobPost) {
        return jobPostRepository.save(jobPost);
    }

    public List<JobPostDto> getRecruiterPosts(int recruiter) {
        List<IRecruiterJobs> jobs= jobPostRepository.getRecruiterJobs(recruiter);
        List<JobPostDto> jobPostDtos = new ArrayList<>();

        for (IRecruiterJobs job : jobs) {
            JobLocation jobLocation = new JobLocation(job.getLocationId(),job.getCity(),job.getState(),job.getCountry());
            JobCompany jobCompany = new JobCompany(job.getCompanyId(),"",job.getCompanyName());

            jobPostDtos.add(new JobPostDto(job.getTotalCandidates(), job.getJobPostId(),job.getJobTitle(),jobCompany,jobLocation));
        }
        return jobPostDtos;
    }

    public JobPost getAPost(int id) {
        return jobPostRepository.findById(id).orElse(null);
    }

    public void deletePost(int id) {
        JobPost jobPost = getAPost(id);
        jobSeekerApplyService.deleteAllByJobPost(jobPost);
        jobSeekerSaveService.deleteAllByJobPost(jobPost);
        jobPostRepository.deleteById(id);
    }

    public List<JobPost> getAll() {
        return jobPostRepository.findAll();
    }


    public List<JobPost> search(String job, String location, List<String> type, List<String> remote, LocalDate date) {
        return Objects.isNull(date) ? jobPostRepository.searchWithoutDate(job,location,type,remote)
                : jobPostRepository.search(job,location,type,remote,date);
    }
}
