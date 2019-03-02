package mk.learning.fileshare.Services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
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
import org.springframework.web.multipart.MultipartFile;

import mk.learning.fileshare.constants.HashmapConstants;

@Component
public class FileServices {

	private final Logger logger = LoggerFactory.getLogger(FileServices.class);

	public boolean setMapData(String Filename) {

		String path = Filename;// "E:\testshare.xlsx";
		logger.info("Path={}", path);
		try {
			FileInputStream inputStream = new FileInputStream(path);
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			int LastRow = sheet.getLastRowNum();
			if (HashmapConstants.mapOfMaps == null)
				HashmapConstants.mapOfMaps = new HashMap<String, Map<String, String>>();
			Map<String, String> MapData = new HashMap<String, String>();
			DataFormatter formatter = new DataFormatter();
			for (int i = 0; i <= LastRow; i++) {
				Row row = sheet.getRow(i);
				// Add key cell
				Cell keyCell = row.getCell(0);
				// Add value cell
				Cell valueCell = row.getCell(1);
				// Store data of value and key cell in string
				// String key = keyCell.getStringCellValue();
				String key = formatter.formatCellValue(keyCell);
				// String value =valueCell.getStringCellValue();
				String value = formatter.formatCellValue(valueCell);
				// Putting key, value to MapData
				MapData.put(key, value);
				// Putting data to ExcelMapData
			}
			HashmapConstants.mapOfMaps.put(HashmapConstants.HR_DETAILS, MapData);
			workbook.close();
			return true;
		} catch (IOException e) {
			logger.error("IOException while parsing excel file ", e);
			return false;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}

	}

	public boolean saveMultipartFile(MultipartFile inputFile, String targetPath) {
		try {
			File uploadedFile = new File(targetPath);
			if (uploadedFile.exists())
				uploadedFile.delete();
			else
				uploadedFile.createNewFile();
			InputStream inputStream = inputFile.getInputStream();
			FileOutputStream fStream = new FileOutputStream(uploadedFile);
			BufferedWriter writer = new BufferedWriter(new FileWriter(uploadedFile));
			int ch = 0;
			while ((ch = inputStream.read()) != -1)
				fStream.write(ch);
			fStream.flush();
			fStream.close();
			writer.flush();
			writer.close();
			return true;
		} catch (IOException e) {
			logger.error("error while creating file at path {}", targetPath);
			logger.error(e.getMessage(), e);
			return false;
		} catch (Exception e) {
			logger.error("error while creating file at path {}", targetPath);
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	public void cleanTempFiles(String baseDirPath) {
		File file = new File(baseDirPath);
		long thresholdTime = new Date().getTime() - 2 * 60 * 1000;
		for (File Dfile : file.listFiles()) {
			// iterate over file.listFiles())
			long lm = Dfile.lastModified();
			if (lm < thresholdTime)
				deleteFile(Dfile.getAbsolutePath());
		}

	}

	public void deleteFile(String filePath) {
		// create a file object using the filePath
		File file = new File(filePath);
		if (file.exists())
			file.delete();
		// check if file exists
		// if exists then use file function to delete
	}
}
