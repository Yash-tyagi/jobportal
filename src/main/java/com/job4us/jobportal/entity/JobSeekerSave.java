package com.job4us.jobportal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id","job_post_id"})
})
public class JobSeekerSave {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "job_post_id",referencedColumnName = "id")
    private JobPost jobPost;
}
