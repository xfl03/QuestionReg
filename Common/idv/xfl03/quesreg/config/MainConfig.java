package idv.xfl03.quesreg.config;

import java.io.File;

public class MainConfig {
	
	//Default
	public int passingScore=60;
	public int questionNumber1=5;
	public int questionNumber2=5;
	public int questionNumber3=0;
	public int questionNumber4=0;
	public int[] questionNumber;
	public boolean needAdminVerify=false;
	public String language="en";
	public int httpServerPort=1024;
	public int emailSupport=2;
	public int giveAdminPermission=0;
	
	public ConfigTool config=null;
	
	public MainConfig(File configFile){
		config=new ConfigTool(configFile);
		passingScore=config.getIntConfig("passing-score", passingScore);
		questionNumber1=config.getIntConfig("question-number-1", questionNumber1);
		questionNumber2=config.getIntConfig("question-number-2", questionNumber2);
		questionNumber3=config.getIntConfig("question-number-3", questionNumber3);
		questionNumber4=config.getIntConfig("question-number-4", questionNumber4);
		needAdminVerify=config.getBooleanConfig("", needAdminVerify);
		language=config.getStringConfig("language", language);
		httpServerPort=config.getIntConfig("http-server-port", httpServerPort);
		emailSupport=config.getIntConfig("email-support", emailSupport);
		giveAdminPermission=config.getIntConfig("give-admin-permission", giveAdminPermission);

		questionNumber=new int[5];
		questionNumber[1]=questionNumber1;
		questionNumber[2]=questionNumber2;
		questionNumber[3]=questionNumber3;
		questionNumber[4]=questionNumber4;
	}
	public String getEmailSupportString(){
		switch(emailSupport){
		case 0:
			return "email";
		case 1:
			return "qq";
		default:
		case 2:
			return "email/qq";
		}
	}
}
