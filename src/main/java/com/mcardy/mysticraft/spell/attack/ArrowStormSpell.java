package com.mcardy.mysticraft.spell.attack;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Arrow.PickupStatus;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.JsonObject;
import com.mcardy.mysticraft.MystiCraft;
import com.mcardy.mysticraft.caster.Caster;
import com.mcardy.mysticraft.crafting.SpellRecipe;
import com.mcardy.mysticraft.spell.Spell;
import com.mcardy.mysticraft.util.Configurable;

public class ArrowStormSpell implements Spell, Configurable	 {

	private int count;

	@Override
	public void cast(Caster caster) {
		new BukkitRunnable() {
			int current = 0;
			public void run() {
				caster.getPlayer().launchProjectile(Arrow.class).setPickupStatus(PickupStatus.DISALLOWED);
				current++;
				if (current == count)
					this.cancel();
			}
		}.runTaskTimer(MystiCraft.getInstance(), 1, 1);
	}

	@Override
	public String getName() {
		return "arrowstorm";
	}

	@Override
	public String getDescription() {
		return "Shoots a storm of arrows";
	}

	@Override
	public float getManaCost() {
		return 50;
	}

	@Override
	public SpellRecipe getRecipe() {
		return SpellRecipe.builder(this.getName()).add(Material.BOW, 2).add(Material.ARROW, 10).setLevel(3).setPrerequisite("arrow").build();
	}
	
	@Override
	public void setDefaults(JsonObject root) {
		root.addProperty("arrow_count", 20);
	}

	@Override
	public void load(JsonObject root) {
		count = root.get("arrow_count").getAsInt();
	}

}
