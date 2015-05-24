package idv.xfl03.quesreg.bukkitapi.command;

import idv.xfl03.quesreg.MainPool;
import idv.xfl03.quesreg.command.CommandRequestSet;
import idv.xfl03.quesreg.command.CommandRespondSet;
import idv.xfl03.quesreg.command.QRCommandHandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QRCommandListener implements CommandExecutor {
	private QRCommandHandler qrch;

	public QRCommandListener(MainPool mainPool) {
		qrch=new QRCommandHandler(mainPool);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		CommandRequestSet req=new CommandRequestSet();
		req.args=args;
		if(sender instanceof Player){
			Player p=(Player)sender;
			req.sender=p.getName();
		}else{
			req.sender=CommandRequestSet.CONSOLE;
		}
		CommandRespondSet res=qrch.onCommand(req);
		sender.sendMessage(res.getReturnText());
		return res.returnCode;
	}

}
