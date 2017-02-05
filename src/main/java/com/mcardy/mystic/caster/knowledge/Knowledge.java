package com.mcardy.mystic.caster.knowledge;

import java.util.HashSet;
import java.util.Set;

import com.mcardy.mystic.spell.Spell;

public class Knowledge {

	public static Knowledge createNewKnowledge() {
		Knowledge knowledge = new Knowledge();
		knowledge.level = 1;
		knowledge.experience = 0;
		knowledge.extraMana = 0;
		knowledge.currentMana = knowledge.getMaxMana();
		knowledge.spells = new HashSet<String>();
		return knowledge;
	}
	
	private int level;
	private int extraMana;
	private float experience;
	private float currentMana;
	private Set<String> spells;
	
	public Knowledge() {
	}
	
	public void drainMana(float amount) {
		setCurrentMana(getCurrentMana()-amount);
	}
	
	public float getCurrentMana() {
		return currentMana;
	}
	
	public float getExperience() {
		return experience;
	}
	
	public int getExtraMana() {
		return extraMana;
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getMaxMana() {
		return level*100+extraMana;
	}
	
	public float getRegenPerSecond() {
		return 1 + 0.5F*(level-1);
	}
	
	public boolean hasMana(float amount) {
		return getCurrentMana() >= amount;
	}
	
	public boolean isSpellKnown(Spell spell) {
		return isSpellKnown(spell.getName());
	}
	
	public boolean isSpellKnown(String spell) {
		return spells.contains(spell);
	}
	
	public void setCurrentMana(float mana) {
		this.currentMana = mana;
		if (currentMana > getMaxMana())
			currentMana = getMaxMana();
		if (currentMana < 0)
			currentMana = 0;
	}
	
	public void setExperience(float xp) {
		this.experience = xp;
	}
	
	public void setExtraMana(int extra) {
		this.extraMana = extra;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public void setSpellKnown(String spell, boolean known) {
		if (known) {
			this.spells.add(spell);
		} else {
			this.spells.remove(spell);
		}
	}
	
	public void teachSpell(Spell spell) {
		teachSpell(spell.getName());
	}
	
	public void teachSpell(String spell) {
		this.spells.add(spell);
	}
	
}
