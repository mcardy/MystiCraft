package com.mcardy.mysticraft.spell.attack;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;

import com.mcardy.mysticraft.caster.Caster;
import com.mcardy.mysticraft.crafting.SpellRecipe;
import com.mcardy.mysticraft.projectile.TargetedProjectile;
import com.mcardy.mysticraft.spell.CastCondition;
import com.mcardy.mysticraft.spell.Spell;
import com.mcardy.mysticraft.spell.conditions.TargetLivingCastCondition;

public class TrackingArrowSpell implements Spell {

	@Override
	public void cast(Caster caster) {
		new TargetedProjectile(Arrow.class, caster.getTargetLiving()).launch(caster.getPlayer());
	}

	@Override
	public String getName() {
		return "trackingarrow";
	}

	@Override
	public String getDescription() {
		return "Shoots an arrow that tracks your target";
	}

	@Override
	public float getManaCost() {
		return 100;
	}

	@Override
	public SpellRecipe getRecipe() {
		return SpellRecipe.builder(getName()).add(Material.BOW, 1).add(Material.EYE_OF_ENDER, 1).add(Material.ARROW, 5).build();
	}
	
	@Override
	public CastCondition[] conditions() {
		return new CastCondition[] {new TargetLivingCastCondition()};
	}

}
