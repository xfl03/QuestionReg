package idv.xfl03.quesreg.bukkitapi;

import java.io.File;

import idv.xfl03.quesreg.MainPool;
import idv.xfl03.quesreg.bukkitapi.command.QRCommand;
import idv.xfl03.quesreg.bukkitapi.listener.PlayerListener;
import idv.xfl03.quesreg.config.MainConfig;
import idv.xfl03.quesreg.data.MainData;
import idv.xfl03.quesreg.database.VeriSQL;
import idv.xfl03.quesreg.hash.EncodeTool;
import idv.xfl03.quesreg.httpserver.HttpServerThread;
import idv.xfl03.quesreg.question.QuestionList;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class QuestionReg extends JavaPlugin {
    
    private PlayerListener playerListener = null;
    private HttpServerThread hst=null ;
    private Thread hstt=null;
	public static final String PLUGIN_NAME="QuestionReg";
	public static final String PLUGIN_VERSION="1.0.0";
	private MainPool mainPool=null;
    @Override
    public  void onEnable(){
        //Init Main Pool
        mainPool=new MainPool();
        
    	//Register PlayerListener
    	playerListener = new PlayerListener(mainPool);
    	PluginManager pm = getServer().getPluginManager();
        pm.registerEvents((Listener) playerListener, this);
        
        //Register Command
        getCommand("questionreg").setExecutor(new QRCommand(mainPool));
        
        //Init Main Data
        File dataFolder=new File("plugins/QuestionReg");
        mainPool.mainData=new MainData(dataFolder);
        
        //Init Main Config
        mainPool.mainConfig=new MainConfig(mainPool.mainData.mainConfigFile);
        
        //Init Verify DB
        mainPool.veriSQL=new VeriSQL(mainPool.mainData.databaseFolder);
        
        //Init Question List
        mainPool.questionList=new QuestionList(mainPool.mainData.questionFolder,mainPool.mainConfig);
        String ids=mainPool.questionList.getRandomQuestions();
        System.out.println("Random Questions: "+ids);
        int[] id=mainPool.questionList.getIds(ids);
        System.out.println("Question 1: "+id[0]);;
        System.out.println("ID 10001: "+mainPool.questionList.getQuestion(10001).question);
        
        //Http Server
        hst=new HttpServerThread(mainPool);
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
	@Override
    public void onDisable(){
		if(hstt!=null)
			hstt.stop();
		System.out.println("["+PLUGIN_NAME+"] QuestionMsg Disabled");
	}
	
}
