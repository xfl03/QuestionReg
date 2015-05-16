package idv.xfl03.quesreg.command;

import java.sql.ResultSet;
import java.util.Calendar;

import idv.xfl03.quesreg.MainPool;
import idv.xfl03.quesreg.hash.EncodeTool;

public class QRCommand {
	private MainPool mainPool;
	public QRCommand(MainPool mainPool) {
		this.mainPool=mainPool;
	}

	public CommandSet onCommand(CommandSet cs){
		if(cs.sender!=CommandSet.CONSOLE){
			//Verify permission
			try {
				ResultSet rs=mainPool.veriSQL.getUserResultsByUsername(cs.sender);
				if(!rs.next()||rs.getInt("admin")!=1){
					cs.attendReturnText("You aren't QuestionReg admin. (Maybe you are OP.)");
					return cs;
				}
			} catch (Exception e) {
				cs.attendReturnText("[ERROR] SQL Exception.");
				return cs;
			}
		}
		//If have permission
		cs.returnCode=true;
		if(cs.args.length==0||cs.args[0].equalsIgnoreCase("help")){
			//List Help list
			cs.attendReturnText("==QuestionReg Help==");
			cs.attendSplitLineToReturnText();
			
			cs.attendReturnText("Register a new user");
			cs.attendReturnText("/qr reg <username> [optional-password] [optional-email] [optional-age]");
			cs.attendBlankLineToReturnText();
			
			cs.attendReturnText("Verify a user(User must have been created)");
			cs.attendReturnText("/qr verify <username>");
			cs.attendBlankLineToReturnText();

			cs.attendReturnText("Set Admin (User must have been created)");
			cs.attendReturnText("/qr admin <username>");
			cs.attendBlankLineToReturnText();
			
			cs.attendReturnText("Whitelist a user(User can have not been created)");
			cs.attendReturnText("/qr whitelist <username>");
			cs.attendBlankLineToReturnText();
			return cs;
		}
		if(cs.args[0].equalsIgnoreCase("reg")){
			if(cs.args.length==1){
				cs.attendReturnText("Register a new user");
				cs.attendReturnText("/qr reg <username> [optional-password] [optional-email] [optional-age]");
				cs.attendBlankLineToReturnText();
				return cs;
			}
			String username=cs.args[1];
			try {
				ResultSet rs=mainPool.veriSQL.getUserResultsByUsername(username);
				if(rs.next()){
					cs.attendReturnText("User Exsits!");
					return cs;
				}
			} catch (Exception e1) {
				cs.attendReturnText("[ERROR] "+e1.getMessage());
				return cs;
			}
			
			boolean displayPassword=false;
			String password="";
			String email="";
			int age=0;
			switch(cs.args.length){
			case 5:
				try{
					age=Integer.parseInt(cs.args[3]);
				}catch(Exception e){
					cs.attendReturnText("[ERROR] 'arg' must be int");
				}
			case 4:
				email=cs.args[3];
			case 3:
				password=cs.args[2];
				break;
				
			case 2:
			default:
				displayPassword=true;
				password=((int)(Math.random()*1000000))+"";
				break;
			}
			
			Calendar now = Calendar.getInstance();  
			try {
				mainPool.veriSQL.st.update("insert into user values"
						+ "('"+username+"','"+EncodeTool.basicEncode(password)+"',"
						+ "'','"+email+"',"+age+",'"
						+ now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)+"','',"
						+ "0,0,0,'','');");
				cs.attendReturnText("Success!");
				if(displayPassword){
					cs.attendReturnText("Default Password: "+password);
				}
				return cs;
			} catch (Exception e) {
				cs.attendReturnText("[ERROR] "+e.getMessage());
				return cs;
			}
		}
		cs.attendReturnText("Unsupported argument! Use /qr help");
		return cs;
	}
}
