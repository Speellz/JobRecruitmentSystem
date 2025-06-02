package com.jobrecruitment.model;

import com.jobrecruitment.model.company.Branch;
import com.jobrecruitment.model.company.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = true)
    private Company company;

    public enum Role {
        APPLICANT("APPLICANT"),
        ADMIN("ADMIN"),
        COMPANY("COMPANY"),
        RECRUITER("RECRUITER");

        private final String role;

        Role(String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }

        @Override
        public String toString() {
            return this.role;
        }
    }
}
