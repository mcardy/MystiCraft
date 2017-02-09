package com.mcardy.mystic.crafting;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;

import com.mcardy.mystic.MystiCraft;
import com.mcardy.mystic.spell.Spell;

public class SpellTome {

	// Minecraft pages have width of 19 characters
	private static final int PAGE_WIDTH = 19;
	private static final String DISPLAY_NAME = ChatColor.AQUA + "Spell Tome: ";
	private static final Material MATERIAL = Material.WRITTEN_BOOK;
	
	public static ItemStack getSpellTome(String label, Player crafter) {
		ItemStack i = new ItemStack(MATERIAL);
		Spell spell = MystiCraft.getSpellManager().getSpell(label);
		BookMeta meta = (BookMeta)i.getItemMeta();
		meta.setDisplayName(DISPLAY_NAME + StringUtils.capitalize(label));
		if (crafter != null)
			meta.setAuthor(crafter.getName());
		else
			meta.setAuthor("unknown");
		meta.setGeneration(Generation.TATTERED);
		meta.setLore(Arrays.asList(new String[]{ChatColor.GRAY + spell.getDescription(), ChatColor.GRAY + "Mana Cost: " + spell.getManaCost()}));
		// TODO Add description
		meta.addPage("-------------------=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		i.setItemMeta(meta);
		return i;
	}
	
	public static boolean isSpellTome(ItemStack tome) {
		if (tome.getType() == MATERIAL) {
			BookMeta meta = (BookMeta) tome.getItemMeta();
			if (meta.getDisplayName().startsWith(DISPLAY_NAME)) {
				return true;
			}
		}
		return false;
	}
	
	public static String getSpell(ItemStack tome) {
		if (isSpellTome(tome)) {
			return tome.getItemMeta().getDisplayName().replaceAll(DISPLAY_NAME, "").toLowerCase();
		}
		return null;
	}
	
}
