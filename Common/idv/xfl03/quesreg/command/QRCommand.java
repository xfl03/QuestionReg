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
					cs.attendReturnText("No permission!");
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
			cs.attendReturnText("/qr reg <username> [optional-password (Also '*' for random)] [optional-email] [optional-age]");
			cs.attendBlankLineToReturnText();
			
			cs.attendReturnText("Verify a user(User must have been created)");
			cs.attendReturnText("/qr verify <username/code/"+mainPool.mainConfig.getEmailSupportString()+">");
			cs.attendBlankLineToReturnText();

			cs.attendReturnText("Set Admin (User must have been created)");
			cs.attendReturnText("/qr admin <username>");
			cs.attendBlankLineToReturnText();
			
			cs.attendReturnText("Remove off Admin (User must have been created)");
			cs.attendReturnText("/qr unadmin <username>");
			cs.attendBlankLineToReturnText();
			
			cs.attendReturnText("Whitelist a user(User can have not been created)");
			cs.attendReturnText("/qr whitelist <username>");
			cs.attendBlankLineToReturnText();
			return cs;
		}
		if(cs.args[0].equalsIgnoreCase("reg")){
			if(cs.args.length==1){
				cs.attendReturnText("Register a new user");
				cs.attendReturnText("/qr reg <username> [optional-password (Also '*' for random)] [optional-email] [optional-age]");
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
			
			boolean displayPassword=true;
			String password=((int)(Math.random()*900000+100000))+"";//Random six number for password
			String email="";
			int age=0;
			switch(cs.args.length){
			default:
			case 5:
				try{
					age=Integer.parseInt(cs.args[3]);
				}catch(Exception e){
					cs.attendReturnText("[ERROR] 'age' must be int");
				}
			case 4:
				email=cs.args[3];
			case 3:
				password=cs.args[2];
				displayPassword=false;
				break;
			case 2:
				break;
			}
			
			if(!email.equalsIgnoreCase("")){
				try {
					ResultSet rs1=mainPool.veriSQL.getUserResultsByEmail(email);
					if(rs1.next()){
						cs.attendReturnText("Email Exsits!");
						return cs;
					}
				} catch (Exception e) {
					cs.attendReturnText("[ERROR] "+e.getMessage());
					return cs;
				}
				
			}
			
			try {
				Calendar now = Calendar.getInstance();
				String code=EncodeTool.encodeByMD5(username+"CODE"+((int)(Math.random()*1000000000))+"CODE"+password);
				mainPool.veriSQL.st.update("insert into user values"
						+ "('"+username+"','"+EncodeTool.basicEncode(password)+"',"
						+ "'','"+code+"','"+email+"',"+age+",'"
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
		if(cs.args[0].equalsIgnoreCase("verify")){
			if(cs.args.length==1){
				cs.attendReturnText("Verify a user(User must have been created)");
				cs.attendReturnText("/qr verify <username/code/"+mainPool.mainConfig.getEmailSupportString()+">");
				return cs;
			}
			String input=cs.args[1];
			try {
				ResultSet rs=mainPool.veriSQL.getUserResultsByAnyThing(input);
				if(rs==null){
					cs.attendReturnText("User Not Found!");
					return cs;
				}
				int veri=rs.getInt("veri");
				String username=rs.getString("username");
				if(veri==1){
					cs.attendReturnText(username+" has already been verified!");
					return cs;
				}
				mainPool.veriSQL.st.update("UPDATE user SET veri=1 WHERE username='"+username+"';");
				cs.attendReturnText("Success! " +username+" has been verified!");
				return cs;
			} catch (Exception e) {
				cs.attendReturnText("[ERROR] "+e.getMessage());
				return cs;
			}
		}
		if(cs.args[0].equalsIgnoreCase("admin")){
			if(cs.args.length==1){
				cs.attendReturnText("Set Admin (User must have been created)");
				cs.attendReturnText("/qr admin <username>");
				return cs;
			}
			if(mainPool.mainConfig.giveAdminPermission==0){
				if(!cs.sender.equalsIgnoreCase(CommandSet.CONSOLE)){
					cs.attendReturnText("No permission! Please use this command in console!");
					return cs;
				}
			}
			String username=cs.args[1];
			try {
				ResultSet rs=mainPool.veriSQL.getUserResultsByUsername(username);
				if(!rs.next()){
					cs.attendReturnText("User Not Found!");
					return cs;
				}
				if(rs.getInt("admin")==1){
					cs.attendReturnText(username+" has already been admin!");
					return cs;
				}
				mainPool.veriSQL.st.update("UPDATE user SET admin=1 WHERE username='"+username+"';");
				cs.attendReturnText("Success! " +username+" is admin now!");
				return cs;
			} catch (Exception e) {
				cs.attendReturnText("[ERROR] "+e.getMessage());
				return cs;
			}
		}
		if(cs.args[0].equalsIgnoreCase("unadmin")){
			if(cs.args.length==1){
				cs.attendReturnText("Remove off Admin (User must have been created)");
				cs.attendReturnText("/qr unadmin <username>");
				return cs;
			}
			if(mainPool.mainConfig.giveAdminPermission==0){
				if(!cs.sender.equalsIgnoreCase(CommandSet.CONSOLE)){
					cs.attendReturnText("No permission! Please use this command in console!");
					return cs;
				}
			}
			String username=cs.args[1];
			try {
				ResultSet rs=mainPool.veriSQL.getUserResultsByUsername(username);
				if(!rs.next()){
					cs.attendReturnText("User Not Found!");
					return cs;
				}
				if(rs.getInt("admin")==0){
					cs.attendReturnText(username+" has never been admin!");
					return cs;
				}
				mainPool.veriSQL.st.update("UPDATE user SET admin=0 WHERE username='"+username+"';");
				cs.attendReturnText("Success! " +username+" is no longer admin!");
				return cs;
			} catch (Exception e) {
				cs.attendReturnText("[ERROR] "+e.getMessage());
				return cs;
			}
		}
		if(cs.args[0].equalsIgnoreCase("whitelist")){
			if(cs.args.length==1){
				cs.attendReturnText("Whitelist a user(User can have not been created)");
				cs.attendReturnText("/qr whitelist <username>");
				return cs;
			}
			String username=cs.args[1];
			try {
				ResultSet rs=mainPool.veriSQL.getUserResultsByUsername(username);
				if(!rs.next()){
					Calendar now = Calendar.getInstance();
					String password=((int)(Math.random()*900000+100000))+"";//Random six number for password
					String code=EncodeTool.encodeByMD5(username+"CODE"+((int)(Math.random()*1000000000))+"CODE"+password);
					mainPool.veriSQL.st.update("insert into user values"
							+ "('"+username+"','"+EncodeTool.basicEncode(password)+"',"
							+ "'','"+code+"','',0,'"
							+ now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)+"','',"
							+ "2,0,0,'','');");
					cs.attendReturnText("Success! " +username+" is in whitelist now!");
					cs.attendReturnText("Default Password: "+password);
					return cs;
				}else {
					if(rs.getInt("status")==2){
						cs.attendReturnText(username+" have already been in whitelist!");
						return cs;
					}
					mainPool.veriSQL.st.update("UPDATE user SET status=2 WHERE username='"+username+"';");
					cs.attendReturnText("Success! " +username+" is in whitelist now!");
					return cs;
				}
			} catch (Exception e) {
				cs.attendReturnText("[ERROR] "+e.getMessage());
				return cs;
			}
		}
		cs.attendReturnText("Unsupported argument! Use /qr help");
		return cs;
	}
}
