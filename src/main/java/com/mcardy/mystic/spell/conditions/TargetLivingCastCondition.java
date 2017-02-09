package com.mcardy.mystic.spell.conditions;

import org.bukkit.entity.Entity;

import com.mcardy.mystic.caster.Caster;
import com.mcardy.mystic.spell.CastCondition;

public class TargetLivingCastCondition implements CastCondition {

	@Override
	public boolean allowed(Caster caster) {
		Entity living = caster.getTargetLiving();
		return living != null;
	}

	@Override
	public String message() {
		return "You do not have a target to cast this spell on";
	}

}
