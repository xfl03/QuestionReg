package idv.xfl03.quesreg.listener;

public class PlayerListener {
	public EventSet onPlayerLogin(EventSet es){
		es.kickMessage="Hello World!";
		return es;
	}
}
