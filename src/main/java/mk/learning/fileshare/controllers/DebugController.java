package mk.learning.fileshare.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import mk.learning.fileshare.constants.HashmapConstants;

@RestController
public class DebugController {

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
}
