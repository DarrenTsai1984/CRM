package idv.darren.crm.service.impl;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import idv.darren.crm.dao.CompanyRepository;
import idv.darren.crm.dto.CompanyDto;
import idv.darren.crm.entity.Company;
import idv.darren.crm.service.CompanyService;

@Service("companyService")
public class CompanyServiceImpl implements CompanyService {
	
	@Autowired
	private CompanyRepository companyRepository;

	@Override
	public Company save(CompanyDto companyDto) {
		Company company = new Company();
		BeanUtils.copyProperties(companyDto, company);
		return companyRepository.save(company);
	}

	@Override
	public Company update(Long companyId, CompanyDto companyDto) {
		Company company = companyRepository.findById(companyId).orElse(null);
		BeanUtils.copyProperties(companyDto, company, "id");
		return companyRepository.save(company);
	}

	@Override
	public Optional<Company> getCompanyById(Long id) {
		return companyRepository.findById(id);
	}

	@Override
	public void delete(Long id) {
		companyRepository.deleteById(id);
	}

}
