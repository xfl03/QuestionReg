package idv.xfl03.quesreg.listener;

import java.sql.ResultSet;

import idv.xfl03.quesreg.MainPool;

public class PlayerListener {
	private MainPool mainPool;
	public PlayerListener(MainPool mainPool) {
		this.mainPool=mainPool;
	}

	public EventSet onPlayerLogin(EventSet es){
		try {
			System.out.println((mainPool==null));
			ResultSet rs=mainPool.veriSQL.getUserResultsByUsername(es.username);
			if(!rs.next()){
				//User not exist
				es.kickMessage="User not exists! Please go to http://<Server IP>:"+mainPool.mainConfig.httpServerPort+" to register.";
				return es;
			}
			int status=rs.getInt("status");
			if(status==3){
				//banned
				es.kickMessage="You have been banned.";
				return es;
			}
			if(rs.getInt("admin")==1){
				return es;
			}
			if(status==0){
				//Not passed exam
				es.kickMessage="You haven't passed the exam! Please go to http://<Server IP>:"+mainPool.mainConfig.httpServerPort+" to join the exam.";
				return es;
			}
			if(status==1){
				//passed
				if(mainPool.mainConfig.needAdminVerify){
					es.kickMessage="You haven't been verified! Please ask admin to verify your account.";
				}
				return es;
			}
			if(status==2){
				//whitelist
				return es;
			}
			es.kickMessage="[ERROR] System ERROR.";
			return es;
			
		} catch (Exception e) {
			e.printStackTrace();
			es.kickMessage="[ERROR] Exception Occured.";
			return es;
		}
	}
}
