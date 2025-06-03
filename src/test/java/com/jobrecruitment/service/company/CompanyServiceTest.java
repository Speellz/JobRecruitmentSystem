package com.jobrecruitment.service.company;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.company.Company;
import com.jobrecruitment.repository.company.CompanyRepository;
import com.jobrecruitment.service.common.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CompanyServiceTest {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserService userService;

    @Test
    void registerCompany_savesPendingCompany() {
        User owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        owner.setPassword("pass");
        userService.registerUser(owner);

        Company company = new Company();
        company.setName("TestCo");
        company.setEmail("test@example.com");

        boolean result = companyService.registerCompany(company, owner);

        Company saved = companyRepository.findByEmail("test@example.com").orElse(null);
        assertThat(result).isTrue();
        assertThat(saved).isNotNull();
        assertThat(saved.getStatus()).isEqualTo("Pending");
        assertThat(saved.getOwner()).isEqualTo(owner);
    }

    @Test
    void approveCompany_setsApprovedStatus() {
        User owner = new User();
        owner.setName("Owner2");
        owner.setEmail("owner2@example.com");
        owner.setPassword("pass");
        userService.registerUser(owner);

        Company company = new Company();
        company.setName("Test2");
        company.setEmail("test2@example.com");
        companyService.registerCompany(company, owner);

        Company saved = companyRepository.findByEmail("test2@example.com").orElseThrow();
        boolean result = companyService.approveCompany(saved.getId(), 10);

        Company approved = companyRepository.findById(saved.getId()).orElse(null);
        assertThat(result).isTrue();
        assertThat(approved.getStatus()).isEqualTo("Approved");
        assertThat(approved.getAdminApprovedBy()).isEqualTo(10);
    }

    @Test
    void rejectCompany_setsRejectedStatus() {
        User owner = new User();
        owner.setName("Owner3");
        owner.setEmail("owner3@example.com");
        owner.setPassword("pass");
        userService.registerUser(owner);

        Company company = new Company();
        company.setName("Test3");
        company.setEmail("test3@example.com");
        companyService.registerCompany(company, owner);

        Company saved = companyRepository.findByEmail("test3@example.com").orElseThrow();
        boolean result = companyService.rejectCompany(saved.getId());

        Company rejected = companyRepository.findById(saved.getId()).orElse(null);
        assertThat(result).isTrue();
        assertThat(rejected.getStatus()).isEqualTo("Rejected");
    }
}
