package com.mcardy.mysticraft.caster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import com.mcardy.mysticraft.caster.knowledge.Knowledge;

public class PlayerCaster implements Caster {

	private Knowledge knowledge;
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
	public void regenerate() {
		knowledge.setCurrentMana(knowledge.getCurrentMana() + knowledge.getRegenPerSecond());
	}
	
	@Override
	public UUID getUUID() {
		return uuid;
	}
	
	@Override
	public LivingEntity getTargetLiving() {
		int range = 10;
		Player player = getPlayer();
		BlockIterator iterator = new BlockIterator(player, range);
		List<Entity> entities = player.getNearbyEntities(range, range, range);
		while (iterator.hasNext()) {
			Block block = iterator.next();
			for (Entity entity : entities) {
				if (entity instanceof LivingEntity) {
					int accuracy = 2;
					for (int offX = -accuracy; offX < accuracy; offX++) {
						for (int offY = -accuracy; offY < accuracy; offY++) {
							for (int offZ = -accuracy; offZ < accuracy; offZ++) {
								if (entity.getLocation().getBlock().getRelative(offX, offY, offZ).equals(block)) {
									return (LivingEntity) entity;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public void update() {
		
	}

}
