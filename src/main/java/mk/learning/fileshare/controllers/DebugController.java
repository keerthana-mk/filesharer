package mk.learning.fileshare.controllers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import mk.learning.fileshare.constants.HashmapConstants;

@RestController
public class DebugController {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@GetMapping(value = "/HashmapSize")
	public int getHashMapSize() {
		if (HashmapConstants.mapOfMaps == null)
			return 0;
		Map<String, String> hrMap = HashmapConstants.mapOfMaps.get(HashmapConstants.HR_DETAILS);
		return (hrMap == null ? 0 : hrMap.size());
	}

	@GetMapping(value = "/HashmapContents")
	public Map<String, String> getMap() {
		if (HashmapConstants.mapOfMaps == null)
			return null;
		else
			return HashmapConstants.mapOfMaps.get(HashmapConstants.HR_DETAILS);
	}
	
	@GetMapping(value="/dbGet")
	public String getDbDetails() {
		List<Map<String,Object>> result = jdbcTemplate.queryForList("select * from FileSharer_UserId");
			return (result.get(0).get("Username").toString()+" "+result.get(0).get("password").toString());
		
	}
	
	/*@GetMapping(value="/encrypt")
	public String EncryptMD5(String password) throws Exception
	{
		Object principal = SecurityContextHolder.getContext().getAuthentication().getCredentials();
		if(principal instanceof UserDetails)
	password = ((UserDetails) principal).getPassword();
else
	password= principal.toString();	
	        MessageDigest m=MessageDigest.getInstance("MD5");
	        m.update(password.getBytes(),0,password.length());
	        System.out.println("MD5: "+new BigInteger(1,m.digest()).toString(16));
			return new BigInteger(1,m.digest()).toString(16) ;
			
	}*/
}
