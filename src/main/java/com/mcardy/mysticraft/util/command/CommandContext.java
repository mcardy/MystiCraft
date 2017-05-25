package com.mcardy.mysticraft.util.command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mcardy.mysticraft.util.command.Arguments.Argument;

public class CommandContext {

	private String label;
	private String[] arguments;
	private CommandSender sender;
		
	private Map<String, Object> args;
	
	protected CommandContext(CommandSender sender, String label, String[] arguments) {
		this.sender = sender;
		this.label = label;
		this.arguments = arguments;
		this.args = new HashMap<String, Object>();
	}
	
	public CommandSender getSender() {
		return sender;
	}
	
	public Player getPlayer() {
		if (isPlayer()) {
			return (Player) sender;
		} else {
			return null;
		}
	}
	
	public boolean isPlayer() {
		return sender instanceof Player;
	}
	
	public String[] getArguments() {
		return arguments;
	}
	
	public String getLabel() {
		return label;
	}
	
	protected boolean bindParamater(Argument key, String text) {
		Object value = null;
		text = text.toLowerCase();
				
		switch (key.type()) {
		case BOOLEAN:
			if (text == "true") {
				value = true;
			} else if (text == "false") {
				value = false;
			} else {
				return false;
			}
			break;
		case OPTION:
			for (String s : key.options()) {
				if (s.equalsIgnoreCase(text)) {
					value = s;
				}
			}
			if (value == null) {
				return false;
			}
			break;
		case STRING:
			value = text;
			break;
		case INTEGER:
			try {
				value = Integer.valueOf(text);
			} catch (NumberFormatException e) {
				return false;
			}
			break;
		case FLOAT:
			try {
				value = Float.valueOf(text);
			} catch (NumberFormatException e) {
				return false;
			}
		case PLAYER:
			value = getPlayer(text);
		default:
			break;
		}
				
		this.args.put(key.label(), value);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public <E> E getParamater(String key) {
		if (args.containsKey(key)) {
			try {
				return (E) args.get(key);
			} catch (ClassCastException ex) {
				return null;
			}
		} else {
			return null;
		}
	}
	
	private Player getPlayer(String name) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}
	
}
