package idv.xfl03.quesreg.httpserver;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
		if(target.startsWith("userinfoa")){
        	return userinfo_admin();
        }
        if(target.startsWith("userinfo")){
        	return userinfo();
        }
        if(target.startsWith("userpasta")){
        	return userpast_admin();
        }
        if(target.startsWith("userpast")){
        	return userpast();
        }
        if(target.startsWith("userques")){
        	return userques();
        }
        if(target.startsWith("question")){
        	return question();
        }
        if(target.startsWith("answer")){
        	return answer();
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
					return "Logined.";
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
				return "User does not exist.";
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
			return "Illegal Token.";
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
					return "Logined.";
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
				return "USER DOES EXIST.";
				
			rs= mainPool.mainDB.getUserResultsByEmail(email.get(0));
			if(rs.next())
				return "EMAIL DOES EXIST.";
    			
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
		if(token == null){
			return "Bad Request";
		}
		ResultSet rs;
		try {
			rs = mainPool.mainDB.getUserResultsByToken(token);
			return getUserInfo(rs);
		} catch (Exception e) {
			return "SQL ERROR."+e.getMessage();
		}
	}
	//anyone can check if I wrote anything wrong below?   -Lucas
	public String userinfo_admin(){
		if(token==null){
			return "Bad Request";
		}
		try {
			ResultSet rs0=mainPool.mainDB.getUserResultsByToken(token);
			if(rs0==null){
				return "Bad Request";
			}
			if(rs0.getInt("admin")!=1){
				return "Not Admin";
			}
		} catch (Exception e1) {
			return "SQL ERROR."+e1.getMessage();
		}
		
		List<String> anything = uriAttributes.get("input");
		if(!anything.isEmpty()){
			ResultSet rs;
			try {
				rs = mainPool.mainDB.getUserResultsByAnyThing(anything.get(0));
				return getUserInfo(rs);
			} catch (Exception e) {
				return "SQL ERROR."+e.getMessage();
			}
		}
		return "No such user.";
	}
	public String userpast(){
		if(token==null){
			return "Bad Request";
		}
		ResultSet rs1;
		try {
			rs1=mainPool.mainDB.getUserResultsByToken(token);
			return getUserPast(rs1);
		}
		catch(Exception e){
			return "SQL ERROR."+e.getMessage();
		}
	}
	public String userpast_admin(){
		if(token==null){
			return "Bad Request";
		}
		try {
			ResultSet rs0=mainPool.mainDB.getUserResultsByToken(token);
			if(!rs0.next()){
				return "Bad Request";
			}
			if(rs0.getInt("admin")!=1){
				return "Not Admin";
			}
		} catch (Exception e1) {
			return "SQL ERROR."+e1.getMessage();
		}
		List<String> username = uriAttributes.get("username");
		if(!username.isEmpty()){
			ResultSet rs;
			try {
				rs = mainPool.mainDB.getUserResultsByUsername(username.get(0));
				return getUserPast(rs);
			} catch (Exception e) {
				e.printStackTrace();
				return "SQL ERROR."+e.getMessage();
			}
		}
		return "No such user.";
	}
	public String userques(){
		ResultSet rs;
		try {
			rs=mainPool.mainDB.getUserResultsByToken(token);
			if(!rs.next()){
				return "Bad Request";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "SQL ERROR."+e.getMessage();
		}
		int total = 0;
		for(int a:mainPool.mainConfig.questionNumber){
			total+=a;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(total);
		sb.append(",");
		String s=mainPool.questionList.getRandomQuestions();
		sb.append(s);
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			mainPool.mainDB.st.update("INSERT into score values('"+rs.getString("username")+"','-1','"+s+"','"+dateFormat.format(new Date())+"',"+System.currentTimeMillis()+",);");
		} catch (Exception e) {
			e.printStackTrace();
			return "SQL ERROR."+e.getMessage();
		}
		return sb.toString();
	}
	public String question(){
		if(token==null){
			return "Bad Request";
		}
		List<String> q = uriAttributes.get("q");
		String que=q.get(0);
		return mainPool.questionList.getQuestion(que);
	}
	public String answer(){
		if(token==null){
			return "Bad Request";
		}
		ResultSet rs,rs2;
		String username;
		try {
			rs=mainPool.mainDB.getUserResultsByToken(token);
			if(!rs.next()){
				return "Bad Request";
			}
			username=rs.getString("username");
			rs2=mainPool.mainDB.getScoreResultsByUsername(username);
			if(!rs2.next()){
				return "No Question Found";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "SQL ERROR."+e.getMessage();
		}
		List<String> a = uriAttributes.get("a");
		String que=a.get(0);
		String scores;
		boolean pass;
		try {
			scores = mainPool.questionList.getScore(rs2.getString("ques"), que);
			pass=mainPool.questionList.isPass(scores);
			if(pass){
				mainPool.mainDB.st.update("UPDATE user SET status='1' WHERE username='"+username+"';");
			}
			mainPool.mainDB.st.update("UPDATE score SET score='"+scores+"' WHERE username='"+username+"' AND time="+rs2.getLong("time")+" ;");
		} catch(Exception e) {
			return "SQL ERROR."+e.getMessage();
		}
		
		return scores+","+booleanToInt(pass);
	}
	
	//Tools
	private String getUserPast(ResultSet rs1){
		try {
			if(!rs1.next()){
				return "Bad Request";
			}
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM score WHERE username=");
			sb.append("'");
			sb.append(rs1.getString("username"));
			sb.append("'");
			ResultSet rs = mainPool.mainDB.st.query(sb.toString());
			int times=0;
			StringBuilder temp0=new StringBuilder();
			while(rs.next()){
				times++;
				temp0.append(",");
				temp0.append(rs.getString("score").replaceAll(",", "."));
			}
			
			return times+","+(mainPool.mainConfig.examTimes-times)+temp0.toString();
		}
		catch(Exception e){
			return "SQL ERROR."+e.getMessage();
		}
	}
	private String getUserInfo(ResultSet rs){
		try {
			if(!rs.next()){
				return "Bad Request";
			}
			StringBuilder sb = new StringBuilder();
			sb.append(rs.getString("username"));
			sb.append(",");
			sb.append(rs.getString("email"));
			sb.append(",");
			sb.append(rs.getInt("age"));
			sb.append(",");
			sb.append(rs.getString("regdate"));
			sb.append(",");
			sb.append(rs.getInt("veri"));
			sb.append(",");
			sb.append(rs.getInt("admin"));
			sb.append(",");
			sb.append(rs.getString("logdate"));
			sb.append(",");
			sb.append(rs.getString("logip"));
			sb.append(",");
			sb.append(rs.getString("code"));
			sb.append(",");
			sb.append(rs.getString("status"));
			return sb.toString();
		} catch (Exception e) {
			return "SQL ERROR."+e.getMessage();
		}
	}
	
	private int booleanToInt(boolean b){
		return b?1:0;
	}
}