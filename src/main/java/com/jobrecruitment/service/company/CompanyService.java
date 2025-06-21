package com.jobrecruitment.service.company;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.company.Company;
import com.jobrecruitment.model.company.CompanyStatus;
import com.jobrecruitment.repository.company.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        company.setStatus(CompanyStatus.PENDING);
        company.setOwner(owner);
        companyRepository.save(company);
        return true;
    }

    public List<Company> getPendingCompanies() {
        return companyRepository.findByStatus(CompanyStatus.PENDING);
    }

    public boolean approveCompany(int companyId, int adminId) {
        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();
            company.setStatus(CompanyStatus.APPROVED);
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
            company.setStatus(CompanyStatus.REJECTED);
            companyRepository.save(company);
            return true;
        }
        return false;
    }

    public Company findById(Integer id) {
        return companyRepository.findById(id).orElse(null);
    }

    public List<Company> getApprovedCompanies() {
        return companyRepository.findByStatus(CompanyStatus.APPROVED);
    }

    public boolean deleteCompanyById(int id) {
        if (companyRepository.findById(id).isPresent()) {
            companyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void saveCompany(Company company) {
        companyRepository.save(company);
    }
}
