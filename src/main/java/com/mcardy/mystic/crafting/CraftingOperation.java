package com.mcardy.mystic.crafting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.mcardy.mystic.MystiCraft;

public class CraftingOperation {

	private Map<Item, BukkitRunnable> items;
	private Block block;
	private Item book;
	
	public CraftingOperation(Block block, ItemStack stack) {
		this.block = block;
		ItemStack toDrop = stack.clone();
		toDrop.setAmount(1);
		stack.setAmount(stack.getAmount()-1);
		Item item = block.getWorld().dropItem(block.getLocation().add(0.5, 1.2, 0.5), toDrop);
		item.setVelocity(new Vector(0,0,0));
		item.teleport(block.getLocation().add(0.5, 1.5, 0.5));
		item.setPickupDelay(Integer.MAX_VALUE);
		item.setGlowing(true);
		item.setGravity(false);
		this.book = item;
		items = new HashMap<Item, BukkitRunnable>();
	}
	
	public void add(ItemStack stack) {
		World world = block.getWorld();
		Location location = block.getLocation().add(0, 1, 0.5);

		ItemStack cloned = stack.clone();
		cloned.setAmount(1);
		Item entity = world.dropItem(location, cloned);
		entity.setVelocity(new Vector(0, 0, 0));
		entity.teleport(location);
		entity.setGravity(false);
		entity.setGlowing(true);
		entity.setPickupDelay(Integer.MAX_VALUE);
		stack.setAmount(stack.getAmount() - 1);

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
		String spell = getSpell();
		if (spell != null) {
			book.setCustomName("Ready to craft: " + spell);
			book.setCustomNameVisible(true);
		} else {
			book.setCustomNameVisible(false);
		}
	}
	
	public void clear(boolean doDrop) {
		for (Entry<Item, BukkitRunnable> entry : items.entrySet()) {
			Item item = entry.getKey();
			if (doDrop) {
				item.getWorld().dropItemNaturally(item.getLocation(), item.getItemStack()).setPickupDelay(0);
			}
			item.remove();
			entry.getValue().cancel();
		}
		
		if (doDrop)
			book.getWorld().dropItemNaturally(book.getLocation(), book.getItemStack()).setPickupDelay(0);
		book.remove();
	}
	
	public boolean craft(Player crafter) {
		// TODO Block crafting operation when not allowed for player
		String spell = getSpell();
		if (spell != null) {
			ItemStack tome = SpellTome.getSpellTome(spell, crafter);
			Location spawnLoc = block.getLocation().add(0.5, 1.5, 0.5);
			spawnLoc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLoc, 40);
			for (Entry<Item, BukkitRunnable> entry : items.entrySet()) {
				Item item = entry.getKey();
				item.remove();
				entry.getValue().cancel();
			}
			book.setCustomNameVisible(false);
			new BukkitRunnable() {
				public void run() {
					book.setItemStack(tome);
					book.setGlowing(false);
					book.setPickupDelay(0);
					book.getWorld().playSound(book.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
				}
			}.runTaskLater(MystiCraft.getInstance(), 32);
			return true;
		} else {
			return false;
		}
	}
	
	public String getSpell() {
		List<ItemStack> itemList = getItems();
		for (SpellRecipe recipe : MystiCraft.getCraftingManager().getRecipes()) {
			if (recipe.equal(itemList)) {
				return recipe.getSpell();
			}
		}
		return null;
	}
	
	public Block getBlock() {
		return this.block;
	}
	
	public List<ItemStack> getItems() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		for (Item i : items.keySet())
			list.add(i.getItemStack());
		return list;
	}
	
	public Collection<Item> getItemEntities() {
		return items.keySet();
	}
	
}
