package com.mcardy.mysticraft.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mcardy.mysticraft.MystiCraft;
import com.mcardy.mysticraft.caster.Caster;
import com.mcardy.mysticraft.spell.CastSource;
import com.mcardy.mysticraft.util.command.Arguments;
import com.mcardy.mysticraft.util.command.Arguments.Argument;
import com.mcardy.mysticraft.util.command.Arguments.ArgumentType;
import com.mcardy.mysticraft.util.command.Command;
import com.mcardy.mysticraft.util.command.CommandContext;
import com.mcardy.mysticraft.wand.Wand;

public class CasterCommands {

	@Command(label = "cast", description = "Casts a spell", usage = "/cast <spell>")
	@Arguments(args = { @Argument(label = "spell", type = ArgumentType.STRING, required = true) })
	public static boolean castCommand(CommandContext ctx) {
		if (!ctx.isPlayer()) {
			ctx.getSender().sendMessage(ChatColor.RED + "Only in-game players can cast spells");
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
			ctx.getPlayer().sendMessage(ChatColor.AQUA + "Mana: " + caster.getKnowledge().getCurrentMana() + "/"
					+ caster.getKnowledge().getMaxMana());
		} else {
			ctx.getSender().sendMessage(ChatColor.RED + "Only in-game players can check mana");
		}
		return true;
	}

	@Command(label = "mana.restore", description = "Restores mana", usage = "/mana restore <player>")
	@Arguments(args = { @Argument(label = "player", type = ArgumentType.PLAYER, required = false) })
	public static boolean manaRestoreCommand(CommandContext ctx) {
		if (ctx.getParamater("player") != null) {
			Player target = ctx.getParamater("player");
			Caster caster = MystiCraft.getCasterManager().getCaster(target);
			caster.getKnowledge().setCurrentMana(caster.getKnowledge().getMaxMana());
			target.sendMessage(ChatColor.AQUA + "Your mana has been restored");
		} else if (ctx.isPlayer()) {
			Caster caster = MystiCraft.getCasterManager().getCaster(ctx.getPlayer());
			caster.getKnowledge().setCurrentMana(caster.getKnowledge().getMaxMana());
			ctx.getSender().sendMessage(ChatColor.AQUA + "Your mana has been restored");
		} else {
			ctx.getSender().sendMessage(ChatColor.RED + "Only in-game players can restore mana");
			return true;
		}
		return true;
	}

	@Command(label = "bind", description = "Binds a spell to a wand in your hand", usage = "/bind <spell> <left|right>")
	@Arguments(args = { @Argument(label = "spell", type = ArgumentType.STRING, required = true),
			@Argument(label = "hand", type = ArgumentType.OPTION, required = true, options = { "left", "right" }) })
	public static boolean bindSpellCommand(CommandContext ctx) {
		if (!ctx.isPlayer()) {
			ctx.getSender().sendMessage(ChatColor.RED + "Only in-game players can bind spells");
			return true;
		}
		String spell = ctx.getParamater("spell").toString().toLowerCase();
		Caster caster = MystiCraft.getCasterManager().getCaster(ctx.getPlayer());
		if (MystiCraft.getSpellManager().getSpell(spell) != null && caster.getKnowledge().isSpellKnown(spell)) {
			ItemStack item = null;
			Wand wand = null;
			if (ctx.getParamater("hand").equals("right")) {
				item = ctx.getPlayer().getInventory().getItemInMainHand();
			} else if (ctx.getParamater("hand").equals("left")) {
				item = ctx.getPlayer().getInventory().getItemInOffHand();
			}
			if ((wand = MystiCraft.getWandManager().getWand(item)) != null) {
				wand.bind(item, spell);
			} else {
				ctx.getPlayer().sendMessage(ChatColor.RED + "The item in your " + ctx.getParamater("hand") + " hand is not a wand");
			}
		} else {
			ctx.getPlayer().sendMessage(ChatColor.RED + "You do not know " + spell);
		}
		return true;
	}

}
