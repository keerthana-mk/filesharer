package mk.learning.fileshare.Services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import mk.learning.fileshare.constants.HashmapConstants;

@Component
public class SearchFileBasedOnEmp {
	private final Logger logger = LoggerFactory.getLogger(SearchFileBasedOnEmp.class);

	/*
	 * public File searchPath(File file, String search) { // file=new File("E:\\");
	 * // search="1626285.pdf"; if (file.isDirectory()) { File[] files =
	 * file.listFiles(); String[] filePath = new String[10000]; for (File f : files)
	 * { File found = searchPath(f, search); if (found != null) {
	 * filePath[0]=file.getAbsolutePath(); return found; } }
	 * 
	 * } else { if(file.getName().equals(search)) { logger.info("else part"); return
	 * file; }
	 * 
	 * } return null;
	 * 
	 * }
	 */

	@Value("${downloadBaseDir}")
	String downloadBaseDir;

	@Value("${pathDelimiter}")
	String pathDelimiter;

	@Value("${hrDir:HR}")
	String hrDir;

	@Value("${finDir:FINANCE}")
	String finDir;

	// we need a function that will return a list of paths to files related to given
	// employee and functionality
	public ArrayList<String> getFilePaths4Employee(String empCode, String functionality) {
		String baseDir;
		if (functionality.equalsIgnoreCase(HashmapConstants.FUNCTIONALITY_HR))
			baseDir = downloadBaseDir + pathDelimiter + hrDir;
		else if (functionality.equalsIgnoreCase(HashmapConstants.FUNCTIONALITY_FIN))
			baseDir = downloadBaseDir + pathDelimiter + finDir;
		else
			return null;

		logger.info("baseDir = {}", baseDir);

		File baseDirFile = new File(baseDir);
		if (!baseDirFile.isDirectory()) {
			logger.info("baseDir is not a directory");
			return null;
		} else {
			logger.info("baseDir is a directory, traversing");
			return findAllEmployees(baseDirFile, empCode);
		}
	}

	private ArrayList<String> findAllEmployees(File baseDirFile, String empCode) {
		ArrayList<String> result = new ArrayList<String>();
		traverse(baseDirFile, result, empCode);
		return result;
	}

	private void traverse(File baseDirFile, ArrayList<String> result, String empCode) {
		logger.info("traversing file {}", baseDirFile.getAbsolutePath());
		if (baseDirFile.isDirectory()) {
			String[] baseDirFileChildren = baseDirFile.list();
			for (int i = 0; i < baseDirFileChildren.length; i++) {
				logger.info("calling traverse for {}", baseDirFileChildren[i]);
				traverse(new File(baseDirFile.getAbsoluteFile() + pathDelimiter + baseDirFileChildren[i]), result,
						empCode);
			}
		} else {
			String paddedEmpCodeWithExt = HashmapConstants.zeroString.substring(0, 8 - empCode.length()) + empCode
					+ HashmapConstants.PDF_EXTENSION;
			logger.info("paddedEmpCodeWithExt = {}", paddedEmpCodeWithExt);
			logger.info("in else -- {}", baseDirFile.getName());
			if (baseDirFile.getName().contains(paddedEmpCodeWithExt)) {
				logger.info("adding File {} to list", baseDirFile.getAbsolutePath());
				result.add(baseDirFile.getAbsolutePath());
			}
		}
	}

	// we need a function, which will form a zip of all the files
	public String zipFiles(ArrayList<String> filePaths) {
		try {
			String tempZipFilename = new Date().toString() + ".zip";
			File tempZipFile = new File(tempZipFilename);
			if (tempZipFile.exists() == false)
				tempZipFile.createNewFile();
			ZipOutputStream zipOpStream = new ZipOutputStream(new FileOutputStream(tempZipFile));
			for (String curFile : filePaths) {
				String[] curFileSplitNames = curFile.split(pathDelimiter);
				String zipEntryName = "";
				int index = 0;
				for (int i = 0; i < curFileSplitNames.length; i++) {
					if (curFileSplitNames[i].equalsIgnoreCase("downloads"))
						index = i;
				}
				for (int i = index + 1; i < curFileSplitNames.length; i++)
					zipEntryName = zipEntryName + pathDelimiter + curFileSplitNames[i];
				if (zipEntryName.equalsIgnoreCase(""))
					zipEntryName = curFile;
				logger.info("current zip entry = {}", zipEntryName);
				ZipEntry ze = new ZipEntry(zipEntryName);
				byte[] fileBytes = Files.readAllBytes(Paths.get(curFile));
				zipOpStream.putNextEntry(ze);
				zipOpStream.write(fileBytes);
				zipOpStream.closeEntry();
			}
			zipOpStream.flush();
			zipOpStream.close();
			return tempZipFilename;
		}catch(FileNotFoundException e) {
			return "filenotfound";
		}catch (IOException e) {
			return HashmapConstants.ZIP_EXCEPTION;
		}
	}
}
