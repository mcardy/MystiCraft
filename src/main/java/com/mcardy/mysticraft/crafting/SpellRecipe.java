package com.mcardy.mysticraft.crafting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SpellRecipe {

	private Map<Material, Integer> ingredients;
	private String result;
	private int level;
	private String[] prerequisites;
	
	/**
	 * Creates a new SpellRecipe. See {@link SpellRecipeBuilder} for easy construction
	 * @param result Resultant Spell
	 * @param level Level required
	 * @param ingredients Ingredients
	 */
	public SpellRecipe(String result, int level, Map<Material, Integer> ingredients, String[] prerequisites) {
		this.result = result;
		this.level = level;
		this.ingredients = ingredients;
		this.prerequisites = prerequisites;
	}
	
	/**
	 * Compares the recipe to a list of itemstacks
	 * @param items Items to compare
	 * @return True if it equals
	 */
	public boolean equal(List<ItemStack> items) {
		Map<Material, Integer> toCompare = new HashMap<Material, Integer>();
		for (ItemStack item : items) {
			if (toCompare.containsKey(item.getType())) {
				toCompare.put(item.getType(), toCompare.get(item.getType()) + item.getAmount());
			} else {
				toCompare.put(item.getType(), item.getAmount());
			}
		}
		if (toCompare.keySet().size() != ingredients.keySet().size())
			return false;
		for (Entry<Material, Integer> entry : toCompare.entrySet()) {
			if (ingredients.get(entry.getKey()) != entry.getValue()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Gets the resultant spell
	 */
	public String getSpell() {
		return this.result;
	}
	
	/**
	 * Get the level required
	 */
	public int getLevel() {
		return this.level;
	}
		
	public String[] getPrerequisites() {
		return this.prerequisites;
	}

	/**
	 * Returns a new SpellRecipeBuilder
	 * @param spell the spell to create a builder for
	 * @return a new SpellRecipeBuilder
	 */
	public static SpellRecipeBuilder builder(String spell) {
		return new SpellRecipeBuilder(spell);
	}
	
	public static class SpellRecipeBuilder {
		
		private String spell;
		private String[] prerequisites;
		private int level;
		private Map<Material, Integer> map;
		
		private SpellRecipeBuilder(String spell) {
			this.spell = spell;
			this.level = 1;
			map = new HashMap<Material, Integer>();
		}
		
		/**
		 * Adds a new material
		 * @param m the material
		 * @param a the amount of m
		 * @return self
		 */
		public SpellRecipeBuilder add(Material m, int a) {
			map.put(m, a);
			return this;
		}
		
		/**
		 * Sets the level, defaulted to 1
		 * @param level Level to set
		 * @return self
		 */
		public SpellRecipeBuilder setLevel(int level) {
			this.level = level;
			return this;
		}
		
		public SpellRecipeBuilder setPrerequisite(String... spells) {
			this.prerequisites = spells;
			return this;
		}
		
		/**
		 * Creates a new SpellRecipe from paramaters
		 */
		public SpellRecipe build() {
			return new SpellRecipe(spell, level, map, prerequisites);
		}
		
	}
	
}
