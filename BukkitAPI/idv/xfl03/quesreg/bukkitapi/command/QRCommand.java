package idv.xfl03.quesreg.bukkitapi.command;

import idv.xfl03.quesreg.MainPool;
import idv.xfl03.quesreg.command.CommandSet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QRCommand implements CommandExecutor {
	private idv.xfl03.quesreg.command.QRCommand qrc;

	public QRCommand(MainPool mainPool) {
		qrc=new idv.xfl03.quesreg.command.QRCommand(mainPool);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		CommandSet cs=new CommandSet();
		cs.args=args;
		if(sender instanceof Player){
			Player p=(Player)sender;
			cs.sender=p.getName();
		}else{
			cs.sender=CommandSet.CONSOLE;
		}
		cs=qrc.onCommand(cs);
		sender.sendMessage(cs.getReturnText());
		//System.out.println("label = "+label);
		return cs.returnCode;
	}

}
