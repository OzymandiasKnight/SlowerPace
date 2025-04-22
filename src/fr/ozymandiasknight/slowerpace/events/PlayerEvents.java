package fr.ozymandiasknight.slowerpace.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import fr.ozymandiasknight.slowerpace.CustomItem;
import fr.ozymandiasknight.slowerpace.Main;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;


public class PlayerEvents implements Listener {
	private final Main plugin;
	private ArrayList<Material> basic_stones = new ArrayList<Material>();
	private ArrayList<Material> intermediate_stones = new ArrayList<Material>();
	private ArrayList<Material> advanced_stones = new ArrayList<Material>();
	private ArrayList<Material> crops_blocks = new ArrayList<Material>();
	private ArrayList<Material> builder_smelt = new ArrayList<Material>();
	private ArrayList<Material> farmer_smelt = new ArrayList<Material>();
	private ArrayList<Material> miner_smelt = new ArrayList<Material>();
	private ArrayList<Material> axes_tag = new ArrayList<Material>();
	
	public PlayerEvents(Main plugin) {
		this.plugin = plugin;
		basic_stones = new ArrayList<Material>(Arrays.asList(Material.STONE, Material.SANDSTONE, Material.GRANITE, Material.ANDESITE, Material.DIORITE));
		intermediate_stones = new ArrayList<Material>(Arrays.asList(Material.COAL_ORE, Material.LAPIS_ORE, Material.REDSTONE_ORE, Material.NETHER_QUARTZ_ORE));		
		advanced_stones = new ArrayList<Material>(Arrays.asList(Material.DIAMOND_ORE, Material.EMERALD_ORE, Material.NETHER_GOLD_ORE));
		crops_blocks = new ArrayList<Material>(Arrays.asList(Material.WHEAT, Material.MELON, Material.CARROTS, Material.PUMPKIN));

		builder_smelt = new ArrayList<Material>(Arrays.asList(Material.STONE, Material.GLASS));
		farmer_smelt = new ArrayList<Material>(Arrays.asList(
				Material.COOKED_BEEF, Material.COOKED_CHICKEN, Material.COOKED_MUTTON, Material.COOKED_PORKCHOP, Material.COOKED_RABBIT
		));
		miner_smelt = new ArrayList<Material>(Arrays.asList(Material.IRON_INGOT, Material.GOLD_INGOT));
		
		axes_tag = new ArrayList<Material>(Arrays.asList(
				Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE
		));
	}
	
	//When Player Join
	@EventHandler
	public void OnJoin(PlayerJoinEvent event) {
		//When Player Join
		//Get Player Infos
		Player player = event.getPlayer();
		String player_id = player.getUniqueId().toString();
		
		//Check if players datas are null
		if (plugin.informations.players_datas == null) {
			plugin.informations.players_datas = new HashMap<String, HashMap<String, Integer>>();
		}
		
		//Check if player is in data
		if (plugin.informations.players_datas.containsKey(player_id)) {
			//Player Already in Data
			HashMap<String, Integer> player_data = plugin.informations.players_datas.get(player_id);
			plugin.informations.players_datas.put(player.getUniqueId().toString(), get_data_map(player_data));
		}
		else {
			//New Player
			
			plugin.informations.players_datas.put(player.getUniqueId().toString(), get_data_map(null));
		}
	}
	
	public HashMap<String, Integer> get_data_map(HashMap<String, Integer> current_map) {
		HashMap<String, Integer> default_data = current_map;
		if (default_data == null) {
			default_data = new HashMap<String, Integer>();
		}
		
		if (!default_data.containsKey("level")) {
			default_data.put("level", 1);
		}
		if (!default_data.containsKey("home_y")) {
			default_data.put("home_x", 0);
			default_data.put("home_y", 0);
			default_data.put("home_z", 0);
		}
		
		//Add Jobs
		for (int i=0; i < plugin.informations.jobs_list.size(); i++) {
			String job_name = (String)plugin.informations.jobs_list.get(i);
			
			if (!default_data.containsKey(job_name+"_xp")) {
				default_data.put(job_name+"_xp", 0);
			}
			if (!default_data.containsKey(job_name+"_lvl")) {
				default_data.put(job_name+"_lvl", 0);
			}
		}

		
		return default_data;
		
	}
	
	@EventHandler
	public void OnBlockBreak(BlockBreakEvent event) {
		if (Tag.LOGS.isTagged(event.getBlock().getType())) {
			plugin.informations.add_player_xp(event.getPlayer(), "lumberjack", 1);
		}
		else {
			if (basic_stones.contains(event.getBlock().getType())) {
				plugin.informations.add_player_xp(event.getPlayer(), "miner", 1);
			}
			else if (intermediate_stones.contains(event.getBlock().getType())) {
				plugin.informations.add_player_xp(event.getPlayer(), "miner", 2);
			}
			else if (advanced_stones.contains(event.getBlock().getType())) {
				plugin.informations.add_player_xp(event.getPlayer(), "miner", 5);
			}
			else {
				if (crops_blocks.contains(event.getBlock().getType())) {
					plugin.informations.add_player_xp(event.getPlayer(), "farmer", 1);
				}
			}
			
		}
	}
	
	@EventHandler
	public void OnBlockPosed(BlockPlaceEvent event) {
		plugin.informations.add_player_xp(event.getPlayer(), "builder", 1);
	}
	
	@EventHandler
	public void OnEntityDie(EntityDeathEvent event) {
		if (event.getEntity().getKiller() instanceof Player) {
			if (axes_tag.contains(event.getEntity().getKiller().getInventory().getItemInMainHand().getType())) {
				plugin.informations.add_player_xp((Player) event.getEntity().getKiller(), "lumberjack", event.getDroppedExp());
			}
			else {
				plugin.informations.add_player_xp((Player) event.getEntity().getKiller(), "hunter", event.getDroppedExp());
			}
		}
	}
	
	public void prevent_below_level(CraftItemEvent event, Material mat, String job_name, Integer level_cap) {
		if (event.getCurrentItem().getType() == mat) {
			String player_id = event.getWhoClicked().getUniqueId().toString();
			if (plugin.informations.get_player_parameter(player_id, job_name + "_lvl") < level_cap) {
				String mes = "§c" + job_name.substring(0, 1).toUpperCase() + job_name.substring(1) + " need to be level " + level_cap.toString() + " or higher";
				event.getWhoClicked().sendMessage(mes);
				event.getWhoClicked().getWorld().playSound(event.getWhoClicked().getLocation(), Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_OFF, 1.0f, 2.0f);
				event.setCancelled(true);
			}
		}
	}
	
	
	//When Player Craft Something
	@EventHandler
	public void OnPlayerGetCraft(CraftItemEvent event) {
		
		//Lumberjack
		prevent_below_level(event, Material.STONE_AXE, "lumberjack", 1);
		prevent_below_level(event, Material.IRON_AXE, "lumberjack", 2);
		prevent_below_level(event, Material.DIAMOND_AXE, "lumberjack", 3);
		prevent_below_level(event, Material.NETHERITE_AXE, "lumberjack", 4);
		//Hunter
		prevent_below_level(event, Material.STONE_SWORD, "hunter", 1);
		prevent_below_level(event, Material.IRON_SWORD, "hunter", 2);
		prevent_below_level(event, Material.DIAMOND_SWORD, "hunter", 3);
		prevent_below_level(event, Material.NETHERITE_SWORD, "hunter", 4);
		//Miner
		prevent_below_level(event, Material.STONE_PICKAXE, "miner", 1);
		prevent_below_level(event, Material.IRON_PICKAXE, "miner", 2);
		prevent_below_level(event, Material.DIAMOND_PICKAXE, "miner", 3);
		prevent_below_level(event, Material.NETHERITE_PICKAXE, "miner", 4);
		//Builder
		prevent_below_level(event, Material.STONE_SHOVEL, "builder", 1);
		prevent_below_level(event, Material.IRON_SHOVEL, "builder", 2);
		prevent_below_level(event, Material.DIAMOND_SHOVEL, "builder", 3);
		prevent_below_level(event, Material.NETHERITE_SHOVEL, "builder", 4);
		//Farmer
		prevent_below_level(event, Material.STONE_HOE, "farmer", 1);
		prevent_below_level(event, Material.IRON_HOE, "farmer", 2);
		prevent_below_level(event, Material.DIAMOND_HOE, "farmer", 3);
		prevent_below_level(event, Material.NETHERITE_HOE, "farmer", 4);
		
		if (event.isCancelled() == false) {
			plugin.informations.add_player_xp((Player) event.getWhoClicked(), "builder", 1);
		}
	}
	
	public void try_display_craft(InventoryClickEvent event,CustomItem item, Player player, Inventory inv) {
		if (event.getCurrentItem().getType() == item.item.getType()) {
			if (item.has_all_conditions(player) || player.getGameMode() == GameMode.CREATIVE) {
				inv.setItem(16, item.get_item_craft());
				event.getWhoClicked().openInventory(inv);
			}
			else {
				player.getWorld().playSound(player.getLocation(), Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_OFF, 1.0f, 2.0f);
				event.setCancelled(true);
			}
		}
	}
	
	private ItemStack remove_one_amount(ItemStack item) {
		if (item != null) {
			if (item.getAmount() > 1) {
				item.setAmount(item.getAmount() - 1);
			}
			else {
				item = new ItemStack(Material.AIR);
			}
			return item;
		}
		else {
			return new ItemStack(Material.AIR);
		}
		
		
	}
	
	private ItemStack add_one_amount(ItemStack item) {
		if (item != null) {
			Integer amount = item.getAmount();
			System.out.println(amount.toString());
			System.out.println(item.getMaxStackSize());
			
			if (amount+1 <= item.getMaxStackSize()) {
				ItemStack new_item = item.clone();
				new_item.setAmount(item.getAmount() + 1);
				return new_item;
			}
			else {
				return item;
			}
		}
		else {
			return new ItemStack(Material.AIR);
		}
	}
	
	public void try_custom_craft(InventoryClickEvent event,CustomItem item) {
		Player player = (Player) event.getWhoClicked();
		
		if (event.getCurrentItem().getType() == item.item.getType()) {
			if (item.has_all_materials(event)) {
				if (player.getItemOnCursor() == null || player.getItemOnCursor().getType() == Material.AIR) {
					player.setItemOnCursor(item.item);
					event.getInventory().setItem(10,  remove_one_amount(event.getInventory().getItem(10)));
					event.getInventory().setItem(11,  remove_one_amount(event.getInventory().getItem(11)));
					event.getInventory().setItem(12,  remove_one_amount(event.getInventory().getItem(12)));
				}
				
				else if (player.getItemOnCursor().getItemMeta() != null) {
					if (player.getItemOnCursor().getType() == item.item.getType()){	
						player.setItemOnCursor(add_one_amount(player.getItemOnCursor()));
						event.getInventory().setItem(10,  remove_one_amount(event.getInventory().getItem(10)));
						event.getInventory().setItem(11,  remove_one_amount(event.getInventory().getItem(11)));
						event.getInventory().setItem(12,  remove_one_amount(event.getInventory().getItem(12)));
					}
				}
				
				event.setCancelled(true);
			}
			else {
				event.setCancelled(true);
			}
		}
	}
	
	//Custom GUIS
	@EventHandler
	public void OnGuiClick(InventoryClickEvent event) {
		//Get Player
		Player player = (Player) event.getWhoClicked();
		
		//Job craft list
		if (event.getView().getTitle() == "§bJob's knowledges") {
			Inventory inv_craft = Bukkit.createInventory(player, 27, "§bCraft Veign");
			
			ArrayList<Integer> empty_indexes = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7,8,9,13,14,15,17,18,19,20,21,22,23,24,25,26));
			
			//Set Empty Glasses
			for (int i=0; i < empty_indexes.size(); i++) {
				inv_craft.setItem(empty_indexes.get(i), plugin.item_manager.empty);
			}
			//Test job level to display Crafts
			for (int item_offset=0; item_offset<plugin.item_manager.custom_items.size(); item_offset++) {
				try_display_craft(event, plugin.item_manager.custom_items.get(item_offset),player, inv_craft);
			}
			
			event.setCancelled(true);
		}
		
		
		//Crafts
		if (event.getView().getTitle().startsWith("§bCraft")) {
			//In a craft
			if (event.getCurrentItem() != null) {
				if (event.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE) {
					event.setCancelled(true);
				}
				
				if (event.getSlot() == (16)) {
					for (int item_offset=0; item_offset<plugin.item_manager.custom_items.size(); item_offset++) {
						try_custom_craft(event,plugin.item_manager.custom_items.get(item_offset));
					}
				}
				
			}
			
		}
	}
	@EventHandler
	public void OnGuiClose(InventoryCloseEvent event) {
		if (event.getView().getTitle().startsWith("§bCraft")) {
			if (event.getInventory().getItem(10) != null) {
				event.getPlayer().getInventory().addItem(event.getInventory().getItem(10));
			}
			if (event.getInventory().getItem(11) != null) {
				event.getPlayer().getInventory().addItem(event.getInventory().getItem(11));
			}
			if (event.getInventory().getItem(12) != null) {
				event.getPlayer().getInventory().addItem(event.getInventory().getItem(12));
			}
		}
	}
	
	@EventHandler
	public void OnPlayerInteract(PlayerInteractEvent event) {
		//Is not null
		if (event.getItem() != null) {
			//Custom items
			if (event.getItem().getItemMeta() != null) {
				//Veign Defender
				if (event.getItem().getItemMeta().getDisplayName().startsWith("§b")) {
					if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
						if (event.getItem().getType() == Material.GHAST_SPAWN_EGG) {
							event.getPlayer().getWorld().spawnEntity(event.getClickedBlock().getLocation().add(0, 1, 0), EntityType.IRON_GOLEM);
							event.getPlayer().getInventory().setItemInMainHand(remove_one_amount(event.getItem()));;
						}
						if (event.getItem().getType() == Material.BLAZE_ROD) {
							event.getPlayer().getWorld().strikeLightning(event.getClickedBlock().getLocation().add(0, 1, 0));
							event.getPlayer().getInventory().setItemInMainHand(remove_one_amount(event.getItem()));;
						}
						event.setCancelled(true);
					}
					if (event.getAction() == Action.RIGHT_CLICK_AIR) {
						if (event.getItem().getType() == Material.IRON_SWORD) {
							event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5, 4, true));
							event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5, 4, true));
							event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 5, 256, true));
						}
					}
				}
				
			}
			
		}
	}
	//Player get from furnace
	@EventHandler
	public void OnPlayerSmelt(FurnaceExtractEvent event) {
		System.out.println(event.getExpToDrop());
		//Miner
		if (miner_smelt.contains(event.getItemType())) {
			plugin.informations.add_player_xp(event.getPlayer(), "miner", 1+event.getExpToDrop());
		}
		//Farmer
		else if (farmer_smelt.contains(event.getItemType())) {
			plugin.informations.add_player_xp(event.getPlayer(), "farmer", 1+event.getExpToDrop());
		}
		//Builder
		else if (builder_smelt.contains(event.getItemType())) {
			plugin.informations.add_player_xp(event.getPlayer(), "builder", 1+event.getExpToDrop());
		}
		
	}
	
	//Fishing
	@EventHandler
	public void OnPlayerFish(PlayerFishEvent event) {
		Player player = event.getPlayer();
		if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().startsWith("§b")) {
			if (event.getState() == PlayerFishEvent.State.IN_GROUND || event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY) {
				Location loc = event.getHook().getLocation().subtract(player.getLocation());
				Vector vec = new Vector(loc.getX(),loc.getY(),loc.getZ());
				vec.normalize();
				vec = vec.multiply(Math.sqrt(loc.distance(new Location(player.getWorld(),0,0,0))));
				vec.add(player.getVelocity());
				player.setVelocity(vec);
			}
		}
		else {
			if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
				plugin.informations.add_player_xp(event.getPlayer(), "farmer", 25);
				Float multiplicator = (float) plugin.informations.get_player_parameter(event.getPlayer().getUniqueId().toString(), "farmer_lvl");
				multiplicator = 1.0f + multiplicator/100;
				System.out.println(multiplicator);
				if (Math.random()*multiplicator > 1.0) {
					
					if (Math.random()*multiplicator > 1.03) {
						player.getInventory().addItem(new ItemStack(Material.TRIDENT));					
					}
					
					else if (Math.random()*multiplicator > 1.05) {
						player.getInventory().addItem(new ItemStack(Material.NETHERITE_INGOT));					
					}
					
					else {					
						player.getWorld().spawnEntity(event.getHook().getLocation(),EntityType.GUARDIAN);
					}
					event.setCancelled(true);
				}
			}
		}
	}
}
