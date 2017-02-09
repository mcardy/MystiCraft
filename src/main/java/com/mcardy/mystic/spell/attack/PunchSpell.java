package com.mcardy.mystic.spell.attack;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;

import com.mcardy.mystic.caster.Caster;
import com.mcardy.mystic.crafting.SpellRecipe;
import com.mcardy.mystic.spell.CastCondition;
import com.mcardy.mystic.spell.Spell;
import com.mcardy.mystic.spell.conditions.TargetLivingCastCondition;

public class PunchSpell implements Spell {

	@Override
	public void cast(Caster caster) {
		LivingEntity target = caster.getTargetLiving();
		target.damage(1);
	}

	@Override
	public CastCondition[] conditions() {
		return new CastCondition[] {new TargetLivingCastCondition()};
	}

	@Override
	public String getName() {
		return "punch";
	}

	@Override
	public String getDescription() {
		return "Punches the target creature";
	}

	@Override
	public float getManaCost() {
		return 20;
	}

	@Override
	public SpellRecipe getRecipe() {
		return SpellRecipe.builder(getName()).add(Material.IRON_SWORD, 1).build();
	}

	
	
}
