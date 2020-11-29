package idv.darren.crm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import idv.darren.crm.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

	void deleteById(Long id);
}
