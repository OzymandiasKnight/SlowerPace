package fr.ozymandiasknight.slowerpace;

import org.bukkit.plugin.java.JavaPlugin;

import fr.ozymandiasknight.slowerpace.commands.HomeCommand;
import fr.ozymandiasknight.slowerpace.commands.JobCraftsCommand;
import fr.ozymandiasknight.slowerpace.commands.JobInfosCommand;
import fr.ozymandiasknight.slowerpace.commands.JobInfosLevelAdd;
import fr.ozymandiasknight.slowerpace.events.PlayerEvents;

public class Main extends JavaPlugin {
	public ServerInfos informations = new ServerInfos();
	public ItemManager item_manager = new ItemManager(this);
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
	}
	

	
	@Override
	public void onDisable() {
		//Server Shut Down
		informations.save_infos();
		System.out.println("Unloading SlowerPace");
		
	}
}
