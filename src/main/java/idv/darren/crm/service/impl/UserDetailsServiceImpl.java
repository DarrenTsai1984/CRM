package idv.darren.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import idv.darren.crm.dao.UserRepository;
import idv.darren.crm.entity.User;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findById(username).orElse(null);
		if(null == user) {
			throw new UsernameNotFoundException("該使用者不存在！");
		}
		List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole());
		return new org.springframework.security.core.userdetails.User(user.getUsername(), 
				new BCryptPasswordEncoder().encode(user.getPassword()), auths);
	}

}
