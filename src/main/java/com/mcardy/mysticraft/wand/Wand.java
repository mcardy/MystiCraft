package com.mcardy.mysticraft.wand;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public interface Wand {
	
	public Recipe getRecipe();
	
	public boolean isWand(ItemStack i);
	
	public void onInteract(Player clicker, Action action, EquipmentSlot hand, ItemStack i);
	
	public void bind(ItemStack i, String spell);

}
