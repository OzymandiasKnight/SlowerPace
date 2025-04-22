package fr.ozymandiasknight.slowerpace.commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ozymandiasknight.slowerpace.Main;

public class JobInfosCommand implements CommandExecutor {
	private final Main plugin;
	public JobInfosCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			String player_id = player.getUniqueId().toString();
			HashMap<String, Integer> player_info = plugin.informations.get_player_data(player_id);
			Integer player_level = 1;
			player_level = player_info.get("level");
			player.sendMessage("§a[" + player.getDisplayName() + "] - Level : " + player_level.toString());
			
			for (int i=0; i < plugin.informations.jobs_list.size(); i++) {
				String job_name = (String)plugin.informations.jobs_list.get(i);
				Integer job_xp = plugin.informations.get_player_parameter(player_id, job_name+"_xp");
				Integer job_lvl = plugin.informations.get_player_parameter(player_id, job_name+"_lvl");
				String mes = "§a-" + job_name.substring(0, 1).toUpperCase() + job_name.substring(1) + " : ";
				mes = mes + job_lvl.toString();
				mes = mes + " - (" + job_xp.toString() + "/" + plugin.informations.get_level_cap(job_lvl).toString() + ")";
				player.sendMessage(mes);
				
			}
		}
		return false;
	}

}
