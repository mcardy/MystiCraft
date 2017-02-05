package com.mcardy.mystic.spell;

import com.mcardy.mystic.crafting.SpellRecipe;

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
