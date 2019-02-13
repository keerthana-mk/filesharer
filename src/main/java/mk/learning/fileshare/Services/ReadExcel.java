package mk.learning.fileshare.Services;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import mk.learning.fileshare.constants.HashmapConstants;

@Component
public class ReadExcel {

	private final Logger logger = LoggerFactory.getLogger(ReadExcel.class);
	
	public boolean setMapData(String Filename) {
		DataFormatter formatter = new DataFormatter();
		String path = Filename;// "E:\testshare.xlsx";
		try {
			FileInputStream inputStream = new FileInputStream(path);
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			int LastRow = sheet.getLastRowNum();
			if (HashmapConstants.mapOfMaps == null)
				HashmapConstants.mapOfMaps = new HashMap<String, Map<String, String>>();
			Map<String, String> MapData = new HashMap<String, String>();
			for (int i = 0; i <= LastRow; i++) {
				Row row = sheet.getRow(i);
				// Add key cell
				Cell keyCell = row.getCell(0);
				// Add value cell
				Cell valueCell = row.getCell(1);
				// Store data of value and key cell in string
				String key = Double.toString(keyCell.getNumericCellValue());
				String value = Double.toString(valueCell.getNumericCellValue());
				// Putting key, value to MapData
				MapData.put(key, value);
				// Putting data to ExcelMapData
			}
			HashmapConstants.mapOfMaps.put(HashmapConstants.HR_DETAILS, MapData);
			return true;
		} catch (IOException e) {
			logger.error("IOException while parsing excel file ",e);
			return false;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}

	}

}
