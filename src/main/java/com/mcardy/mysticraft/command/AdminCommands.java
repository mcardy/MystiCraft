package com.mcardy.mysticraft.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.mcardy.mysticraft.MystiCraft;
import com.mcardy.mysticraft.caster.Caster;
import com.mcardy.mysticraft.util.command.Arguments;
import com.mcardy.mysticraft.util.command.Command;
import com.mcardy.mysticraft.util.command.CommandContext;
import com.mcardy.mysticraft.util.command.Arguments.Argument;
import com.mcardy.mysticraft.util.command.Arguments.ArgumentType;

public class AdminCommands {
	
	@Command(label = "teach", description = "Teaches a spell to the given player", usage = "/teach <spell> [player]")
	@Arguments(args = { @Argument(label = "spell", type = ArgumentType.STRING, required = true), @Argument(label = "player", type = ArgumentType.PLAYER, required = false) })
	public static boolean teachCommand(CommandContext ctx) {
		Player player;
		if (ctx.getParamater("player") != null) {
			player = ctx.getParamater("player");
		} else {
			if (ctx.isPlayer()) {
				player = ctx.getPlayer();
			} else {
				ctx.getSender().sendMessage(ChatColor.RED + "Specify a player to teach the spell to");
				return true;
			}
		}
		String spell = ctx.getParamater("spell");
		if (MystiCraft.getSpellManager().getSpell(spell) == null) {
			ctx.getSender().sendMessage(ChatColor.RED + "That spell does not exist!");
			return true;
		}
		Caster caster = MystiCraft.getCasterManager().getCaster(player);
		caster.getKnowledge().teachSpell(spell);
		if (ctx.getSender() != player) {
			ctx.getSender().sendMessage(ChatColor.GREEN + "You have successfully taught " + player.getName() + " " + spell);
			player.sendMessage(ChatColor.GREEN + "You have learned " + spell);
		} else {
			player.sendMessage(ChatColor.GREEN + "You have learned " + spell);
		}
		return true;
	}
	
	@Command(label = "unlearn", description = "A debugging spell to unlearn a spell", usage = "/unlearn <spell>")
	@Arguments(args = { @Argument(label = "spell", type = ArgumentType.STRING, required = true)})
	public static boolean unlearnCommand(CommandContext ctx) {
		if (!ctx.isPlayer())
			return false;
		String spell = ctx.getParamater("spell");
		Caster caster = MystiCraft.getCasterManager().getCaster(ctx.getPlayer());
		caster.getKnowledge().setSpellKnown(spell, false);
		ctx.getSender().sendMessage("You have successfully unlearned " + spell);
		return true;
	}
	
}
