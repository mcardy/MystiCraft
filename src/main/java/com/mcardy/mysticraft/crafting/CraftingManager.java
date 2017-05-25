package com.mcardy.mysticraft.crafting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.mcardy.mysticraft.MystiCraft;
import com.mcardy.mysticraft.caster.Caster;

public class CraftingManager {

	private Set<SpellRecipe> recipes;
	private Map<Block, CraftingOperation> operations;

	public CraftingManager() {
		recipes = new HashSet<SpellRecipe>();
		operations = new HashMap<Block, CraftingOperation>();
	}

	public void enable() {
		Bukkit.getPluginManager().registerEvents(new CraftingListener(), MystiCraft.getInstance());
		recipes.add(SpellRecipe.builder("test").add(Material.ENDER_PEARL, 1).build());
	}

	public void disable() {
		for (CraftingOperation op : operations.values()) {
			op.clear(true);
		}
	}

	public void addRecipe(SpellRecipe recipe) {
		if (recipe != null)
			recipes.add(recipe);
	}
	
	public Set<SpellRecipe> getRecipes() {
		return this.recipes;
	}

	public class CraftingListener implements Listener {

		private Material craftingBlock = Material.BOOKSHELF;
		private Material craftingItem = Material.BOOK;

		@EventHandler
		public void interact(PlayerInteractEvent event) {
			ItemStack held = event.getItem();
			if (held != null && SpellTome.isSpellTome(held)
					&& (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)) {
				teach(event);
			} else if (event.getClickedBlock() != null && event.getClickedBlock().getType() == craftingBlock && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (!operations.containsKey(event.getClickedBlock())) { 
					if (held != null && held.getType() == craftingItem) { // TODO Check regular book
						CraftingOperation operation = new CraftingOperation(event.getClickedBlock(), event.getItem());
						operations.put(event.getClickedBlock(), operation);
					}
				} else {
					if (event.getPlayer().isSneaking()) {
						operations.remove(event.getClickedBlock()).clear(true);;
					} else {
						CraftingOperation operation = operations.get(event.getClickedBlock());
						if (held == null) {
							if (operation.craft(event.getPlayer())) {
								operations.remove(event.getClickedBlock());
							} 
							event.setCancelled(true);
						} else {
							operation.add(held);
							event.setCancelled(true);
						}
					}
				}
			}
		}
		

		private void teach(PlayerInteractEvent event) {
			ItemStack held = event.getItem();
			String spell = SpellTome.getSpell(held);
			Caster caster = MystiCraft.getCasterManager().getCaster(event.getPlayer());
			if (caster.getKnowledge().isSpellKnown(spell)) {
				event.getPlayer().sendMessage(ChatColor.RED + "You already know that spell");
			} else {
				held.setAmount(held.getAmount() - 1);
				caster.getKnowledge().teachSpell(spell);
				event.getPlayer().sendMessage(ChatColor.GREEN + "You have successfully learned " + spell);
			}
			event.setCancelled(true);
		}

		@EventHandler
		public void onDespawn(ItemDespawnEvent event) {
			for (Entry<Block, CraftingOperation> entry : operations.entrySet()) {
				CraftingOperation op = entry.getValue();
				for (Item item : op.getItemEntities()) {
					if (item == event.getEntity()) {
						op.clear(true);
						operations.remove(entry.getKey());
						return;
					}
				}
			}
		}

		@EventHandler
		public void onBlockBreak(BlockBreakEvent event) {
			if (operations.containsKey(event.getBlock())) {
				operations.remove(event.getBlock()).clear(true);
			}
		}

	}

}
