package idv.darren.crm.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import idv.darren.crm.dto.CompanyDto;
import idv.darren.crm.entity.Company;
import idv.darren.crm.service.CompanyService;

@Controller
@RequestMapping(value = "/company", produces = "application/json; charset=utf-8")
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@GetMapping("/get/{id}")
	public ResponseEntity<?> getCompany(@PathVariable Long id) {
		Optional<Company> company = companyService.getCompanyById(id);
		return company.map(response -> ResponseEntity.ok().body(company))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@PostMapping("/add")
	public ResponseEntity<Company> addCompany(@RequestBody CompanyDto companyDto) {
		Company company = companyService.save(companyDto);
		return ResponseEntity.ok().body(company);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody CompanyDto companyDto) {
		Company company = companyService.update(id, companyDto);
		return ResponseEntity.ok().body(company);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
		companyService.delete(id);
		return ResponseEntity.ok().build();
	}

}
