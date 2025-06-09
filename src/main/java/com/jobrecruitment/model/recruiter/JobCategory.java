package com.jobrecruitment.model.recruiter;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Job_Categories")
public class JobCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;
}
