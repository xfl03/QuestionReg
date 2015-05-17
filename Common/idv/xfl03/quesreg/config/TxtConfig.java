package idv.xfl03.quesreg.config;

import java.io.File;

public class TxtConfig {
	
	//Default
	public int passingScore=60;
	public int questionNumber=5;
	public boolean needAdminVerify=false;
	public String language="en";
	public int httpServerPort=1024;
	public int emailSupport=2;
	public int giveAdminPermission=0;
	
	public ConfigTool config=null;
	
	public TxtConfig(File configFile){
		config=new ConfigTool(configFile);
		passingScore=config.getIntConfig("passing-score", passingScore);
		questionNumber=config.getIntConfig("question-number", questionNumber);
		needAdminVerify=config.getBooleanConfig("", needAdminVerify);
		language=config.getStringConfig("language", language);
		httpServerPort=config.getIntConfig("http-server-port", httpServerPort);
		emailSupport=config.getIntConfig("email-support", emailSupport);
		giveAdminPermission=config.getIntConfig("give-admin-permission", giveAdminPermission);
		//System.out.println(language);
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
