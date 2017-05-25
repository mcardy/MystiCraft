package com.mcardy.mysticraft.spell;

import com.mcardy.mysticraft.caster.Caster;
import com.mcardy.mysticraft.crafting.SpellRecipe;

public interface Spell {

	public void cast(Caster caster);
	public default CastCondition[] conditions() { return new CastCondition[] {}; }
	
	public String getName();
	public String getDescription();
	public float getManaCost();
	public SpellRecipe getRecipe();	
	
}
