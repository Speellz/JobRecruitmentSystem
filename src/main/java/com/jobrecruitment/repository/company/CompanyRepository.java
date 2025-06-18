package com.jobrecruitment.repository.company;

import com.jobrecruitment.model.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findByEmail(String email);
    Optional<Company> findByOwnerId(Long ownerId);
    List<Company> findByStatus(String status);

}
