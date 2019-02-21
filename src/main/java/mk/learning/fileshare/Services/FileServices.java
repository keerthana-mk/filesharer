package mk.learning.fileshare.Services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileServices {

	private final Logger logger = LoggerFactory.getLogger(FileServices.class);

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
		long thresholdTime = new Date().getTime() - 2*60*1000;
		for(File Dfile:file.listFiles()) {
			//iterare over file.listFiles())
			long lm = Dfile.lastModified();
			if(lm < thresholdTime )
				deleteFile(Dfile.getAbsolutePath());
		}
		
	}	
	public void deleteFile(String filePath) {
		//create a file object using the filePath
		File file=new File(filePath);
		if(file.exists())
			file.delete();
		//check if file exists
		//if exists then use file function to delete
	}
}
