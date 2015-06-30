package idv.xfl03.quesreg.data;

import java.io.File;
import java.io.IOException;

public class MainData {
	
	public File dataFolder=null;
	public File questionFolder=null;
	public File databaseFolder=null;
	public File mainConfigFile=null;
	public File webFolder=null;
	public File languageFolder=null;
	public File logFile=null;
	public ResourceFileTool rft;
	
	public MainData(File DataFolder){
		rft=new ResourceFileTool();
		dataFolder=DataFolder;
		mainConfigFile=getSubFile(dataFolder,"config.txt");
    	questionFolder=getSubFile(dataFolder,"question");
    	databaseFolder=getSubFile(dataFolder,"database");
    	webFolder=getSubFile(dataFolder,"web");
    	languageFolder=getSubFile(dataFolder,"language");
    	logFile=getSubFile(dataFolder,"QuestionReg.log");
    	
    	//First Time To Load Plugin
        if(!dataFolder.exists()){
        	System.out.println("Cannot Find Data Folder!");
        	this.initDataFolder();
        }
        
        //For someone who deleted some file/folder
        if(!mainConfigFile.exists()){
        	System.out.println("Cannot Find Config File!");
        	this.initConfigFile();
        }
        if(!questionFolder.exists()){
        	System.out.println("Cannot Find Question Folder!");
        	this.initQuestionFolder();
        }
        if(!databaseFolder.exists()){
        	System.out.println("Cannot Find DataBase Folder!");
        	this.initDatabaseFolder();
        }
        if(!webFolder.exists()){
        	System.out.println("Cannot Find Web Folder!");
        	this.initWebFolder();
        }
        if(!languageFolder.exists()){
        	System.out.println("Cannot Find Language Folder!");
        	this.initLanguageFolder();
        }
        
	}
	
	private void initDataFolder(){
		// \plugins\QuestionReg
		dataFolder.mkdirs();
    	if(dataFolder.exists()){
    		System.out.println("Successfully Created "+dataFolder.getAbsolutePath());
    	}else{
    		System.out.println("Cannot Create "+dataFolder.getAbsolutePath());
    		System.out.println("Cannot Load QuestionMsg, please check permission!");
    		return;
    	}
    	
    	this.initConfigFile();
    	this.initQuestionFolder();
    	this.initDatabaseFolder();
    	this.initWebFolder();
    	this.initLanguageFolder();
	}
	
	private void initConfigFile(){
		// \plugins\QuestionReg\config.txt
    	try {
			mainConfigFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	if(mainConfigFile.exists()){
    		System.out.println("Successfully Created "+mainConfigFile.getAbsolutePath());
    	}else{
    		System.out.println("Cannot Create "+mainConfigFile.getAbsolutePath());
    		System.out.println("Cannot Load QuestionMsg, please check permission!");
    		return;
    	}
    	
    	rft.resourceFileCopy("/resource/config.txt",mainConfigFile);
	}
	
	private void initQuestionFolder(){
		// \plugins\QuestionReg\question
    	questionFolder.mkdirs();
    	if(questionFolder.exists()){
    		System.out.println("Successfully Created "+questionFolder.getAbsolutePath());
    	}else{
    		System.out.println("Cannot Create "+questionFolder.getAbsolutePath());
    		System.out.println("Cannot Load QuestionMsg, please check permission!");
    		return;
    	}
	}
	
	private void initDatabaseFolder(){
		// \plugins\QuestionReg\database
    	databaseFolder.mkdirs();
    	if(databaseFolder.exists()){
    		System.out.println("Successfully Created "+databaseFolder.getAbsolutePath());
    	}else{
    		System.out.println("Cannot Create "+databaseFolder.getAbsolutePath());
    		System.out.println("Cannot Load QuestionMsg, please check permission!");
    		return;
    	}
	}
	
	private void initWebFolder(){
		// \plugins\QuestionReg\web
    	webFolder.mkdirs();
    	if(webFolder.exists()){
    		System.out.println("Successfully Created "+webFolder.getAbsolutePath());
    	}else{
    		System.out.println("Cannot Create "+webFolder.getAbsolutePath());
    		System.out.println("Cannot Load QuestionMsg, please check permission!");
    		return;
    	}
	}
	
	private void initLanguageFolder(){
		// \plugins\QuestionReg\language
    	languageFolder.mkdirs();
    	if(languageFolder.exists()){
    		System.out.println("Successfully Created "+languageFolder.getAbsolutePath());
    	}else{
    		System.out.println("Cannot Create "+languageFolder.getAbsolutePath());
    		System.out.println("Cannot Load QuestionMsg, please check permission!");
    		return;
    	}
	}
	
	public File getSubFile(File parentFile,String subName){
		//return new File(parentFile.getAbsolutePath()+"\\"+subName);
		return new File(parentFile,subName);
	}
	
}
