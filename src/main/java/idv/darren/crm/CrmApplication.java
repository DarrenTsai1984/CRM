package idv.darren.crm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import idv.darren.crm.dao.ClientRepository;
import idv.darren.crm.dao.CompanyRepository;
import idv.darren.crm.dao.UserRepository;
import idv.darren.crm.entity.User;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class CrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrmApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(CompanyRepository companyRepository, ClientRepository clientRepository,
			UserRepository userRepository) {
		return args -> {
			List<User> userList = new ArrayList<>();
			User superUser = new User();
			superUser.setUsername("root");
			superUser.setPassword("root");
			superUser.setRole("ROLE_SUPER_USER");
			userList.add(superUser);

			User manager = new User();
			manager.setUsername("ted");
			manager.setPassword("123");
			manager.setRole("ROLE_MANAGER");
			userList.add(manager);

			User operator = new User();
			operator.setUsername("darren");
			operator.setPassword("123");
			operator.setRole("ROLE_OPERATOR");
			userList.add(operator);

			userRepository.saveAll(userList);
		};
	}

}
