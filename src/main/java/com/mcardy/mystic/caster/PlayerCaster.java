package com.mcardy.mystic.caster;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.mcardy.mystic.caster.knowledge.Knowledge;

public class PlayerCaster implements Caster {

	private Knowledge knowledge;
	private BukkitRunnable regenRunnable;
	private UUID uuid;
	private Map<String, Object> data;
	
	protected PlayerCaster(UUID uuid, Knowledge knowledge) {
		this.knowledge = knowledge;
		this.uuid = uuid;
		this.data = new HashMap<String, Object>();
	}
	
	@Override
	public void connect() {		
	}

	@Override
	public void disconnect() {		
	}

	@Override
	public Knowledge getKnowledge() {
		return knowledge;
	}

	@Override
	public Player getPlayer() {
		return Bukkit.getPlayer(this.uuid);
	}
	
	@Override
	public BukkitRunnable getRegenerationRunnable() {
		if (regenRunnable == null) {
			regenRunnable = new BukkitRunnable() {
				@Override
				public void run() {
					knowledge.setCurrentMana(knowledge.getCurrentMana() + knowledge.getRegenPerSecond());
				}
			};
		}
		return regenRunnable;
	}
	
	public UUID getUUID() {
		return uuid;
	}

}
