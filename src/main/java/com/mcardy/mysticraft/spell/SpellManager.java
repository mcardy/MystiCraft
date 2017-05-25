package com.mcardy.mysticraft.spell;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.Listener;

import com.google.gson.JsonObject;
import com.mcardy.mysticraft.MystiCraft;
import com.mcardy.mysticraft.spell.attack.ArrowSpell;
import com.mcardy.mysticraft.spell.attack.ArrowStormSpell;
import com.mcardy.mysticraft.spell.attack.BoltSpell;
import com.mcardy.mysticraft.spell.attack.PunchSpell;
import com.mcardy.mysticraft.util.Configurable;
import com.mcardy.mysticraft.util.JsonConfig;

public class SpellManager {

	private Map<String, Spell> spells;
	private JsonConfig spellConfig;
	
	public SpellManager() {
		spells = new HashMap<String, Spell>();
		spellConfig = new JsonConfig(MystiCraft.getInstance().getDataFolder(), "spells.conf");
		spellConfig.saveDefaultConfig();
	}
	
	public void enable() {
		registerSpells();
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
		if (spell instanceof Listener) {
			MystiCraft.getInstance().getServer().getPluginManager().registerEvents((Listener) spell, MystiCraft.getInstance());
		}
		if (spell instanceof Configurable) {
			if (!spellConfig.getConfig().has(spell.getName())) {
				JsonObject obj = new JsonObject();
				((Configurable) spell).setDefaults(obj);
				spellConfig.getConfig().add(spell.getName(), obj);
				spellConfig.saveConfig();
				((Configurable) spell).load(obj);
			} else {
				((Configurable) spell).load(spellConfig.getConfig().get(spell.getName()).getAsJsonObject());
			}
		}
	}
	
	private void registerSpells() {
		register(new ArrowSpell());
		register(new ArrowStormSpell());
		register(new BoltSpell());
		register(new PunchSpell());
	}
	
}
