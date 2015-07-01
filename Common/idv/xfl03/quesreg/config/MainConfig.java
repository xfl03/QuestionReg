package idv.xfl03.quesreg.config;

import java.io.File;

public class MainConfig {
	
	//Default
	//Comments are in 'config.txt'
	public int examTimes=3;
	public int questionTypes=1;
	public int[] questionNumber;
	public int scoreModules=1;
	public int[][] scoreModule;
	public int[] passingScore;
	public boolean needAdminVerify=false;
	public String language="en";
	public int httpServerPort=1024;
	public int emailSupport=3;
	public int giveAdminPermission=0;
	public boolean useCode=true;
	public String serverIP="<Server IP>";
	
	public ConfigTool config;
	
	public MainConfig(File configFile){
		config=new ConfigTool(configFile);
		
		examTimes=config.getIntConfig("exam-times", examTimes);
		questionTypes=config.getIntConfig("question-types", questionTypes);
		scoreModules=config.getIntConfig("score-modules", scoreModules);
		needAdminVerify=config.getBooleanConfig("", needAdminVerify);
		language=config.getStringConfig("language", language);
		httpServerPort=config.getIntConfig("http-server-port", httpServerPort);
		emailSupport=config.getIntConfig("email-support", emailSupport);
		giveAdminPermission=config.getIntConfig("give-admin-permission", giveAdminPermission);
		useCode=config.getBooleanConfig("use-code", useCode);
		serverIP=config.getStringConfig("server-ip", serverIP);
		
		if(questionTypes<1){
			questionTypes=1;
		}
		questionNumber=new int[questionTypes+1];
		questionNumber[1]=config.getIntConfig("question-number-1", 5);
		for(int i=2;i<=questionTypes;i++){
			questionNumber[i]=config.getIntConfig("question-number-"+i, 0);
		}
		
		scoreModule=new int[scoreModules+1][];
		passingScore=new int[scoreModules+1];
		scoreModule[1]=stringToInts(config.getStringConfig("score-module-1", "0"));
		passingScore[1]=config.getIntConfig("passing-score-1", 60);
		for(int i=2;i<=scoreModules;i++){
			scoreModule[i]=stringToInts(config.getStringConfig("score-module-"+i, "0"));
			passingScore[i]=config.getIntConfig("passing-score-"+i, 0);
		}
	}
	
	//Get the text about email support
	public String getEmailSupportString(){
		switch(emailSupport){
		case 1:
			return "email";
		case 2:
			return "qq";
		default:
		case 3:
			return "email/qq";
		}
	}
	
	//Tool
	private int[] stringToInts(String str){
		String[] tmp=str.split(",");
		int[] tmp2=new int[tmp.length];
		for(int i=0;i<tmp.length;i++){
			tmp2[i]=Integer.parseInt(tmp[i]);
		}
		return tmp2;
	}
}
