package idv.xfl03.quesreg.httpserver;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import idv.xfl03.quesreg.MainPool;
import idv.xfl03.quesreg.hash.EncodeTool;

public class APIHandler {
	private MainPool mainPool;
	private Map<String, List<String>> uriAttributes;
	private String target;
	public String token;
	private String clientIP;
	public APIHandler(MainPool mainPool,Map<String, List<String>> uriAttributes,String target,String token,String clientIP){
		this.mainPool=mainPool;
		this.uriAttributes=uriAttributes;
		this.target=target;
		this.token=token;
		this.clientIP=clientIP;
	}
	public String handle(){
		if(target.startsWith("login")){
        	return login();
        }
		if(target.startsWith("userexist")){
        	return userexist();
        }
		if(target.startsWith("emailsupport")){
        	return emailsupport();
        }
		if(target.startsWith("codesupport")){
        	return codesupport();
        }
		if(target.startsWith("changepw")){
        	return changepw();
        }
        if(target.startsWith("reg")){
        	return reg();
        }
		return "API Not Found.";
	}
	
	public String login(){
		if(!token.equalsIgnoreCase("")){
    		try {
				ResultSet rs0= mainPool.mainDB.getUserResultsByToken(token);
				if(!rs0.next()){
					token="";
					return "Bad Token. [Not Find]";
				}
				if(rs0.getString("logip").equalsIgnoreCase(clientIP)){
					return "Have logined.";
				}
				token="";
				return "Bad Token. [IP Changed] ";
			} catch (Exception e) {
				token="";
				return "Bad Token. [Exception]";
			}
    	}
		List<String> username=uriAttributes.get("username");
		List<String> password=uriAttributes.get("password");
		if(username==null||password==null){
			return "Not enough args.";
		}
		try {
			ResultSet rs= mainPool.mainDB.getUserResultsByUsername(username.get(0));
			ResultSetMetaData   rsmd1 = rs.getMetaData();
			int count = rsmd1.getColumnCount();
			if(count==0){
				return "Not exist user.";
			}
			if(rs.getString("password").equalsIgnoreCase(EncodeTool.encodeByMD5(password.get(0)))){
				Calendar now = Calendar.getInstance();
				token=EncodeTool.encodeByMD5("TOKEN-"+System.currentTimeMillis()+Math.random());
				String querySQL="update user SET token='"+token+"',logip='"+clientIP+"',logdate= '"
					+ now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)+"'"
						+ "WHERE username='"+username.get(0)+"'";
				mainPool.mainDB.st.update(querySQL);
				return "Success.";
			}
			return "Wrong Password.";
		} catch (Exception e) {
				return "SQL ERROR.";
		}
	}
	public String userexist(){
		List<String> username=uriAttributes.get("username");
		if(username==null||username.size()<1){
			return "Not enough args.";
		}
		try {
			ResultSet rs=mainPool.mainDB.getUserResultsByUsername(username.get(0));
			return booleanToInt(rs.next())+"";
		} catch (Exception e) {
			return "SQL ERROR."+e.getMessage();
		}
	}
	public String emailsupport() {
		return mainPool.mainConfig.emailSupport+"";
	}
	public String codesupport() {
		return booleanToInt(mainPool.mainConfig.useCode)+"";
	}
	public String changepw(){
		if(token.equalsIgnoreCase("")){
			return "No Token.";
		}
		try {
			ResultSet rs=mainPool.mainDB.getUserResultsByToken(token);
			if(!rs.next()){
				token="";
				return "Bad Token. [Not Find]";
			}
			List<String> oldPW=uriAttributes.get("old");
			List<String> newPW=uriAttributes.get("new");
			if(oldPW==null||oldPW.isEmpty()||newPW==null||newPW.isEmpty()){
				return "Not enough args.";
			}
			if(!oldPW.get(0).equalsIgnoreCase(rs.getString("password"))){
				return "Wrong Password!";
			}
			if(!oldPW.get(0).matches("^\\w{32}$")||!newPW.get(0).matches("^\\w{32}$")){
				return "Password wrong pattern.";
			}
			mainPool.mainDB.st.update(
					"UPDATE user SET password='"+EncodeTool.encodeByMD5(newPW.get(0))+"' "
							+ "WHERE token='"+token+"'");
		} catch (Exception e) {
			return "SQL ERROR."+e.getMessage();
		}
		return "";
	}
	public String reg(){
		if(!token.equalsIgnoreCase("")){
    		try {
				ResultSet rs0= mainPool.mainDB.getUserResultsByToken(token);
				if(!rs0.next()){
					token="";
					return "Bad Token. [Not Find]";
				}
				if(rs0.getString("logip").equalsIgnoreCase(clientIP)){
					return "Have logined.";
				}
				token="";
				return "Bad Token. [IP Changed] ";
			} catch (Exception e) {
				token="";
				return"Bad Token. [Exception]";
			}
		}
		List<String> username=uriAttributes.get("username");
		List<String> password=uriAttributes.get("password");
		List<String> email=uriAttributes.get("email");
		List<String> age=uriAttributes.get("age");
		if(username==null||password==null||email==null||age==null){
			return "Not enough args.";
		}
		try {
			if(!username.get(0).matches("^[a-zA-Z]\\w{2,19}$")){
				return "Username wrong pattern.";
			}
    			
			if(!email.get(0).matches("^\\d{5,13}$")
					&&!email.get(0).matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")){
				return "Email wrong pattern.";
			}
    			
			if(!password.get(0).matches("^\\w{32}$")
					||!age.get(0).matches("^\\d{1,2}$")){
				return "Password / Age wrong pattern.";
			}
    			
			ResultSet rs;
    			
			rs= mainPool.mainDB.getUserResultsByUsername(username.get(0));
			if(rs.next())
				return "USER EXIST.";
				
			rs= mainPool.mainDB.getUserResultsByEmail(email.get(0));
			if(rs.next())
				return "EMAIL EXIST.";
    			
			Calendar now = Calendar.getInstance();  
			String code=EncodeTool.encodeByMD5(username.get(0)+"CODE"+((int)(Math.random()*1000000000))+"CODE"+password.get(0));
			token=EncodeTool.encodeByMD5("TOKEN-"+System.currentTimeMillis()+Math.random()*100);
			mainPool.mainDB.st.update("insert into user values"
					+ "('"+username.get(0)+"','"+EncodeTool.encodeByMD5(password.get(0))+"',"
					+ "'"+token+"','"+code+"','"+email.get(0)+"',"+age.get(0)+",'"
					+ now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)+"','"
					+ now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)+"',"
					+ "0,0,0,'"+clientIP+"','"+clientIP+"');");
			return "Success.";
		} catch (Exception e) {
			return "SQL ERROR."+e.getMessage();
		}
	}
	public String userinfo(){
		//TODO finish it!!
		return "Not finish.";
	}
	
	private int booleanToInt(boolean b){
		return b?1:0;
	}
}
