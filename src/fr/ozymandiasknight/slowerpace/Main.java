package fr.ozymandiasknight.slowerpace;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.ozymandiasknight.slowerpace.commands.HomeCommand;
import fr.ozymandiasknight.slowerpace.commands.JobCraftsCommand;
import fr.ozymandiasknight.slowerpace.commands.JobInfosCommand;
import fr.ozymandiasknight.slowerpace.commands.JobInfosLevelAdd;
import fr.ozymandiasknight.slowerpace.commands.TpFriendCommand;
import fr.ozymandiasknight.slowerpace.events.PlayerEvents;

public class Main extends JavaPlugin {
	public ServerInfos informations = new ServerInfos();
	public ItemManager item_manager = new ItemManager(this);
	public HashMap<Player, ArrayList<Player>> players_tp_requests = new HashMap<Player, ArrayList<Player>>();
	
	@Override
	public void onEnable() {
		//Server Start
		System.out.println("Loading SlowerPace");
		informations.load_infos();
		item_manager.setup();
		getServer().getPluginManager().registerEvents(new PlayerEvents(this), this);
		getCommand("job-info").setExecutor(new JobInfosCommand(this));
		getCommand("job-add").setExecutor(new JobInfosLevelAdd(this));
		getCommand("job-craft").setExecutor(new JobCraftsCommand(this));
		getCommand("home").setExecutor(new HomeCommand(this));
		getCommand("tpa").setExecutor(new TpFriendCommand(this));
	}
	

	
	@Override
	public void onDisable() {
		//Server Shut Down
		informations.save_infos();
		System.out.println("Unloading SlowerPace");
		
	}
}
