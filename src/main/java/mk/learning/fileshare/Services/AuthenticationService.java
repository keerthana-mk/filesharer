package mk.learning.fileshare.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import mk.learning.fileshare.constants.Queries;

@Component
public class AuthenticationService implements AuthenticationProvider {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		logger.info("in authentication service");
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		logger.info("authentication user {}",username);
		if (verifyUserFromDb(username, password)) {
			logger.info("user is valid, granting access");
			final List<GrantedAuthority> grantedAuths = new ArrayList<>();
			grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
			final UserDetails principal = new User(username, password, grantedAuths);
			final Authentication auth = new UsernamePasswordAuthenticationToken(principal, password, grantedAuths);
			return auth;
		} else {
			logger.info("user is invalid, rejecting access");
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	public boolean verifyUserFromDb(String username, String password) {
		try {
		List<Map<String, Object>> result = jdbcTemplate.queryForList(Queries.AUTH_QUERY, username, password);
		if (result == null || result.size() < 1)
			return false;
		else
			return true;
		}catch(Exception e) {
			logger.error("Error while accessing db",e);
			return false;
		}
	}

}
