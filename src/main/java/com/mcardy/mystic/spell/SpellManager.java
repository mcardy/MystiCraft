package com.mcardy.mystic.spell;

import java.util.HashMap;
import java.util.Map;

import com.mcardy.mystic.MystiCraft;
import com.mcardy.mystic.spell.attack.PunchSpell;

public class SpellManager {

	private Map<String, Spell> spells;
	
	public SpellManager() {
		spells = new HashMap<String, Spell>();
	}
	
	public void enable() {
		register(new TestSpell());
		register(new PunchSpell());
	}
	
	/**
	 * Gets a spell with the given name
	 * @param spell The spell to get
	 * @return Null if no spell by that name exists
	 */
	public Spell getSpell(String spell) {
		return spells.get(spell);
	}
	
	/**
	 * Registers the given spell
	 */
	public void register(Spell spell) {
		spells.put(spell.getName(), spell);
		MystiCraft.getCraftingManager().addRecipe(spell.getRecipe());
	}
	
}
