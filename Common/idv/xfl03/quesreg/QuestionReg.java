package idv.xfl03.quesreg;

import java.io.File;

import idv.xfl03.quesreg.MainPool;
import idv.xfl03.quesreg.config.MainConfig;
import idv.xfl03.quesreg.data.MainData;
import idv.xfl03.quesreg.database.VeriSQL;
import idv.xfl03.quesreg.hash.EncodeTool;
import idv.xfl03.quesreg.httpserver.HttpServerThread;
import idv.xfl03.quesreg.question.QuestionList;

public class QuestionReg{
    
    public Thread hstt=null;
	public static final String PLUGIN_NAME="QuestionReg";
	public static final String PLUGIN_VERSION="1.0.0";
	public MainPool mainPool=null;
	
    public void enable(){
        //Init Main Pool
        mainPool=new MainPool();
        
        //Init Main Data
        File dataFolder=new File("plugins/QuestionReg");
        mainPool.mainData=new MainData(dataFolder);
        
        //Init Main Config
        mainPool.mainConfig=new MainConfig(mainPool.mainData.mainConfigFile);
        
        //Init Verify DB
        mainPool.veriSQL=new VeriSQL(mainPool.mainData.databaseFolder);
        
        //Init Question List
        mainPool.questionList=new QuestionList(mainPool.mainData.questionFolder,mainPool.mainConfig,mainPool.mainData);
        String ids=mainPool.questionList.getRandomQuestions();
        System.out.println("Random Questions: "+ids);
        int[] id=mainPool.questionList.getIds(ids);
        System.out.println("Question 1: "+id[0]);;
        System.out.println("ID 10001: "+mainPool.questionList.getQuestion(10001).question);
        
        //Http Server
        HttpServerThread hst=new HttpServerThread(mainPool);
        hstt=new Thread(hst);
        hstt.setName("QuestionReg Http Server Thread");
        hstt.start();
		
		//MD5 Test
		try {
			System.out.println("[MD5-TEST] xfl03 ---> "+EncodeTool.encodeByMD5("xfl03"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        System.out.println("["+PLUGIN_NAME+"] QuestionMsg Enabled");
	}
    
	@SuppressWarnings("deprecation")
    public void disable(){
		if(hstt!=null)
			hstt.stop();
		System.out.println("["+PLUGIN_NAME+"] QuestionMsg Disabled");
	}
	
}
