package idv.darren.crm.service;

import java.util.Optional;

import idv.darren.crm.dto.CompanyDto;
import idv.darren.crm.entity.Company;

public interface CompanyService {

	Company save(CompanyDto companyDto);
	
	Company update(Long companyId, CompanyDto companyDto);
	
	Optional<Company> getCompanyById(Long companyId);
	
	void delete(Long companyId);
}
