package fr.ozymandiasknight.slowerpace;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ServerInfos {
	public ArrayList<String> jobs_list = new ArrayList<String>();
	
	public HashMap<String, HashMap<String, Integer>> players_datas = null;
	String path_data = "slower_pace.txt";
	//Load players_datas
	public void load_infos() {
		//Setup Jobs
		jobs_list.add("hunter");
		jobs_list.add("lumberjack");
		jobs_list.add("miner");
		jobs_list.add("farmer");
		jobs_list.add("builder");
		
		players_datas = FileManager.load_file(path_data);
		if (players_datas == null) {
			//Load Error Making a empty file
			players_datas = null;
			FileManager.save_file(path_data, players_datas);
		}
	}
	//Save players_datas
	public void save_infos() {
		FileManager.save_file(path_data, players_datas);
	}
		
	
	//Set players_datas variable
	public void set_players_datas(HashMap<String, HashMap<String, Integer>> datas) {
		players_datas = datas;
	}
	//Get players_datas variable
	public HashMap<String, HashMap<String, Integer>> get_players_datas()  {
		return players_datas;
	}
	
	//Get Player Data by Id
	public HashMap<String, Integer> get_player_data(String id) {
		HashMap<String, Integer> data = new HashMap<String, Integer>();
		if (players_datas != null) {
			if (players_datas.containsKey(id)) {
				data = players_datas.get(id);
			}
			
		}
		return data;
	}
	
	//Set Player Data Parameter
	public void set_player_parameter(String id, String key, Integer value) {
		HashMap<String, Integer> data = new HashMap<String, Integer>();
		if (players_datas != null) {
			if (players_datas.containsKey(id)) {
				data = players_datas.get(id);
			}
			
		}
		
		data.put(key, value);
		
		players_datas.put(id, data);
	}
	

	
	public Integer get_player_parameter(String id, String key) {
		HashMap<String, Integer> data = new HashMap<String, Integer>();
		if (players_datas != null) {
			if (players_datas.containsKey(id)) {
				data = players_datas.get(id);
			}
			
		}
		return data.get(key);
	}
	
	public Integer get_level_cap(Integer curr_level) {
		return (int) (
				100*(curr_level+1) + 50*Math.pow(curr_level+1, 2) + 100*Math.pow(curr_level, 2)
		);
	}
	
	public void add_player_xp(Player player, String job_name, Integer value) {
		
		
		if (jobs_list.contains(job_name)) {
			String id = player.getUniqueId().toString();
			String key_xp = job_name + "_xp";
			String key_lvl = job_name + "_lvl";
			
			Integer player_xp = get_player_parameter(id, key_xp);
			Integer player_lvl = get_player_parameter(id, key_lvl);
			
			for (int i=0; i<player.getServer().getOnlinePlayers().size()-1; i++) {
				value = value + 1;
			}
			
			if (player_xp+value >= get_level_cap(player_lvl)) {
				set_player_parameter(id, key_xp, 0);
				set_player_parameter(id, key_lvl, player_lvl+1);
				set_player_parameter(id, "level", get_player_parameter(id, "level") + 1);
				
				//Display level up to player
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
				player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY , player.getEyeLocation(), 10, 1, 1 , 1);
				player.sendMessage("§a" + job_name.substring(0, 1).toUpperCase() + job_name.substring(1) + " job level up");
			}
			else {
				set_player_parameter(id, key_xp, player_xp + value);
			}
		}
	}
	
	public boolean has_player_job_level(Player player, String condition, Integer cap) {
		if (jobs_list.contains(condition)) {
			String id = player.getUniqueId().toString();
			String key_lvl = condition + "_lvl";
			
			return (get_player_parameter(id, key_lvl) >= cap);
		}
		else {
			if (condition.equalsIgnoreCase("level")) {
				System.out.println("Fonctionne");
				String id = player.getUniqueId().toString();
				return (get_player_parameter(id, "level") >= cap);
			}
			else {
				return false;
			}
		}
	}
	
}
