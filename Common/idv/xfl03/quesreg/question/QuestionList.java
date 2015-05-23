package idv.xfl03.quesreg.question;

import idv.xfl03.quesreg.config.MainConfig;

import java.io.File;
import java.util.ArrayList;

public class QuestionList {
	public ArrayList<ArrayList<Question>> alq;
	public int[] sum={-1,0,0,0,0};
	
	private final static QuestionFileFilter qff0=new QuestionFileFilter();;
	private final static QuestionFolderFilter qff1=new QuestionFolderFilter();
	private final static int SPLIT_BASIC=10000;
	
	private MainConfig mc;
	public QuestionList(File questionFolder,MainConfig mc){
		this.mc=mc;
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
			if(!f.getAbsolutePath().endsWith(mc.language)){
				continue;
			}
			File[] subFile=f.listFiles(qff0);
			for(File f1 : subFile){
				Question q=new Question(f1);
				alq.get(q.type).add(q);
				//System.out.println(q.type+" "+q.question);
				sum[q.type]++;
			}
		}
		
		for(int i=1;i<=4;i++){
			if(sum[i]<mc.questionNumber[i]){
				System.out.println("TYPE "+i+" "+sum[i]+"/"+mc.questionNumber[i]);
				for(int j=sum[i];j<mc.questionNumber[i];j++){
					alq.get(i).add(new Question(i));
					//System.out.println(i+" blank");
				}
			}
		}
		
	}
	public String getRandomQuestions(){
		//System.out.println("getRandomQuestions");
		StringBuilder sb=new StringBuilder();
		for(int i=1;i<=4;i++){
			//System.out.println(i);
			if(mc.questionNumber[i]==0){
				//System.out.println(i+" Needs Nothing");
				continue;
			}
			int[] id=new int[mc.questionNumber[i]];
			for(int j=0;j<mc.questionNumber[i];j++){
				int max=alq.get(i).size();
				int temp=i*SPLIT_BASIC+getRandom(1,max);
				int loopCount=0;
				while(isUsed(id,temp)&&loopCount<=mc.questionNumber[i]){
					loopCount++;
					temp=i*10000+getRandom(1,alq.get(i).size()-1);
				}
				if(isUsed(id,temp)){
					//Too much Loop Times
					for(int k=i*SPLIT_BASIC+1;i<=i*SPLIT_BASIC+max;i++){
						if(!isUsed(id,k)){
							temp=k;
							break;
						}
					}
				}
				//System.out.println(temp+" "+max);
				id[j]=temp;
			}
			if(sb.length()>0){
				sb.append(",");
			}
			sb.append(idsToString(id));
		}
		return sb.toString();
	}
	public Question getQuestion(int id){
		int type=id/SPLIT_BASIC;
		int i=id%SPLIT_BASIC;
		return alq.get(type).get(i-1);
	}
	public int[] getIds(String ids){
		String[] temp=ids.split(",");
		int[] id=new int[temp.length];
		for(int i=0;i<temp.length;i++){
			id[i]=Integer.parseInt(temp[i]);
		}
		return id;
	}
	private int getRandom(int min,int max){
		return (int)(Math.random()*(max-min+1))+min;
	}
	private boolean isUsed(int[] array,int test){
		for(int i=0;i < array.length;i++){
			if(array[i]==test){
				return true;
			}
		}
		return false;
	}
	private String idsToString(int[] array){
		StringBuilder sb=new StringBuilder();
		sb.append(array[0]);
		for(int i=1;i<array.length;i++){
			sb.append(",");
			sb.append(""+array[i]);
		}
		return sb.toString();
	}
}
