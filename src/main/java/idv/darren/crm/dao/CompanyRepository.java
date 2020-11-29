package idv.darren.crm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import idv.darren.crm.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

	void deleteById(Long id);
}
