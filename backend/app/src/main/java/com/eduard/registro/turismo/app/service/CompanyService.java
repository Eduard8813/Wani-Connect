package com.eduard.registro.turismo.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.eduard.registro.turismo.app.model.Company;
import com.eduard.registro.turismo.app.repository.CompanyRepository;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public Company createCompany(Company company) {
        company.setPassword(passwordEncoder.encode(company.getPassword()));
        return companyRepository.save(company);
    }

    public boolean existsByUsername(String username) {
        return companyRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return companyRepository.existsByEmail(email);
    }

    public java.util.Optional<Company> findByUsername(String username) {
        return companyRepository.findByUsername(username);
    }
}