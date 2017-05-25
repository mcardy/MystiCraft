package com.mcardy.mysticraft.spell.attack;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Arrow.PickupStatus;

import com.mcardy.mysticraft.caster.Caster;
import com.mcardy.mysticraft.crafting.SpellRecipe;
import com.mcardy.mysticraft.spell.Spell;

public class ArrowSpell implements Spell {

	@Override
	public void cast(Caster caster) {
		caster.getPlayer().launchProjectile(Arrow.class).setPickupStatus(PickupStatus.DISALLOWED);
	}

	@Override
	public String getName() {
		return "arrow";
	}

	@Override
	public String getDescription() {
		return "Conjure an arrow";
	}

	@Override
	public float getManaCost() {
		return 5;
	}

	@Override
	public SpellRecipe getRecipe() {
		return SpellRecipe.builder(this.getName()).add(Material.ARROW, 5).add(Material.BOW, 1).setLevel(1).build();
	}
	
}
