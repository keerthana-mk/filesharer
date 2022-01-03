package mk.learning.fileshare.Services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;
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

	@Autowired
	UserRegistration registerService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		logger.info("in authentication service");
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		logger.info("authentication user ={} on {}", username);
		// Added by kothimari
		password = EncryptPassword(password);
		// logger.info("Password={}", password);
		if (verifyUserFromDb(username, password)) {
			logger.info("user is valid, granting access");
			final List<GrantedAuthority> grantedAuths = new ArrayList<>();
			grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
			final UserDetails principal = new User(username, password, grantedAuths);
			final Authentication auth = new UsernamePasswordAuthenticationToken(principal, password, grantedAuths);
			return auth;
		} else {
			logger.info("user {} is invalid, rejecting access", username);
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	public boolean verifyUserFromDb(String username, String password) {
		try {
			System.out.println("In vrify db, date==" + "'" + registerService.loginDate.get(0) + "'");
			List<Map<String, Object>> result = jdbcTemplate.queryForList(Queries.AUTH_QUERY, username, password);
			if (result == null || result.size() < 1) { // means user is not valid
				logger.info("Username or password incorrect for {}. so login failed!!", username);
				return false;
			} else {
				logger.info("User {} verified from DB", username);
				boolean passwordExpired = registerService.PasswordExpired(username);
				System.out.println("password expired = " + passwordExpired);
				logger.info("passwordExpired {} ", passwordExpired);
				return true;
			}
		} catch (Exception e) {
			logger.error("Error while accessing db", e);
			return false;
		}
	}

	public String EncryptPassword(String password) {
		try {
			
			  MessageDigest m = MessageDigest.getInstance("MD5");
			  m.update(password.getBytes(), 0, password.length()); 
			  String digest = new BigInteger(1, m.digest()).toString(16); 
			  System.out.println("MD5 fousername" + digest);
			  return digest;
			 
			/*  String HashedPwd = BCrypt.hashpw(password, BCrypt.gensalt());
			return HashedPwd;*/
		} catch (Exception e) {
			logger.info("Encrypt password failed");
			return null;
		}
	}

}
