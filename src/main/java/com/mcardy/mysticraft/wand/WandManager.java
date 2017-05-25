package com.mcardy.mysticraft.wand;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.mcardy.mysticraft.MystiCraft;

public class WandManager {

	public Set<Wand> wands;
	
	public WandManager() {
		wands = new HashSet<Wand>();
	}
	
	public void enable() {
		Bukkit.getPluginManager().registerEvents(new WandListener(), MystiCraft.getInstance());
		registerWand(new SimpleWand());
	}
	
	public void disable() {
	}
	
	public void registerWand(Wand wand) {
		this.wands.add(wand);
		if (wand.getRecipe() != null) {
			Bukkit.addRecipe(wand.getRecipe());
		}
	}
	
	public boolean isWand(ItemStack i) {
		return getWand(i) != null;
	}
	
	public Wand getWand(ItemStack i) {
		for (Wand wand : wands) {
			if (wand.isWand(i)) {
				return wand;
			}
		}
		return null;
	}
	
	private class WandListener implements Listener {
		
		@EventHandler
		public void onPlayerInteract(PlayerInteractEvent event) {
			Wand wand;
			if (event.getItem()!= null) {
				if ((wand = getWand(event.getItem())) != null) {
					wand.onInteract(event.getPlayer(), event.getAction(), event.getHand(), event.getItem());
				}
			}
		}
		
	}
	
}
