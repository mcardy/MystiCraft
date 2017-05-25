package com.mcardy.mysticraft.projectile;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class CustomProjectileListener implements Listener {

	@EventHandler
	public void onEntityCollision(ProjectileHitEvent event) {
		if (event.getEntity().hasMetadata("mysti_projectile")) {
			CustomProjectile proj =  (CustomProjectile) event.getEntity().getMetadata("mysti_projectile");
			if (event.getHitEntity() != null) {
				proj.onEntityCollision(event.getHitEntity());
			} else if (event.getHitBlock() != null) {
				proj.onBlockCollision(event.getHitBlock());
			}
		}
	}
	
}
