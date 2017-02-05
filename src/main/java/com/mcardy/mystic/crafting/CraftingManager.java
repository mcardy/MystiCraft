package com.mcardy.mystic.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.mcardy.mystic.MystiCraft;
import com.mcardy.mystic.caster.Caster;

public class CraftingManager {

	private Set<SpellRecipe> recipes;
	private Map<Block, Map<Item, BukkitRunnable>> operations;

	public CraftingManager() {
		recipes = new HashSet<SpellRecipe>();
		operations = new HashMap<Block, Map<Item, BukkitRunnable>>();
	}

	public void enable() {
		Bukkit.getPluginManager().registerEvents(new CraftingListener(), MystiCraft.getInstance());
		recipes.add(SpellRecipe.builder("test").add(Material.ENDER_PEARL, 1).build());
	}

	public void disable() {
		for (Block block : operations.keySet()) {
			clear(block, true);
		}
	}

	public void addRecipe(SpellRecipe recipe) {
		if (recipe != null)
			recipes.add(recipe);
	}

	private void clear(Block b, boolean doDrop) {
		Map<Item, BukkitRunnable> map = operations.get(b);
		if (map == null)
			return;

		for (Entry<Item, BukkitRunnable> entry : map.entrySet()) {
			Item item = entry.getKey();
			if (doDrop) {
				item.getWorld().dropItemNaturally(item.getLocation(), item.getItemStack()).setPickupDelay(0);
			}
			item.remove();
			entry.getValue().cancel();
		}
		operations.remove(b);
	}
	
	public class CraftingListener implements Listener {

		@EventHandler
		public void interact(PlayerInteractEvent event) {
			ItemStack held = event.getItem();
			if (held != null && SpellTome.isSpellTome(held) && (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)) {
				teach(event);
			} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (event.getClickedBlock().getType() == Material.BOOKSHELF) {
					if (event.getPlayer().isSneaking()) {
						clear(event.getClickedBlock(), true);
					} else {
						if (held != null) {
							// TODO Check if stick is wand
							if (held.getType() == Material.STICK) {
								craft(event);
							} else {
								addToCrafting(event);
							}
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
				held.setAmount(held.getAmount()-1);
				caster.getKnowledge().teachSpell(spell);
				event.getPlayer().sendMessage(ChatColor.GREEN + "You have successfully learned " + spell);
			}
			event.setCancelled(true);
		}
		
		private void craft(PlayerInteractEvent event) {
			Map<Item, BukkitRunnable> map = operations.get(event.getClickedBlock());
			if (map == null)
				return;
			List<ItemStack> items = new ArrayList<ItemStack>();
			for (Item i : map.keySet())
				items.add(i.getItemStack());
			for (SpellRecipe recipe : recipes) {
				if (recipe.equal(items)) {
					ItemStack tome = SpellTome.getSpellTome(recipe.getSpell());
					Location spawnLoc = event.getClickedBlock().getLocation().add(0.5, 1.5, 0.5);
					spawnLoc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLoc, 40);
					clear(event.getClickedBlock(), false);
					new BukkitRunnable() {
						public void run() {
							Item tomeItem = spawnLoc.getWorld().dropItem(spawnLoc, tome);
							tomeItem.setVelocity(new Vector(0, 0, 0));
							tomeItem.teleport(spawnLoc);
						}
					}.runTaskLater(MystiCraft.getInstance(), 24);
					break;
				}
			}
		}

		private void addToCrafting(PlayerInteractEvent event) {
			Map<Item, BukkitRunnable> items;
			if (!operations.containsKey(event.getClickedBlock())) {
				items = new HashMap<Item, BukkitRunnable>();
			} else {
				items = operations.get(event.getClickedBlock());
			}

			ItemStack item = event.getItem();
			Block block = event.getClickedBlock();
			World world = block.getWorld();
			Location location = block.getLocation().add(0, 1, 0.5);

			ItemStack cloned = item.clone();
			cloned.setAmount(1);
			Item entity = world.dropItem(location, cloned);
			entity.setVelocity(new Vector(0, 0, 0));
			entity.teleport(location);
			entity.setGravity(false);
			entity.setGlowing(true);
			entity.setPickupDelay(Integer.MAX_VALUE);
			item.setAmount(item.getAmount() - 1);

			BukkitRunnable runnable = new BukkitRunnable() {
				int tickCount = 0;
				int speed = 8;
				// Circle maths!
				double unit = 2 * Math.PI / (speed * 10);
				double divisor = speed * Math.PI;

				@Override
				public void run() {
					entity.setVelocity(
							new Vector(Math.sin(unit * tickCount) / divisor, 0, Math.cos(unit * tickCount) / divisor));

					tickCount++;
					if (tickCount == speed * 10) {
						tickCount = 0;
					}
				}
			};
			runnable.runTaskTimer(MystiCraft.getInstance(), 1, 1);
			items.put(entity, runnable);
			event.setCancelled(true);

			operations.put(event.getClickedBlock(), items);
		}
		
		@EventHandler
		public void onDespawn(ItemDespawnEvent event) {
			for (Map<Item, BukkitRunnable> maps : operations.values()) {
				for (Item item : maps.keySet()) {
					if (item == event.getEntity()) {
						ItemStack stack = item.getItemStack();
						item.getWorld().dropItemNaturally(item.getLocation(), stack).setPickupDelay(0);
						maps.remove(item).cancel();
						break;
					}
				}
			}
		}

		@EventHandler
		public void onBlockBreak(BlockBreakEvent event) {
			if (operations.containsKey(event.getBlock())) {
				clear(event.getBlock(), true);
			}
		}
		
	}

}
