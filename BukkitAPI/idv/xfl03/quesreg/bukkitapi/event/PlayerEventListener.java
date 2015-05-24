package idv.xfl03.quesreg.bukkitapi.event;

import idv.xfl03.quesreg.MainPool;
import idv.xfl03.quesreg.event.EventRequestSet;
import idv.xfl03.quesreg.event.EventRespondSet;
import idv.xfl03.quesreg.event.PlayerEventHanlder;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEventListener implements Listener {
	
  	private PlayerEventHanlder peh;
  	
    public PlayerEventListener(MainPool mainPool) {
		peh=new PlayerEventHanlder(mainPool);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        EventRequestSet req=new EventRequestSet();
        req.username=event.getPlayer().getName();
        req.ip=event.getPlayer().getAddress().getHostName();
        EventRespondSet res=peh.onPlayerLogin(req);
        
        if(res.kickMessage!=null){
        	event.getPlayer().kickPlayer(res.kickMessage);
        }
    } 
    
}
