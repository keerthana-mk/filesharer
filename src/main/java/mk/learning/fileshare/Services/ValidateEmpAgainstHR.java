package mk.learning.fileshare.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import mk.learning.fileshare.constants.HashmapConstants;

@Component
public class ValidateEmpAgainstHR {

	private final Logger logger = LoggerFactory.getLogger(ValidateEmpAgainstHR.class);

	public ArrayList<String> FindAllEmpForHR()

	{
		ArrayList<String> EmpListForHR = new ArrayList<>();
		Map<String, String> empHrMap = HashmapConstants.mapOfMaps.get(HashmapConstants.HR_DETAILS);
		for (String entry : empHrMap.keySet()) {
			String curHr = empHrMap.get(entry);
			logger.info("Cur HR: {}",curHr);
			if (curHr.equalsIgnoreCase("1626285.0")) {
				logger.info("In IF");
				EmpListForHR.add(entry);
			}
		
		}
		logger.info("Emp_list for HR = {}",EmpListForHR);
		return EmpListForHR;
	}

	private String to_string(String string) {
		// TODO Auto-generated method stub
		return null;
	}

}
