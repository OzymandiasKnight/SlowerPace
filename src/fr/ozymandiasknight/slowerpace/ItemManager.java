package fr.ozymandiasknight.slowerpace;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class ItemManager {
	private final Main plugin;
	public ItemManager(Main plugin) {
		this.plugin = plugin;
	}
	
	public ItemStack empty = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
	public CustomItem vign_defender = null;
	public CustomItem zeus_wrath = null;
	public CustomItem katana = null;
	public CustomItem wind_stick = null;
	public CustomItem grappling_hook = null;
	public CustomItem swift_bow = null;
	
	public ArrayList<CustomItem> custom_items = new ArrayList<CustomItem>();
	
	
	public void setup() {
		
		//Empty
		ItemMeta empty_meta = empty.getItemMeta();
		empty_meta.setDisplayName("§8○");
		empty.setItemMeta(empty_meta);
		
		//Vign's Defender
		vign_defender = new CustomItem(plugin, Material.GHAST_SPAWN_EGG, "§bVign's Defender");
		ArrayList<String> vign_conditions = new ArrayList<String>(Arrays.asList("miner:6","farmer:4"));
		ArrayList<Material> vign_craft = new ArrayList<Material>(Arrays.asList(Material.VINE,Material.IRON_BLOCK));
		vign_defender.conditions_required = vign_conditions;
		vign_defender.materials_required = vign_craft;

		zeus_wrath = new CustomItem(plugin, Material.BLAZE_ROD, "§bZeus wrath");
		ArrayList<String> zeus_conditions = new ArrayList<String>(Arrays.asList("level:7","hunter:4"));
		ArrayList<Material> zeus_craft = new ArrayList<Material>(Arrays.asList(Material.BLAZE_POWDER,Material.MAGMA_CREAM,Material.FIRE_CHARGE));
		zeus_wrath.conditions_required = zeus_conditions;
		zeus_wrath.materials_required = zeus_craft;
		
		katana = new CustomItem(plugin, Material.IRON_SWORD, "§bKatana");
		ArrayList<String> katana_conditions = new ArrayList<String>(Arrays.asList("hunter:5","miner:5"));
		ArrayList<Material> katana_craft = new ArrayList<Material>(Arrays.asList(Material.IRON_SWORD,Material.SHIELD));
		katana.conditions_required = katana_conditions;
		katana.materials_required = katana_craft;
		
		wind_stick = new CustomItem(plugin, Material.STICK, "§bThrow Stick");
		ArrayList<String> wind_conditions = new ArrayList<String>(Arrays.asList("level:7","hunter:2"));
		ArrayList<Material> wind_craft = new ArrayList<Material>(Arrays.asList(Material.STICK,Material.PISTON));
		ArrayList<String> wind_enchants = new ArrayList<String>(Arrays.asList("knockback:5"));
		wind_stick.conditions_required = wind_conditions;
		wind_stick.materials_required = wind_craft;
		wind_stick.enchantments = wind_enchants;
		wind_stick.set_enchants();
		
		grappling_hook = new CustomItem(plugin, Material.FISHING_ROD, "§bGrappling Hook");
		ArrayList<String> grappling_conditions = new ArrayList<String>(Arrays.asList("level:10","farmer:6"));
		ArrayList<Material> grappling_craft = new ArrayList<Material>(Arrays.asList(Material.TRIPWIRE_HOOK,Material.FISHING_ROD));
		grappling_hook.conditions_required = grappling_conditions;
		grappling_hook.materials_required = grappling_craft;
		
		swift_bow = new CustomItem(plugin, Material.BOW, "§bSwift Bow");
		ArrayList<String> swift_conditions = new ArrayList<String>(Arrays.asList("level:17","builder:7"));
		ArrayList<Material> swift_craft = new ArrayList<Material>(Arrays.asList(Material.BOW,Material.ENDER_PEARL, Material.FEATHER));
		swift_bow.conditions_required = swift_conditions;
		swift_bow.materials_required = swift_craft;
		
		
		custom_items.add(vign_defender);
		custom_items.add(zeus_wrath);
		custom_items.add(katana);
		custom_items.add(wind_stick);
		custom_items.add(grappling_hook);
		custom_items.add(swift_bow);
	}
}
