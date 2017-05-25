package com.mcardy.mysticraft.util.command;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.SimplePluginManager;

import com.mcardy.mysticraft.util.command.Arguments.Argument;

public class CommandManager {

	private CommandMap map;
	protected Map<String, Executor> executorMap = new HashMap<String, Executor>();
	protected Map<String, TabCompleter> completerMap = new HashMap<String, TabCompleter>();

	public CommandManager() {
		if (Bukkit.getServer().getPluginManager() instanceof SimplePluginManager) {
			SimplePluginManager manager = (SimplePluginManager) Bukkit.getServer().getPluginManager();
			try {
				Field field = SimplePluginManager.class.getDeclaredField("commandMap");
				field.setAccessible(true);
				map = (CommandMap) field.get(manager);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void register(Object obj) {
		for (Method m : obj.getClass().getMethods()) {
			if (!Modifier.isStatic(m.getModifiers()) && m.getParameterCount() == 1
					&& m.getParameterTypes()[0] == CommandContext.class) {
				if (m.isAnnotationPresent(Command.class) && m.getReturnType() == boolean.class) {
					registerCommand(m, obj);
				} else if (m.isAnnotationPresent(Completer.class) && m.getReturnType() == List.class) {
					registerCompleter(m, obj);
				}
			}
		}
	}

	public void register(Class<?> c) {
		for (Method m : c.getMethods()) {
			if (Modifier.isStatic(m.getModifiers()) && m.getParameterCount() == 1
					&& m.getParameterTypes()[0] == CommandContext.class) {
				if (m.isAnnotationPresent(Command.class) && m.getReturnType() == boolean.class) {
					registerCommand(m, null);
				} else if (m.isAnnotationPresent(Completer.class) && m.getReturnType() == List.class) {
					registerCompleter(m, null);
				}
			}
		}
	}

	private void registerCommand(Method m, Object o) {
		Command cmd = m.getAnnotation(Command.class);
		String label = cmd.label().split("\\.")[0];
		if (map.getCommand(label) == null) {
			BukkitCommand bukkitCommand = new BukkitCommand(label, cmd.description(), cmd.usage());
			map.register(label, bukkitCommand);
		}
		Executor ex = new Executor(m, o, cmd);
		executorMap.put(cmd.label(), ex);
	}

	private void registerCompleter(Method m, Object o) {
		Completer comp = m.getAnnotation(Completer.class);
		TabCompleter tab = new TabCompleter(m, o);
		completerMap.put(comp.label(), tab);
	}

	private class Executor {

		private Method m;
		private Object o;
		private Command cmd;
		private Argument[] args;

		Executor(Method m, Object o, Command cmd) {
			this.m = m;
			this.o = o;
			this.cmd = cmd;
			if (m.isAnnotationPresent(Arguments.class)) {
				this.args = m.getAnnotation(Arguments.class).args();
			}
		}

		boolean execute(CommandSender sender, String label, String[] strs) {
			strs = (String[]) ArrayUtils.subarray(strs, cmd.label().split("\\.").length-1, strs.length);
			CommandContext ctx = new CommandContext(sender, label, strs);
			if (this.args != null) {
				for (int i = 0; i < this.args.length; i++) {
					if (strs.length <= i) {
						break;
					}
					if (!ctx.bindParamater(this.args[i], strs[i])) {
						sender.sendMessage(cmd.usage());
						return false;
					}
				}
				if (strs.length < this.args.length) {
					for (int i = strs.length; i < this.args.length; i++) {
						if (this.args[strs.length].required()) {
							sender.sendMessage(cmd.usage());
							return false;
						}
					}
				}
			}
			
			try {
				return (boolean) this.m.invoke(this.o, ctx);
			} catch (Exception e) {
				e.printStackTrace();
				sender.sendMessage(cmd.usage());
				return false;
			}
		}
	}

	private class TabCompleter {

		private Method m;
		private Object o;

		TabCompleter(Method m, Object o) {
			this.m = m;
			this.o = o;
		}

		@SuppressWarnings("unchecked")
		List<String> complete(CommandContext context) {
			try {
				return (List<String>) this.m.invoke(this.o, context);
			} catch (Exception ex) {
				return new ArrayList<String>();
			}
		}

	}

	private class BukkitCommand extends org.bukkit.command.Command {

		BukkitCommand(String name, String desc, String usage) {
			super(name, desc, usage, new ArrayList<String>());
		}

		@Override
		public boolean execute(CommandSender sender, String label, String[] args) {
			Executor ex = null;
			for (int i = args.length; i >= 0; i--) {
				String key = label;
				for (int j = 0; j < i; j++) {
					key += "." + args[j];
				}
				if (executorMap.containsKey(key)) {
					ex = executorMap.get(key);
					break;
				}
			}
			if (ex != null) {
				return ex.execute(sender, label, args);
			} else {
				sender.sendMessage("There was an error executing the command: no command found...");
				return true;
			}
		}

		@Override
		public List<String> tabComplete(CommandSender sender, String label, String[] args)
				throws IllegalArgumentException {
			CommandContext ctx = new CommandContext(sender, label, args);
			TabCompleter comp = null;
			for (int i = args.length; i >= 0; i--) {
				String key = label;
				for (int j = 0; j < i; j++) {
					key += "." + args[j];
				}
				if (executorMap.containsKey(key)) {
					comp = completerMap.get(key);
					break;
				}
			}
			if (comp != null) {
				return comp.complete(ctx);
			} else {
				return new ArrayList<String>();
			}
		}

	}

}
