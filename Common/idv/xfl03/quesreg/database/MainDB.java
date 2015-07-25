 package idv.xfl03.quesreg.database;

import idv.xfl03.quesreg.QuestionReg;

import java.io.File;
import java.sql.ResultSet;

public class MainDB {
	public SqliteTool st=null;
	//private static final int SQL_FILE_VERSION=1;
	public MainDB(File DatabaseFolder){
		File DBFile=new File(DatabaseFolder.getAbsolutePath()+"\\veri.db");
		boolean exist=DBFile.exists();
		st=new SqliteTool(DBFile.getAbsolutePath());
		try {
			st.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!exist){
			try {
				st.update("create table user("
						+ "username varchar(20), "
						+ "password varchar(32),"
						+ "token varchar(32),"
						+ "code varchar(32),"
						+ "email varchar(200), "
						+ "age int,"
						+ "regdate varchar(10),"
						+ "logdate varchar(10),"
						+ "status int,"//User Status ( 0-NotPassedExam 1-PassedExam 2-Whitelist 3-Banned)
						+ "veri int,"//If verified by admin ( 0-No 1-Yes )
						+ "admin int,"//If is admin ( 0-No 1-Yes )
						+ "regip varchar(15),"
						+ "logip varchar(15)"
						+ ");");
				st.update("create table score("
						+ "username varchar(20), "
						+ "score varchar(1024),"
						+ "ques varchar(1024),"
						+ "date varchar(10),"
						+ "time long"
						+ ");");
				st.update("create table version("
						+ "version varchar(100)"
						+ ");");
				
				//For test
				st.update("insert into user values("
						+ "'xfl03','593bbf1b91880577d26588095add4c72','3024da271ec6205fc3d364d87e8552bb','3024da271ec6205fc3d364d87e8552bb',"
						+ "'1552775831',16,'2015-04-26','2015-05-15',1,1,1,'127.0.0.1','127.0.0.1');");
				st.update("insert into score values("
						+ "'xfl03','100','5,1,2,4,3','2015-04-26',10000);");
				st.update("insert into version values('"+QuestionReg.PLUGIN_VERSION+"');");
				ResultSet rs = st.query("select * from user;");
		        while(rs.next()){
		        	System.out.print("username = " + rs.getString("username") + " "); 
		        	System.out.println("token = " + rs.getString("token")); 
		        } 
		        rs = st.query("select * from score;");
		        while(rs.next()){
		        	System.out.print("username = " + rs.getString("username") + " "); 
		        	System.out.println("score = " + rs.getString("score")); 
		        } 
			}catch(Exception e) {
				e.printStackTrace();
			}
		}else{
			
		}
	}
	
	public ResultSet getUserResultsByUsername(String username) throws Exception{
		return st.query("select * from user where username = '"+username+"' ;");
	}
	public ResultSet getUserResultsByToken(String token) throws Exception{
		return st.query("select * from user where token = '"+token+"' ;");
	}
	public ResultSet getUserResultsByCode(String code) throws Exception{
		return st.query("select * from user where code = '"+code+"' ;");
	}
	public ResultSet getUserResultsByCodeFront(String codeFront) throws Exception{
		return st.query("select * from user where code LIKE '"+codeFront+"%' ;");
	}
	public ResultSet getUserResultsByEmail(String email) throws Exception{
		return st.query("select * from user where email = '"+email+"' ;");
	}
	public ResultSet getUserResultsByAnyThing(String input) throws Exception{
		ResultSet rs=getUserResultsByUsername(input);
		if(rs.next()){
			return rs;
		}
		rs=getUserResultsByCode(input);
		if(rs.next()){
			return rs;
		}
		rs=getUserResultsByEmail(input);
		if(rs.next()){
			return rs;
		}
		return null;
	}
	
	
	
	public ResultSet getScoreResultsByUsername(String username) throws Exception{
		return st.query("select * from score where username = '"+username+"' ORDER BY time DESC;");
	}
	
}
