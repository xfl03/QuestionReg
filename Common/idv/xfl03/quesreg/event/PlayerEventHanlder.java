package idv.xfl03.quesreg.event;

import java.sql.ResultSet;

import idv.xfl03.quesreg.MainPool;

public class PlayerEventHanlder {
	private MainPool mainPool;
	public PlayerEventHanlder(MainPool mainPool) {
		this.mainPool=mainPool;
	}

	public EventRespondSet onPlayerLogin(EventRequestSet req){
		EventRespondSet res=new EventRespondSet();
		try {
			System.out.println((mainPool==null));
			ResultSet rs=mainPool.veriSQL.getUserResultsByUsername(req.username);
			if(!rs.next()){
				//User not exist
				res.kickMessage="User not exists! Please go to http://<Server IP>:"+mainPool.mainConfig.httpServerPort+" to register.";
				return res;
			}
			int status=rs.getInt("status");
			if(status==3){
				//banned
				res.kickMessage="You have been banned.";
				return res;
			}
			if(rs.getInt("admin")==1){
				return res;
			}
			if(status==0){
				//Not passed exam
				res.kickMessage="You haven't passed the exam! Please go to http://<Server IP>:"+mainPool.mainConfig.httpServerPort+" to join the exam.";
				return res;
			}
			if(status==1){
				//passed
				if(mainPool.mainConfig.needAdminVerify){
					res.kickMessage="You haven't been verified! Please ask admin to verify your account.";
				}
				return res;
			}
			if(status==2){
				//whitelist
				return res;
			}
			res.kickMessage="[ERROR] System ERROR.";
			return res;
			
		} catch (Exception e) {
			e.printStackTrace();
			res.kickMessage="[ERROR] Exception Occured.";
			return res;
		}
	}
}
