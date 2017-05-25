package com.mcardy.mysticraft.wand;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.mcardy.mysticraft.MystiCraft;
import com.mcardy.mysticraft.caster.Caster;
import com.mcardy.mysticraft.spell.CastSource;

public class SimpleWand implements Wand {

	private final String displayName = ChatColor.DARK_AQUA + "Simple Wand - Bound Spell: ";
	
	private ItemStack baseItem;
	
	public SimpleWand() {
		baseItem = new ItemStack(Material.STICK, 1);
		ItemMeta meta = baseItem.getItemMeta();
		meta.setDisplayName(displayName + "none");
		meta.setLore(Arrays.asList(new String[]{"Right click to cast", "'/bind <spell>' to bind"}));
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.MENDING, 1, true);
		baseItem.setItemMeta(meta);
	}
	
	@Override
	public Recipe getRecipe() {
		ShapedRecipe r = new ShapedRecipe(baseItem);
		r.shape("  x", " y ", "x  ");
		r.setIngredient('x', Material.REDSTONE);
		r.setIngredient('y', Material.STICK);
		return r;
	}

	@Override
	public boolean isWand(ItemStack i) {
		if (i == null || !i.hasItemMeta() || !i.getItemMeta().hasDisplayName())
			return false;
		return i.getItemMeta().getDisplayName().startsWith(displayName);
	}

	@Override
	public void onInteract(Player clicker, Action action, EquipmentSlot hand, ItemStack i) {
		String boundSpell = getBoundSpell(i.getItemMeta().getDisplayName());
		if (boundSpell.equals("none")) {
			clicker.sendMessage(ChatColor.RED + "You need to bind a spell with /bind <spell>");
			return;
		}
		Caster c = MystiCraft.getCasterManager().getCaster(clicker);
		if (!c.getKnowledge().isSpellKnown(boundSpell)) {
			clicker.sendMessage(ChatColor.RED + "You do not know " + boundSpell);
			return;
		}
		MystiCraft.getCasterManager().cast(c, boundSpell, CastSource.WAND);
	}
	
	private String getBoundSpell(String name) {
		return name.replace(displayName, "");
	}

	@Override
	public void bind(ItemStack i, String spell) {
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(displayName + spell);
		i.setItemMeta(meta);
	}

}
