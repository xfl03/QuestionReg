package idv.xfl03.quesreg.command;

import java.sql.ResultSet;
import java.util.Calendar;

import idv.xfl03.quesreg.MainPool;
import idv.xfl03.quesreg.hash.EncodeTool;

public class QRCommandHandler {
	private MainPool mainPool;
	public QRCommandHandler(MainPool mainPool) {
		this.mainPool=mainPool;
	}

	public CommandRespondSet onCommand(CommandRequestSet req){
		CommandRespondSet res=new CommandRespondSet();
		if(req.sender!=CommandRequestSet.CONSOLE){
			//Verify permission
			try {
				ResultSet rs=mainPool.mainDB.getUserResultsByUsername(req.sender);
				if(!rs.next()||rs.getInt("admin")!=1){
					res.attendReturnText("No permission!");
					res.attendReturnText("You aren't QuestionReg admin. (Maybe you are OP.)");
					return res;
				}
			} catch (Exception e) {
				res.attendReturnText("[ERROR] SQL Exception.");
				return res;
			}
		}
		//If have permission
		res.returnCode=true;
		if(req.args.length==0||req.args[0].equalsIgnoreCase("help")){
			//List Help list
			res.attendReturnText("==QuestionReg Help==");
			res.attendSplitLineToReturnText();
			
			res.attendReturnText("Register a new user");
			res.attendReturnText("/qr reg <username> [optional-password (Also '*' for random)] [optional-email] [optional-age]");
			res.attendBlankLineToReturnText();
			
			res.attendReturnText("Verify a user(User must have been created)");
			res.attendReturnText("/qr verify <username/code/"+mainPool.mainConfig.getEmailSupportString()+">");
			res.attendBlankLineToReturnText();
			
			res.attendReturnText("Get user info (User must have been created)");
			res.attendReturnText("/qr info <username/code/"+mainPool.mainConfig.getEmailSupportString()+">");
			res.attendBlankLineToReturnText();

			res.attendReturnText("Set Admin (User must have been created)");
			res.attendReturnText("/qr admin <username>");
			res.attendBlankLineToReturnText();
			
			res.attendReturnText("Remove off Admin (User must have been created)");
			res.attendReturnText("/qr unadmin <username>");
			res.attendBlankLineToReturnText();
			
			res.attendReturnText("Whitelist a user(User can have not been created)");
			res.attendReturnText("/qr whitelist <username>");
			res.attendBlankLineToReturnText();
			return res;
		}
		if(req.args[0].equalsIgnoreCase("reg")){
			if(req.args.length==1){
				res.attendReturnText("Register a new user");
				res.attendReturnText("/qr reg <username> [optional-password (Also '*' for random)] [optional-email] [optional-age]");
				return res;
			}
			String username=req.args[1];
			try {
				ResultSet rs=mainPool.mainDB.getUserResultsByUsername(username);
				if(rs.next()){
					res.attendReturnText("User Exsits!");
					return res;
				}
			} catch (Exception e1) {
				res.attendReturnText("[ERROR] "+e1.getMessage());
				return res;
			}
			
			boolean displayPassword=true;
			String password=((int)(Math.random()*900000+100000))+"";//Random six number for password
			String email="";
			int age=0;
			switch(req.args.length){
			default:
			case 5:
				try{
					age=Integer.parseInt(req.args[3]);
				}catch(Exception e){
					res.attendReturnText("[ERROR] 'age' must be int");
				}
			case 4:
				email=req.args[3];
			case 3:
				password=req.args[2];
				displayPassword=false;
				break;
			case 2:
				break;
			}
			
			if(!email.equalsIgnoreCase("")){
				try {
					ResultSet rs1=mainPool.mainDB.getUserResultsByEmail(email);
					if(rs1.next()){
						res.attendReturnText("Email Exsits!");
						return res;
					}
				} catch (Exception e) {
					res.attendReturnText("[ERROR] "+e.getMessage());
					return res;
				}
				
			}
			
			try {
				Calendar now = Calendar.getInstance();
				String code=EncodeTool.encodeByMD5(username+"CODE"+((int)(Math.random()*1000000000))+"CODE"+password);
				mainPool.mainDB.st.update("insert into user values"
						+ "('"+username+"','"+EncodeTool.basicEncode(password)+"',"
						+ "'','"+code+"','"+email+"',"+age+",'"
						+ now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)+"','',"
						+ "0,0,0,'','');");
				res.attendReturnText("Success!");
				if(displayPassword){
					res.attendReturnText("Default Password: "+password);
				}
				return res;
			} catch (Exception e) {
				res.attendReturnText("[ERROR] "+e.getMessage());
				return res;
			}
		}
		if(req.args[0].equalsIgnoreCase("verify")){
			if(req.args.length==1){
				res.attendReturnText("Verify a user(User must have been created)");
				res.attendReturnText("/qr verify <username/code/"+mainPool.mainConfig.getEmailSupportString()+">");
				return res;
			}
			String input=req.args[1];
			try {
				ResultSet rs=mainPool.mainDB.getUserResultsByAnyThing(input);
				if(rs==null){
					res.attendReturnText("User Not Found!");
					return res;
				}
				int veri=rs.getInt("veri");
				String username=rs.getString("username");
				if(veri==1){
					res.attendReturnText(username+" has already been verified!");
					return res;
				}
				mainPool.mainDB.st.update("UPDATE user SET veri=1 WHERE username='"+username+"';");
				res.attendReturnText("Success! " +username+" has been verified!");
				return res;
			} catch (Exception e) {
				res.attendReturnText("[ERROR] "+e.getMessage());
				return res;
			}
		}
		if(req.args[0].equalsIgnoreCase("info")){
			if(req.args.length==1){
				res.attendReturnText("Get user info (User must have been created)");
				res.attendReturnText("/qr info <username/code/"+mainPool.mainConfig.getEmailSupportString()+">");
				res.attendBlankLineToReturnText();
				return res;
			}
			//TODO Add Code Here
		}
		if(req.args[0].equalsIgnoreCase("admin")){
			if(req.args.length==1){
				res.attendReturnText("Set Admin (User must have been created)");
				res.attendReturnText("/qr admin <username>");
				return res;
			}
			if(mainPool.mainConfig.giveAdminPermission==0){
				if(!req.sender.equalsIgnoreCase(CommandRequestSet.CONSOLE)){
					res.attendReturnText("No permission! Please use this command in console!");
					return res;
				}
			}
			String username=req.args[1];
			try {
				ResultSet rs=mainPool.mainDB.getUserResultsByUsername(username);
				if(!rs.next()){
					res.attendReturnText("User Not Found!");
					return res;
				}
				if(rs.getInt("admin")==1){
					res.attendReturnText(username+" has already been admin!");
					return res;
				}
				mainPool.mainDB.st.update("UPDATE user SET admin=1 WHERE username='"+username+"';");
				res.attendReturnText("Success! " +username+" is admin now!");
				return res;
			} catch (Exception e) {
				res.attendReturnText("[ERROR] "+e.getMessage());
				return res;
			}
		}
		if(req.args[0].equalsIgnoreCase("unadmin")){
			if(req.args.length==1){
				res.attendReturnText("Remove off Admin (User must have been created)");
				res.attendReturnText("/qr unadmin <username>");
				return res;
			}
			if(mainPool.mainConfig.giveAdminPermission==0){
				if(!req.sender.equalsIgnoreCase(CommandRequestSet.CONSOLE)){
					res.attendReturnText("No permission! Please use this command in console!");
					return res;
				}
			}
			String username=req.args[1];
			try {
				ResultSet rs=mainPool.mainDB.getUserResultsByUsername(username);
				if(!rs.next()){
					res.attendReturnText("User Not Found!");
					return res;
				}
				if(rs.getInt("admin")==0){
					res.attendReturnText(username+" has never been admin!");
					return res;
				}
				mainPool.mainDB.st.update("UPDATE user SET admin=0 WHERE username='"+username+"';");
				res.attendReturnText("Success! " +username+" is no longer admin!");
				return res;
			} catch (Exception e) {
				res.attendReturnText("[ERROR] "+e.getMessage());
				return res;
			}
		}
		if(req.args[0].equalsIgnoreCase("whitelist")){
			if(req.args.length==1){
				res.attendReturnText("Whitelist a user(User can have not been created)");
				res.attendReturnText("/qr whitelist <username>");
				return res;
			}
			String username=req.args[1];
			try {
				ResultSet rs=mainPool.mainDB.getUserResultsByUsername(username);
				if(!rs.next()){
					Calendar now = Calendar.getInstance();
					String password=((int)(Math.random()*900000+100000))+"";//Random six number for password
					String code=EncodeTool.encodeByMD5(username+"CODE"+((int)(Math.random()*1000000000))+"CODE"+password);
					mainPool.mainDB.st.update("insert into user values"
							+ "('"+username+"','"+EncodeTool.basicEncode(password)+"',"
							+ "'','"+code+"','',0,'"
							+ now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)+"','',"
							+ "2,0,0,'','');");
					res.attendReturnText("Success! " +username+" is in whitelist now!");
					res.attendReturnText("Default Password: "+password);
					return res;
				}else {
					if(rs.getInt("status")==2){
						res.attendReturnText(username+" have already been in whitelist!");
						return res;
					}
					mainPool.mainDB.st.update("UPDATE user SET status=2 WHERE username='"+username+"';");
					res.attendReturnText("Success! " +username+" is in whitelist now!");
					return res;
				}
			} catch (Exception e) {
				res.attendReturnText("[ERROR] "+e.getMessage());
				return res;
			}
		}
		res.attendReturnText("Unsupported argument! Use /qr help");
		return res;
	}
}
