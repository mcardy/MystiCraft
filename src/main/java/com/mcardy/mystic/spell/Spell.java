package com.mcardy.mystic.spell;

import com.mcardy.mystic.caster.Caster;
import com.mcardy.mystic.crafting.SpellRecipe;

public interface Spell {

	public void cast(Caster caster);
	public CastCondition[] conditions();
	
	public String getName();
	public String getDescription();
	public float getManaCost();
	public SpellRecipe getRecipe();	
	
}
