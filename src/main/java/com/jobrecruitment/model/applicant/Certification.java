package com.jobrecruitment.model.applicant;

import com.jobrecruitment.model.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Certifications")
@Data
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "certification_name")
    private String certificationName;

    @Column(name = "issued_by")
    private String issuedBy;

    @Column(name = "issue_date")
    private String issueDate;

    @Column(name = "expiration_date")
    private String expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}