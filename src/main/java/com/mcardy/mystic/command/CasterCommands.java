package com.mcardy.mystic.command;

import org.bukkit.ChatColor;

import com.mcardy.mystic.MystiCraft;
import com.mcardy.mystic.caster.Caster;
import com.mcardy.mystic.spell.CastSource;
import com.mcardy.mystic.util.command.Arguments;
import com.mcardy.mystic.util.command.Command;
import com.mcardy.mystic.util.command.CommandContext;
import com.mcardy.mystic.util.command.Arguments.Argument;
import com.mcardy.mystic.util.command.Arguments.ArgumentType;

public class CasterCommands {
	
	@Command(label = "cast", description = "Casts a spell", usage = "/cast <spell>")
	@Arguments(args = { @Argument(label = "spell", type = ArgumentType.STRING, required = true) })
	public static boolean castCommand(CommandContext ctx) {
		if (!ctx.isPlayer()) {
			ctx.getSender().sendMessage("Only in-game players can cast spells");
			return true;
		}
		String spellLabel = ctx.getParamater("spell");
		MystiCraft.getCasterManager().cast(ctx.getPlayer().getUniqueId(), spellLabel, CastSource.COMMAND);
		return true;
	}
	
	@Command(label = "mana", description = "Shows how much mana you have", usage = "/mana")
	public static boolean manaCommand(CommandContext ctx) {
		if (ctx.isPlayer()) {
			Caster caster = MystiCraft.getCasterManager().getCaster(ctx.getPlayer());
			// TODO Make pretty
			ctx.getPlayer().sendMessage(ChatColor.AQUA + "Mana: " + caster.getKnowledge().getCurrentMana() + "/" + caster.getKnowledge().getMaxMana());
		} else {
			ctx.getSender().sendMessage(ChatColor.RED + "You are not a player!");
		}
		return true;
	}

}
