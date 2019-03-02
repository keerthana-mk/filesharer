package mk.learning.fileshare.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
		return result.get(0).get("Username").toString();
		
	}
	
}
