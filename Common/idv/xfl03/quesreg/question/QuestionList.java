package idv.xfl03.quesreg.question;

import java.io.File;
import java.util.ArrayList;

public class QuestionList {
	public ArrayList<ArrayList<Question>> alq;
	private final static QuestionFileFilter qff0=new QuestionFileFilter();;
	private final static QuestionFolderFilter qff1=new QuestionFolderFilter();
	public QuestionList(File questionFolder){
		alq=new ArrayList<ArrayList<Question>>();
		alq.add(null);
		for(int i=1;i<=4;i++){
			alq.add(new ArrayList<Question>());
		}
		File[] subFolder=questionFolder.listFiles(qff1);
		
		if(subFolder.length==0){
			//First time
			System.out.println("Init Questions!");
		}
		
		for(File f : subFolder){
			File[] subFile=f.listFiles(qff0);
			for(File f1 : subFile){
				Question q=new Question(f1);
				alq.get(q.type).add(q);
				System.out.println(q.type);
			}
		}
		
	}
	
}
