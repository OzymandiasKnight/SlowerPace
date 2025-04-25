package fr.ozymandiasknight.slowerpace.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ozymandiasknight.slowerpace.Main;

public class TpFriendCommand implements CommandExecutor {
	private final Main plugin;
	public TpFriendCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	private void SendCommandsList(Player player) {
		player.sendMessage("§cUse :");
		player.sendMessage("§c-/tpa ask <username>");
		player.sendMessage("§c-/tpa accept <username>");
		player.sendMessage("§c-/tpa deny <username>");
		player.sendMessage("§c-/tpa list");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 2) {
				String parameter = args[0];
				String request = args[1];
				Player target = Bukkit.getPlayerExact(request);
				if (target == null) {
					player.sendMessage("§c" + request + " is not connected");
				}
				else {
					if (target.isOnline()) {
						if (target.getDisplayName() == player.getDisplayName()) {
							player.sendMessage("§cYou can't use this command on yourself");
						}
						else {
							//Accepts
							if (parameter.equals("accept")) {
								//Has player demands
								if (plugin.players_tp_requests.containsKey(player)) {
									ArrayList<Player> askes = plugin.players_tp_requests.get(player);
									if (askes.contains(target)) {
										//Teleport
										target.teleport(player);
										plugin.players_tp_requests.get(player).remove(target);
										player.sendMessage("§a" + request + " successfully teleported to you");
										//Remove player from database
										if (plugin.players_tp_requests.get(player).size() == 0) {
											plugin.players_tp_requests.remove(player);
										}
									}
									else {									
										player.sendMessage("§c" + request + " hasn't asked you to teleport");
									}
								}
								//No demands
								else {
									player.sendMessage("§cYou got no teleportations requests");
								}
							}
							
							//Ask
							else if (parameter.equals("ask")) {
								//Ask player
								ArrayList<Player> list = new ArrayList<Player>();
								if (plugin.players_tp_requests.containsKey(target)) {
									list = plugin.players_tp_requests.get(target);
								}
								list.add(player);
								plugin.players_tp_requests.put(target, list);
								player.sendMessage("§aTeleport request sent to " + request + "§a, wait for him to accept");
								target.sendMessage("§a" + player.getDisplayName() + " asked to teleport to you.");
							}
							//Deny
							else if (parameter.equals("deny")) {
								if (plugin.players_tp_requests.containsKey(player)) {
									ArrayList<Player> askes = plugin.players_tp_requests.get(player);
									if (askes.contains(target)) {
										plugin.players_tp_requests.get(player).remove(target);
										if (plugin.players_tp_requests.get(player).size() == 0) {
											plugin.players_tp_requests.remove(player);
										}
										target.sendMessage("§cYour teleportation request to " + player.getDisplayName() + " got denied");
										player.sendMessage("§aYou denied " + request + " teleportation request");
									}
									else {
										player.sendMessage("§c" + request + " hasn't asked you to teleport");
									}
								}
								else {
									player.sendMessage("§cYou have no teleportations requests");
								}
								//Get Tp list
							}
							else {
								SendCommandsList(player);
							}
							
						}

					}
					else {
						player.sendMessage("§c" + request + " is not online");
					}
				}
			}
			//Args are too less
			else {
				if (args.length == 1) {
					String parameter = args[0];
					if (parameter.equals("list")) {
						if (plugin.players_tp_requests.containsKey(player)) {
							ArrayList<Player> askes = plugin.players_tp_requests.get(player);
							player.sendMessage("§aPlayers that wants to teleport to your destination : ");
							for (int i=0; i<askes.size(); i++) {
								player.sendMessage("§a-" + askes.get(i).getDisplayName());
							}
						}
						else {
							player.sendMessage("§aYou got no teleportations requests");
						}
					}
					else {
						SendCommandsList(player);
					}
				}
				else {
					SendCommandsList(player);
				}
			}
		}
		return false;
	}

}
