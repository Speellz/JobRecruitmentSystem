package com.jobrecruitment.model.applicant;

import com.jobrecruitment.model.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "User_Skills")
public class UserSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "skill_name")
    private String skillName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
