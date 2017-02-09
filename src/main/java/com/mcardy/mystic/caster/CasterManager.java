package com.mcardy.mystic.caster;

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

import com.mcardy.mystic.MystiCraft;
import com.mcardy.mystic.caster.knowledge.Knowledge;
import com.mcardy.mystic.caster.knowledge.KnowledgeSerialization;
import com.mcardy.mystic.spell.CastCondition;
import com.mcardy.mystic.spell.CastSource;
import com.mcardy.mystic.spell.Spell;

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
		if (!caster.getKnowledge().isSpellKnown(label)) {
			player.sendMessage(ChatColor.RED + "You do not know that spell!");
			return;
		}
		
		Spell spell = MystiCraft.getSpellManager().getSpell(label);
		for (CastCondition condition : spell.conditions()) {
			if (!condition.allowed(caster)) {
				player.sendMessage(ChatColor.RED + condition.message());
				return;
			}
		}
		spell.cast(caster);
		caster.getKnowledge().drainMana(spell.getManaCost());;
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
	public void disable() {}
	
	/**
	 * Gets a caster from a player object
	 * @param player The player to get
	 * @return A Caster object
	 */
	public Caster getCaster(Player player) {
		return getCaster(player.getUniqueId());
	}
	
	/**
	 * Gets a caster from a player's UUID
	 * @param playerId The id to get
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
			if (ticks==10)
				ticks = 0;
		}
		
	}
	
}
