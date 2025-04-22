package fr.ozymandiasknight.slowerpace;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItem {
	public ArrayList<String> conditions_required = new ArrayList<String>();
	public ArrayList<Material> materials_required = new ArrayList<Material>();
	public ArrayList<String> enchantments = new ArrayList<String>();
	private final Main plugin;
	public ItemStack item;
	
	public CustomItem(Main plugin, Material item_material, String item_name) {
		this.plugin = plugin;
		item = new ItemStack(item_material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(item_name);
		item.setItemMeta(meta);
	}
	
	@SuppressWarnings("unchecked")
	public Boolean has_all_materials(InventoryClickEvent event) {
		ArrayList<Material> lasting_materials = (ArrayList<Material>) materials_required.clone();
		if (event.getInventory().getItem(10) != null) {
			if (lasting_materials.contains(event.getInventory().getItem(10).getType())) {
				lasting_materials.remove(event.getInventory().getItem(10).getType());
			}
		}
		if (event.getInventory().getItem(11) != null) {
			if (lasting_materials.contains(event.getInventory().getItem(11).getType())) {
				lasting_materials.remove(event.getInventory().getItem(11).getType());
			}
		}
		if (event.getInventory().getItem(12) != null) {
			if (lasting_materials.contains(event.getInventory().getItem(12).getType())) {
				lasting_materials.remove(event.getInventory().getItem(12).getType());
			}
		}
		return lasting_materials.size() == 0;
	}
	
	public ItemStack get_item_info(Player player) {
		ItemStack item_info = item.clone();
		
		ArrayList<String> lore = new ArrayList<String>();
		ItemMeta meta = item_info.getItemMeta();

		
		for (int i=0; i<conditions_required.size(); i++) {
			String cond_name = conditions_required.get(i).split(":")[0];
			Integer cond_cap = Integer.parseInt(conditions_required.get(i).split(":")[1]);
			Boolean cond = plugin.informations.has_player_job_level(player, cond_name, cond_cap);
			//String Line
			String cond_line = cond_name.substring(0, 1).toUpperCase() + cond_name.substring(1) + " " + cond_cap;
			if (cond) {
				lore.add("§a-" + cond_line);
			}
			else {
				lore.add("§c-" + cond_line);
			}
			
		}
		
		meta.setLore(lore);
		item_info.setItemMeta(meta);
		return item_info;
	}
	
	public ItemStack get_item_craft() {
		ItemStack item_craft = item.clone();
		
		ArrayList<String> lore = new ArrayList<String>();
		for (int i=0; i<materials_required.size(); i++) {
			String material_name = materials_required.get(i).toString();
			material_name = material_name.substring(0, 1).toUpperCase() + material_name.substring(1).toLowerCase();
			material_name = "§e" + material_name.replace('_', ' ');
			lore.add(material_name);
		}
		
		ItemMeta meta = item_craft.getItemMeta();
		meta.setLore(lore);
		item_craft.setItemMeta(meta);
		
		return item_craft;
	}
	
	public Boolean has_all_conditions(Player player) {
		ArrayList<Boolean> conditions = new ArrayList<Boolean>();
		for (int i=0; i<conditions_required.size(); i++) {
			String cond_name = conditions_required.get(i).split(":")[0];
			Integer cond_cap = Integer.parseInt(conditions_required.get(i).split(":")[1]);
			Boolean cond = plugin.informations.has_player_job_level(player, cond_name, cond_cap);
			conditions.add(cond);
		}
		return !(conditions.contains(false));
		
	}
	
	public void set_enchants() {
		ItemMeta meta = item.getItemMeta();
		for(int i=0; i<enchantments.size(); i++) {
			NamespacedKey key = NamespacedKey.minecraft(enchantments.get(i).split(":")[0]);
			Enchantment name = Enchantment.getByKey(key);
			Integer level = Integer.parseInt(enchantments.get(i).split(":")[1]);
			meta.addEnchant(name, level, true);
		}
		item.setItemMeta(meta);
	}
}
