package com.mcardy.mystic.caster;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.mcardy.mystic.caster.knowledge.Knowledge;

public interface Caster {

	public void connect();
	public void disconnect();
	
	public Knowledge getKnowledge();
	public Player getPlayer();
	public BukkitRunnable getRegenerationRunnable();
	public UUID getUUID();
	
}
