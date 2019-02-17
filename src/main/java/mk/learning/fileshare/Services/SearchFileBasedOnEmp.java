package mk.learning.fileshare.Services;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@ComponentScan
public class SearchFileBasedOnEmp {
	private final Logger logger = LoggerFactory.getLogger(SearchFileBasedOnEmp.class);
	public File searchPath(File file, String search) {
	//	file=new File("E:\\");
	//	search="1626285.pdf";		
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			String[] filePath = new String[10000];
			for (File f : files) {
				File found = searchPath(f, search);
				if (found != null) {
					filePath[0]=file.getAbsolutePath();
				  return found;
			}
			}

		}
		else 
		{
			if(file.getName().equals(search))
			{
				logger.info("else part");
				return file;
			}
				
		}
		return null;

	}
}
