package idv.xfl03.quesreg.command;

import java.sql.ResultSet;

import idv.xfl03.quesreg.MainPool;

public class QRCommand {
	private MainPool mainPool;
	public QRCommand(MainPool mainPool) {
		this.mainPool=mainPool;
	}

	public CommandSet onCommand(CommandSet cs){
		if(cs.sender!=CommandSet.CONSOLE){
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
		cs.returnCode=true;
		if(cs.args.length==0||cs.args[0].equalsIgnoreCase("help")){
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
		cs.attendReturnText("Unsupported argument! Use /qr help");
		return cs;
	}
}
