package com.mcardy.mysticraft;

import org.bukkit.plugin.java.JavaPlugin;

import com.mcardy.mysticraft.caster.CasterManager;
import com.mcardy.mysticraft.command.AdminCommands;
import com.mcardy.mysticraft.command.CasterCommands;
import com.mcardy.mysticraft.crafting.CraftingManager;
import com.mcardy.mysticraft.spell.SpellManager;
import com.mcardy.mysticraft.util.command.CommandManager;

public class MystiCraft extends JavaPlugin {

	private static MystiCraft INSTANCE;
	
	private CasterManager casterMgr;
	private SpellManager spellMgr;
	private CraftingManager craftingMgr;
	private CommandManager commandMgr;
	
	@Override
	public void onLoad() {
		MystiCraft.INSTANCE = this;
		casterMgr = new CasterManager();
		spellMgr = new SpellManager();
		craftingMgr = new CraftingManager();
		commandMgr = new CommandManager();
	}
	
	@Override
	public void onEnable() {
		commandMgr.register(CasterCommands.class);
		commandMgr.register(AdminCommands.class);
		
		casterMgr.enable();
		spellMgr.enable();
		craftingMgr.enable();
	}
	
	@Override
	public void onDisable() {
		craftingMgr.disable();
		
	}
	
	public static MystiCraft getInstance() {
		return MystiCraft.INSTANCE;
	}
	
	public static CasterManager getCasterManager() {
		return getInstance().casterMgr;
	}
	
	public static CraftingManager getCraftingManager() {
		return getInstance().craftingMgr;
	}
	
	public static SpellManager getSpellManager() {
		return getInstance().spellMgr;
	}
	
}
