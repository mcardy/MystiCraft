package com.mcardy.mysticraft.projectile;

import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Arrow.PickupStatus;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

public class TargetedProjectile extends CustomProjectile {

	private Class<? extends Projectile> projectileClass;
	private Projectile entity;
	private Entity target;
	
	public TargetedProjectile(Class<? extends Projectile> proj, Entity target) {
		this.projectileClass = proj;
		this.target = target;
	}
	
	@Override
	public Projectile getEntity() {
		return entity;
	}

	@Override
	public void launch(Player player) {
		this.entity = player.launchProjectile(this.projectileClass);
		if (entity instanceof Arrow)
			((Arrow)entity).setPickupStatus(PickupStatus.DISALLOWED);
		super.launch(player);
	}

	@Override
	public void onTick() {
		if (target.isDead()) {
			return;
		}
		double speed = this.entity.getVelocity().length() * 0.9D + 0.14D;
		Vector velocity = null;
		Vector direction = this.entity.getVelocity().clone().normalize();
		Vector targetDirection = this.target.getLocation().clone().add(new Vector(0, 0.5D, 0))
				.subtract(this.entity.getLocation()).toVector();
		Vector targetDirectionNorm = targetDirection.clone().normalize();

		double angle = direction.angle(targetDirectionNorm);

		if (angle < 0.12D) {
			velocity = direction.clone().multiply(speed);
		} else {
			velocity = direction.clone().multiply((angle - 0.12D) / angle)
					.add(targetDirectionNorm.clone().multiply(0.12D / angle)).normalize().multiply(speed);
		}
		this.entity.setVelocity(velocity.add(new Vector(0.0D, 0.03D, 0.0D)));
	}

	@Override
	public void onEntityCollision(Entity entity) {		
	}

	@Override
	public void onBlockCollision(Block block) {		
	}

}
