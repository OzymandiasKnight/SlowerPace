package fr.ozymandiasknight.slowerpace.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ozymandiasknight.slowerpace.Main;

public class JobInfosLevelAdd implements CommandExecutor {

	private final Main plugin;
	public JobInfosLevelAdd(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			String player_id = player.getUniqueId().toString();
			plugin.informations.set_player_parameter(player_id,"level",Integer.parseInt(args[0]));
			player.sendMessage(args[0]);
		}
		
		return false;
	}

}
