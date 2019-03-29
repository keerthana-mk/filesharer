package mk.learning.fileshare.Services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import mk.learning.fileshare.constants.HashmapConstants;
import mk.learning.fileshare.constants.Queries;

@Component
public class UserRegistration {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	AuthenticationService auth;

	Logger logger = LoggerFactory.getLogger(UserRegistration.class);

	public boolean CreateNewUser(String Username, String Password, String Func) {

		Password = auth.EncryptPassword(Password);
		logger.info("encrypted password is {}", Password);
		int returnINS = jdbcTemplate.update(Queries.INSERT_SQL, Username, Password, Func,
				HashmapConstants.NEW_USER_FLAG);
		if (returnINS < 1) {
			logger.info("Create user failed={}", returnINS);
			return false;
		} else {
			logger.info("USER Created={}", returnINS);
			return true;
		}
	}

	public boolean isFirstTimeLogin(String username) {
		try {
			List<Map<String, Object>> result = jdbcTemplate.queryForList(Queries.FIRST_LOGIN_QUERY, username);
			if (result.size() == 0)
				return false;
			else {
				BigDecimal firstLoginFlag = (BigDecimal) result.get(0).get("firstloginflag");
				if (firstLoginFlag.toBigInteger().intValue() == -1)
					return true;
				return false;
			}
		} catch (Exception e) {
			logger.error("error while querying for first time login flag - ", e);
			return false;
		}
	}
	
	public boolean deleteUser(String username) {
		try {
			int result = jdbcTemplate.update(Queries.DELETE_USER_QUERY,username);
			if(result>0) {
				logger.info("User Deleted={}",username);
				return true;
			}
			else
			{ 
				logger.info("Couldn't Delete the User {}",username);
				return false;
			}
				
		}catch(Exception e) {
			logger.error("error while deleting user ",e);
			return false;
		}
	}
	
	public boolean updatePassword(String username, String password) {
		try {
			int result = jdbcTemplate.update(Queries.UPDATE_PASSWORD_QUERY, password,
					HashmapConstants.EXISTING_USER_FLAG, username);
			if (result > 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			logger.error("error while updating password ", e);
			return false;
		}
	}

}
