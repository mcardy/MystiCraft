package com.mcardy.mystic.spell;

import com.mcardy.mystic.caster.Caster;

public interface CastCondition {

	public boolean allowed(Caster caster);
	public String message();
	
}
