package idv.xfl03.quesreg.question;

import java.io.File;
import java.io.FileFilter;

public class QuestionFolderFilter implements FileFilter {

	@Override
	public boolean accept(File pathname) {
		return pathname.isDirectory() && pathname.listFiles(new QuestionFileFilter()).length>0 ; 
	}

}
