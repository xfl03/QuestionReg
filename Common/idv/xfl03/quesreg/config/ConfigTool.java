package idv.xfl03.quesreg.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ConfigTool {
	private HashMap<String,String> config=new HashMap<String,String>();
	public ConfigTool(File configFile){
		FileReader fr=null;
		BufferedReader br=null;
		try {
			fr = new FileReader(configFile);
			br=new BufferedReader(fr);
			String s;
	        while((s=br.readLine())!=null){
	            if(!s.startsWith("#")&&!s.equalsIgnoreCase("")){
	            	String[] t=s.split("=");
	            	if(t.length==2){
	            		config.put(t[0], t[1]);
	            	}else if(t.length>2){
	            		config.put(t[0], s.replaceAll(t[0]+"=",""));
	            	}
	            }
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				fr.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public int getLength(){
		return config.size();
	}
	public int getIntConfig(String name,int defaultInt){
		if(config.get(name)==null)
			return defaultInt;
		try{
			return Integer.parseInt(config.get(name));
		}catch(Exception e){
			return defaultInt;
		}
	}
	public String getStringConfig(String name,String defaultString){
		if(config.get(name)==null)
			return defaultString;
		return config.get(name);
	}
	public boolean getBooleanConfig(String name,boolean defaultBoolean){
		if(config.get(name)==null)
			return defaultBoolean;
		return config.get(name).equalsIgnoreCase("true")||config.get(name).equalsIgnoreCase("1");
	}
}
