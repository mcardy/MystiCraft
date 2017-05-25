package com.mcardy.mysticraft.spell;

import com.mcardy.mysticraft.crafting.SpellRecipe;

public abstract class BaseSpell implements Spell {

	@Override
	public CastCondition[] conditions() {
		return new CastCondition[] {};
	}
	
	@Override
	public SpellRecipe getRecipe() {
		return null;
	}
	
}
