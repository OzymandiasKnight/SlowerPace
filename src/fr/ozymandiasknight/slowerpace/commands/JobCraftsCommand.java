package fr.ozymandiasknight.slowerpace.commands;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.ozymandiasknight.slowerpace.Main;

public class JobCraftsCommand implements CommandExecutor {
	private final Main plugin;
	public JobCraftsCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Inventory inv = Bukkit.createInventory(player, 9, "§bJob's knowledges");
			
			
			for (int item_offset=0; item_offset<plugin.item_manager.custom_items.size(); item_offset++) {
				inv.setItem(item_offset, plugin.item_manager.custom_items.get(item_offset).get_item_info(player));
			}

			player.openInventory(inv);
		}
		return false;
	}

}
