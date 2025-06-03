package com.jobrecruitment.service.company;

import com.jobrecruitment.model.company.Company;
import com.jobrecruitment.model.User;
import com.jobrecruitment.repository.company.CompanyRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public boolean registerCompany(Company company, User owner) {
        if (companyRepository.findByEmail(company.getEmail()).isPresent()) {
            return false;
        }
        company.setStatus("Pending");
        company.setOwner(owner);
        companyRepository.save(company);
        return true;
    }


    public List<Company> getPendingCompanies() {
        return companyRepository.findByStatus("Pending");
    }

    public boolean approveCompany(int companyId, int adminId) {
        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();
            company.setStatus("Approved");
            company.setAdminApprovedBy(adminId);
            companyRepository.save(company);
            return true;
        }
        return false;
    }

    public boolean rejectCompany(int companyId) {
        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();
            company.setStatus("Rejected");
            companyRepository.save(company);
            return true;
        }
        return false;
    }

    public Company findById(Integer id) {
        Optional<Company> company = companyRepository.findById(id);
        return company.orElse(null);
    }

    public List<Company> getApprovedCompanies() {
        return companyRepository.findByStatus("Approved");
    }

}
