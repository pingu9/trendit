package com.trendit.db.repository;

import com.trendit.db.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    Page<Company> findByCompanyNameContains(String companyName, Pageable pageable);
}
