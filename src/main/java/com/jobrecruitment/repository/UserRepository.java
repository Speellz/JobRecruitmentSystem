package com.jobrecruitment.repository;

import com.jobrecruitment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByCompanyId(Integer companyId);


    @Query("SELECT u FROM User u WHERE u.company.id = :companyId AND u.role = 'RECRUITER'")
    List<User> findByCompanyIdAndRecruiterRole(@Param("companyId") Long companyId);
}
