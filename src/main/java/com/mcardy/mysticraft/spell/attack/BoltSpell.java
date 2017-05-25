package com.mcardy.mysticraft.spell.attack;

import org.bukkit.Material;

import com.mcardy.mysticraft.caster.Caster;
import com.mcardy.mysticraft.crafting.SpellRecipe;
import com.mcardy.mysticraft.spell.CastCondition;
import com.mcardy.mysticraft.spell.Spell;
import com.mcardy.mysticraft.spell.conditions.TargetLivingCastCondition;

public class BoltSpell implements Spell {

	@Override
	public void cast(Caster caster) {
		caster.getPlayer().getWorld().strikeLightning(caster.getTargetLiving().getLocation());
	}

	@Override
	public String getName() {
		return "bolt";
	}

	@Override
	public String getDescription() {
		return "Calls down a bolt of lightning";
	}

	@Override
	public float getManaCost() {
		return 100;
	}

	@Override
	public SpellRecipe getRecipe() {
		return SpellRecipe.builder("bolt").add(Material.EMERALD, 3).add(Material.DIAMOND_SWORD, 1).build();
	}
	
	@Override
	public CastCondition[] conditions() {
		return new CastCondition[]{new TargetLivingCastCondition()};
	}

}
