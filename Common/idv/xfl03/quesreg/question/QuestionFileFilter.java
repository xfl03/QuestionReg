package idv.xfl03.quesreg.question;

import java.io.File;
import java.io.FileFilter;

public class QuestionFileFilter implements FileFilter {

	@Override
	public boolean accept(File file) {
		if(!file.isFile()){
			return false;
		}
		if(!file.getName().endsWith(".txt")){
			return false;
		}
		if(!file.canRead()){
			return false;
		}
		if(file.length()<=0){
			return false;
		}
		return true;
	}

}
