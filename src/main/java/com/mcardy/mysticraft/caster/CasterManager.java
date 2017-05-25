package com.mcardy.mysticraft.caster;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.mcardy.mysticraft.MystiCraft;
import com.mcardy.mysticraft.caster.knowledge.Knowledge;
import com.mcardy.mysticraft.caster.knowledge.KnowledgeSerialization;
import com.mcardy.mysticraft.spell.CastCondition;
import com.mcardy.mysticraft.spell.CastSource;
import com.mcardy.mysticraft.spell.Spell;

public class CasterManager {

	private Map<UUID, Caster> casters;
	private KnowledgeSerialization serialization;

	public CasterManager() {
		this.casters = new HashMap<UUID, Caster>();
		this.serialization = KnowledgeSerialization.getSerialization();
	}

	/**
	 * Casts a spell with the given name
	 * @param casterID The id of the caster
	 * @param label The name of the spell
	 * @param src The source of the cast
	 */
	public void cast(UUID casterId, String label, CastSource src) {
		Player player = Bukkit.getServer().getPlayer(casterId);
		if (player == null)
			return;
		Caster caster = getCaster(casterId);
		Spell spell = MystiCraft.getSpellManager().getSpell(label);
		for (CastCondition condition : getConditions(spell)) {
			if (!condition.allowed(caster)) {
				player.sendMessage(ChatColor.RED + condition.message());
				return;
			}
		}
		spell.cast(caster);
		caster.getKnowledge().drainMana(spell.getManaCost());;
	}
	
	/**
	 * Casts a spell with the given name
	 * @param casterID The id of the caster
	 * @param label The name of the spell
	 * @param src The source of the cast
	 */
	public void cast(Caster caster, String label, CastSource src) {
		Spell spell = MystiCraft.getSpellManager().getSpell(label);
		for (CastCondition condition : getConditions(spell)) {
			if (!condition.allowed(caster)) {
				caster.getPlayer().sendMessage(ChatColor.RED + condition.message());
				return;
			}
		}
		spell.cast(caster);
		caster.getKnowledge().drainMana(spell.getManaCost());;
	}

	/**
	 * Gets default conditions concatenated with the spell's specific conditions
	 * @param spell
	 * @return
	 */
	private CastCondition[] getConditions(Spell spell) {
		CastCondition spellKnown = new CastCondition() {
			@Override
			public boolean allowed(Caster caster) {
				return caster.getKnowledge().isSpellKnown(spell.getName());
			}
			@Override
			public String message() {
				return "You do not know that spell";
			}
			
		};
		CastCondition manaCheck = new CastCondition() {
			@Override
			public boolean allowed(Caster caster) {
				return caster.getKnowledge().getCurrentMana() >= spell.getManaCost();
			}

			@Override
			public String message() {
				return "You do not have enough mana to cast that spell";
			}
		};
		CastCondition[] defaultConditions = {spellKnown, manaCheck};
		if (spell.conditions().length == 0)
			return defaultConditions;
		int defaultLen = defaultConditions.length;
		int conditionLen = spell.conditions().length;
		CastCondition[] conditions = new CastCondition[defaultLen + conditionLen];
		System.arraycopy(defaultConditions, 0, conditions, 0, defaultLen);
		System.arraycopy(spell.conditions(), 0, conditions, defaultLen, conditionLen);
		return conditions;
	}

	/**
	 * Enables CasterManager
	 */
	public void enable() {
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), MystiCraft.getInstance());
		new CasterUpdater().runTaskTimer(MystiCraft.getInstance(), 2, 2);
	}

	/**
	 * Disables CasterManager
	 */
	public void disable() {
		for (Caster c : this.casters.values()) {
			c.disconnect();
		}
	}

	/**
	 * Gets a caster from a player object
	 * 
	 * @param player
	 *            The player to get
	 * @return A Caster object
	 */
	public Caster getCaster(Player player) {
		return getCaster(player.getUniqueId());
	}

	/**
	 * Gets a caster from a player's UUID
	 * 
	 * @param playerId
	 *            The id to get
	 * @return A Caster object
	 */
	public Caster getCaster(UUID playerId) {
		return casters.get(playerId);
	}

	private class PlayerListener implements Listener {

		@EventHandler
		public void onPlayerJoin(PlayerJoinEvent event) {
			Knowledge knowledge = serialization.load(event.getPlayer().getUniqueId());
			PlayerCaster caster = new PlayerCaster(event.getPlayer().getUniqueId(), knowledge);
			caster.connect();
			casters.put(event.getPlayer().getUniqueId(), caster);
		}

		@EventHandler
		public void onPlayerQuit(PlayerQuitEvent event) {
			Caster caster = casters.remove(event.getPlayer().getUniqueId());
			caster.disconnect();
			serialization.save(event.getPlayer().getUniqueId(), caster.getKnowledge());
		}

	}

	private class CasterUpdater extends BukkitRunnable {

		private int ticks = 0;

		@Override
		public void run() {
			boolean updateMana = ticks == 0;
			for (Caster caster : casters.values()) {
				caster.update();
				if (updateMana)
					caster.regenerate();
			}
			ticks++;
			if (ticks == 10)
				ticks = 0;
		}

	}

}
