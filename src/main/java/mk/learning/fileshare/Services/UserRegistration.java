package mk.learning.fileshare.Services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

	public ArrayList<String> output_date() {
		LocalDate d = LocalDate.now();
		LocalDate e = d.plusMonths(2);
		Locale l = Locale.US;
		ArrayList<String> pass_dates = new ArrayList<>();
		pass_dates.add(0, d.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy", l)));
		pass_dates.add(1, e.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy", l)));
		return pass_dates;
	}
	
	/*public ArrayList<String> dates(){
		ArrayList<String> pass_date = new ArrayList<>();
		java.util.Date today=new Date();
		java.util.Date monthplus60=new Date();
		//monthplus60=monthplus60+
		java.sql.Date fromdate=new java.sql.Date(today.getTime());
		//java.sql.Date todate=new java.sql.Date(today.getTime());
		SimpleDateFormat f=new SimpleDateFormat("dd-MM-yyyy");
		pass_date.add(0, fromdate.toString());
		System.out.println("today_date="+f.format(fromdate));
		return pass_date;
		
	}
*/
	//ArrayList<String> loginDate = output_date();
	ArrayList<String> loginDate = output_date();

	public boolean CreateNewUser(String Username, String Password, String Func) {

		System.out.println("create Date= " + loginDate.get(0) + "  -" + loginDate.get(1));
		Password = auth.EncryptPassword(Password);
		logger.info("encrypted password is {}", Password);
		/*int returnINS = jdbcTemplate.update(Queries.INSERT_SQL, Username, Password, Func,
				HashmapConstants.NEW_USER_FLAG, loginDate.get(0), loginDate.get(1));*/
		int returnINS = jdbcTemplate.update(Queries.INSERT_SQL, Username, Password, Func,
				HashmapConstants.NEW_USER_FLAG,loginDate.get(0).toString(),loginDate.get(1).toString());
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

	public boolean PasswordExpired(String Username) {
		int r = jdbcTemplate.update(Queries.UPDATE_PASSWORD_EXPIRY, Username,loginDate.get(0).toString());
		System.out.println("R in passwordexpired="+r);
		if (r > 0)
		{
			logger.info("User {} has expired ",Username);
			return false;
		}
		logger.info("User {} not expired",Username);
		return true;
	}

	public boolean deleteUser(String username) {
		try {
			int result = jdbcTemplate.update(Queries.DELETE_USER_QUERY, username);
			if (result > 0) {
				logger.info("User Deleted={}", username);
				return true;
			} else {
				logger.info("Couldn't Delete the User {}", username);
				return false;
			}

		} catch (Exception e) {
			logger.error("error while deleting user ", e);
			return false;
		}
	}

	public boolean updatePassword(String username, String password) {
		try {/*
			int result = jdbcTemplate.update(Queries.UPDATE_PASSWORD_QUERY, password,
					HashmapConstants.EXISTING_USER_FLAG, loginDate.get(0), loginDate.get(1), username);*/
			int result = jdbcTemplate.update(Queries.UPDATE_PASSWORD_QUERY, password,
					HashmapConstants.EXISTING_USER_FLAG,loginDate.get(0).toString(),loginDate.get(1).toString(), username);
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
