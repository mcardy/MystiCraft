package com.mcardy.mysticraft.spell;

import com.mcardy.mysticraft.caster.Caster;

public interface CastCondition {

	public boolean allowed(Caster caster);
	public String message();
	
}
