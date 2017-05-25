package com.mcardy.mysticraft.projectile;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.mcardy.mysticraft.MystiCraft;

public abstract class CustomProjectile {

	public abstract org.bukkit.entity.Projectile getEntity();
	
	public void launch(Player player) {
		getEntity().setMetadata("mysti_projectile", new FixedMetadataValue(MystiCraft.getInstance(), this));
		new BukkitRunnable() {
			@Override
			public void run() {
				if (getEntity().isOnGround() || getEntity().isDead())
					this.cancel();
				onTick();
			}
		}.runTaskTimer(MystiCraft.getInstance(), 1, 1);
	}
	
	public abstract void onTick();
	public abstract void onEntityCollision(Entity entity);
	public abstract void onBlockCollision(Block block);
	
}
