package idv.darren.crm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import idv.darren.crm.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
