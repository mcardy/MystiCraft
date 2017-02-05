package com.mcardy.mystic.spell;

import org.bukkit.Material;

import com.mcardy.mystic.caster.Caster;
import com.mcardy.mystic.crafting.SpellRecipe;

public class TestSpell extends BaseSpell {

	@Override
	public void cast(Caster caster) {
		caster.getPlayer().sendMessage("Testing!");
	}

	@Override
	public String getName() {
		return "test";
	}
	
	@Override
	public String getDescription() {
		return "A spell made specifically for testing";
	}

	@Override
	public float getManaCost() {
		return 50;
	}
	
	@Override
	public SpellRecipe getRecipe() {
		return SpellRecipe.builder(getName()).add(Material.WOOL, 2).build();
	}

}
