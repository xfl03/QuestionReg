package idv.xfl03.quesreg.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MajorConfig {
	
	public File dataFolder=null;
	public File questionFolder=null;
	public File databaseFolder=null;
	public File majorConfigFile=null;
	public File webFolder=null;
	public File languageFolder=null;
	public TxtConfig txtConfig=null;
	
	public MajorConfig(File DataFolder){
		dataFolder=DataFolder;
		majorConfigFile=getSubFile(dataFolder,"config.txt");
    	questionFolder=getSubFile(dataFolder,"question");
    	databaseFolder=getSubFile(dataFolder,"database");
    	webFolder=getSubFile(dataFolder,"web");
    	languageFolder=getSubFile(dataFolder,"language");
    	
    	//First Time To Load Plugin
        if(!dataFolder.exists()){
        	System.out.println("Cannot Find Data Folder!");
        	this.initDataFolder();
        }
        
        //For someone who deleted some file/folder
        if(!majorConfigFile.exists()){
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
        
        txtConfig=new TxtConfig(majorConfigFile);
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
			majorConfigFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	if(majorConfigFile.exists()){
    		System.out.println("Successfully Created "+majorConfigFile.getAbsolutePath());
    	}else{
    		System.out.println("Cannot Create "+majorConfigFile.getAbsolutePath());
    		System.out.println("Cannot Load QuestionMsg, please check permission!");
    		return;
    	}
    	
    	resourceFileCopy("config.txt",majorConfigFile);
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
	
	private File getSubFile(File parentFile,String subName){
		return new File(parentFile.getAbsolutePath()+"\\"+subName);
	}
	
	private InputStream getResourceFileStream(String fileName){
		return this.getClass().getClassLoader().getResourceAsStream(fileName);
	}
	
	private void resourceFileCopy(String name, File to) {
		InputStream is = null;
		BufferedInputStream bis=null;
		BufferedOutputStream bos=null;
		try {
			is=getResourceFileStream(name);
			bis =new BufferedInputStream(is);
			bos=new BufferedOutputStream(new FileOutputStream(to));
			
	        byte[] b=new byte[2048];
	        int i=0;
	        while((i=bis.read(b))!=-1){
	        	bos.write(b, 0, i);
	        }
	        bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				is.close();
				bis.close();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
}
