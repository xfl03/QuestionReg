package idv.xfl03.quesreg.bukkitapi.listener;

import idv.xfl03.quesreg.listener.EventSet;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
	
  	private idv.xfl03.quesreg.listener.PlayerListener pl=new idv.xfl03.quesreg.listener.PlayerListener();
  	
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        EventSet es=new EventSet();
        es.username=event.getPlayer().getName();
        es.ip=event.getPlayer().getAddress().getHostName();
        es=pl.onPlayerLogin(es);
        if(es.kickMessage!=null){
        	event.getPlayer().kickPlayer(es.kickMessage);
        }
    } 
    
}
