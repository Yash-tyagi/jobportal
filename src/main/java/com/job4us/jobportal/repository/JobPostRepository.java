package com.job4us.jobportal.repository;

import com.job4us.jobportal.entity.IRecruiterJobs;
import com.job4us.jobportal.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPost,Integer> {
    @Query(value = "select count(s.user_id) as totalCandidates, j.id as jobPostId, j.job_title as jobTitle, l.id as locationId, l.city, l.state, l.country, c.id as companyId, c.name companyName  from " +
            "job_post j inner join job_location l on j.location_id=l.id " +
            "inner join job_company c on c.id=j.company_id " +
            "left join job_seeker_apply s on j.id=s.job_post_id " +
            "where j.posted_by_id = :recruiter " +
            "group by j.id,l.id,c.id",nativeQuery = true)
    List<IRecruiterJobs> getRecruiterJobs(@Param("recruiter") int recruiter);

    @Query(value = "select j.* from job_post j inner join job_location l " +
            "on j.location_id = l.id where " +
            "j.job_title like %:job% and " +
            "(l.city like %:location% or " +
            "l.state like %:location% or " +
            "l.country like %:location%) and " +
            "j.job_type in (:type) and " +
            "j.remote in (:remote) ",nativeQuery = true)
    List<JobPost> searchWithoutDate(@Param("job") String job, @Param("location") String location,
                                    @Param("type") List<String> type, @Param("remote") List<String> remote);

    @Query(value = "select j.* from job_post j inner join job_location l " +
            "on j.location_id = l.id where " +
            "j.job_title like %:job% and " +
            "(l.city like %:location% or " +
            "l.state like %:location% or " +
            "l.country like %:location%) and " +
            "j.job_type in (:type) and " +
            "j.remote in (:remote) and " +
            "j.posted_date >= :date",nativeQuery = true)
    List<JobPost> search(@Param("job") String job, @Param("location") String location,
                         @Param("type") List<String> type, @Param("remote") List<String> remote, @Param("date") LocalDate date);
}
