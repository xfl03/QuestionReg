package idv.xfl03.quesreg.config;

import java.io.File;

public class TxtConfig {
	
	//Default
	public int passingScore=60;
	public int questionNumber=5;
	public boolean needAdminVerify=false;
	public String language="en";
	public int httpServerPort=1024;
	
	public ConfigTool config=null;
	
	public TxtConfig(File configFile){
		config=new ConfigTool(configFile);
		passingScore=config.getIntConfig("passing-score", passingScore);
		questionNumber=config.getIntConfig("question-number", questionNumber);
		needAdminVerify=config.getBooleanConfig("", needAdminVerify);
		language=config.getStringConfig("language", language);
		httpServerPort=config.getIntConfig("http-server-port", httpServerPort);
		System.out.println(language);
	}
}
