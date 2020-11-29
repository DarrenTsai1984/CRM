package idv.darren.crm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	PasswordEncoder password() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(password());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable();

		http.formLogin()
				.and().authorizeRequests() // 定義哪些 URL 需要驗證，哪些不需要
				.antMatchers("/**/add/**").hasAnyAuthority("ROLE_SUPER_USER", "ROLE_OPERATOR")
				.antMatchers("/**/update/**", "/**/delete/**").hasAnyAuthority("ROLE_SUPER_USER", "ROLE_MANAGER")
				.antMatchers("/**/get/**").hasAnyAuthority("ROLE_SUPER_USER", "ROLE_MANAGER", "ROLE_OPERATOR")
				.anyRequest().authenticated() // 所有請求皆可訪問
				.and().csrf().disable(); // 關閉 csrf 防護
	}

}
