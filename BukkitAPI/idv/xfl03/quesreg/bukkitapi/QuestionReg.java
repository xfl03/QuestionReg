package idv.xfl03.quesreg.bukkitapi;

import idv.xfl03.quesreg.bukkitapi.command.QRCommandListener;
import idv.xfl03.quesreg.bukkitapi.event.PlayerEventListener;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class QuestionReg extends JavaPlugin {
    private idv.xfl03.quesreg.QuestionReg questionReg;
    
    @Override
    public void onEnable(){
        //Init Main
    	questionReg=new idv.xfl03.quesreg.QuestionReg();
    	questionReg.enable();
    	
    	//Register PlayerListener
        PlayerEventListener playerListener = new PlayerEventListener(questionReg.mainPool);
    	PluginManager pm = getServer().getPluginManager();
        pm.registerEvents((Listener) playerListener, this);
        
        //Register Command
        getCommand("questionreg").setExecutor(new QRCommandListener(questionReg.mainPool));
        
	}
    
	@Override
    public void onDisable(){
		questionReg.disable();
	}
	
}
