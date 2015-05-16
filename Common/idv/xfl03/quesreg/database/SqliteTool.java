package idv.xfl03.quesreg.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Use JDBC To Use SQLite.
 *
 * @author xfl03 [2015.2 2015.4]
 *
 */
public class SqliteTool {
	private String DBFileName="";
	
	private Connection conn=null;
	
	private Statement stat=null;
	
	public SqliteTool(String DataBaseFileName){
		File DBFile=new File(DataBaseFileName);
		File DBFolder=new File(DBFile.getParent());
		
		if(!DBFolder.exists()){
			DBFolder.mkdirs();
		}
		if(DataBaseFileName==null||DataBaseFileName.equalsIgnoreCase("")){
			throw new IllegalArgumentException("DataBaseFileName Cannot Be Empty");
		}else{
			this.DBFileName=DataBaseFileName;
		}
	}
	
	public void connect() throws Exception{
		Class.forName ( "org.sqlite.JDBC" ); 
		conn = DriverManager.getConnection ( "jdbc:sqlite:" + DBFileName); 
		if(conn==null){
			throw new Exception("Connection Not Start");
		}else{
		stat = conn.createStatement();
		}
	}
	
	public Connection getConnection() throws Exception{
		if(conn!=null){
			return conn;
		}else{
			throw new Exception("Connection NULL");
		}
	}
	
	public Statement getStatement() throws Exception{
		if(stat!=null){
			return stat;
		}else{
			throw new Exception("Statement NULL");
		}
	}
	
	public void update(String updateSQL) throws Exception{
		if(updateSQL==null||updateSQL.equalsIgnoreCase("")){
			throw new IllegalArgumentException("Query SQL Cannot Be Empty");
		}else{
			stat.executeUpdate(updateSQL);
		}
	}
	
	public ResultSet query(String querySQL) throws Exception{
		if(querySQL==null||querySQL.equalsIgnoreCase("")){
			throw new IllegalArgumentException("Query SQL Cannot Be Empty");
		}else{
			return stat.executeQuery(querySQL);
		}
	}
	
	public void disconnect() throws Exception{
		if(conn==null){
			throw new Exception("Connection NULL");
		}else if(stat==null){
			throw new Exception("Statement NULL");
		}else{
		conn.close();
		stat.close();
		}
	}
	
}