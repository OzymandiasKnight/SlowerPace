package fr.ozymandiasknight.slowerpace.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ozymandiasknight.slowerpace.Main;

public class HomeCommand implements CommandExecutor {
	private final Main plugin;
	public HomeCommand(Main plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			String id = player.getUniqueId().toString();
			if (args.length == 1) {
				System.out.println(args[0].toString());
				if (args[0].equals("set")) {
					if (player.getWorld().equals(player.getServer().getWorlds().get(0))) {
						plugin.informations.set_player_parameter(id, "home_x", player.getLocation().getBlockX());
						plugin.informations.set_player_parameter(id, "home_y", player.getLocation().getBlockY());
						plugin.informations.set_player_parameter(id, "home_z", player.getLocation().getBlockZ());
						player.sendMessage("§aHome set");					
					}
					else {
						player.sendMessage("§cYou can only set a home in the main world");
					}
					//Set Home
				}
				else {
					player.sendMessage("§cUse : /home set");
				}
			}
			else {
				if (args.length > 0) {
					player.sendMessage("§cToo much arguments, use one of these cases : ");
					player.sendMessage("§c-/home");
					player.sendMessage("§c-/home set");
				}
				else {
					if (plugin.informations.get_player_parameter(id, "home_y") != 0) {
						Location loc = new Location(player.getServer().getWorlds().get(0), 0.0f, 0.0f, 0.0f);
						loc.setX(plugin.informations.get_player_parameter(id, "home_x"));
						loc.setY(plugin.informations.get_player_parameter(id, "home_y"));
						loc.setZ(plugin.informations.get_player_parameter(id, "home_z"));
						player.teleport(loc);
					}
					else {
						player.sendMessage("§cNo home is set, use : /home set");
					}
					
				}
			}

		}
		return false;
	}

}
