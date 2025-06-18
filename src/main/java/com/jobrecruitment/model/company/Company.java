package com.jobrecruitment.model.company;

import com.jobrecruitment.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "Companies")
@Data
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;
    private String website;
    private String status;

    @Column(name = "admin_approved_by", nullable = true)
    private Integer adminApprovedBy;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "logo_path")
    private String logoPath;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }
}
