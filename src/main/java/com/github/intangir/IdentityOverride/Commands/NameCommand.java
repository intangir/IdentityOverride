package com.github.intangir.IdentityOverride.Commands;

import com.github.intangir.IdentityOverride.MainConfig;
import com.github.intangir.IdentityOverride.NameOverrides;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

@SuppressWarnings("deprecation")
public class NameCommand extends Command {

	public String command;
	public MainConfig config;
	public NameOverrides names;
	
	public NameCommand(MainConfig config, NameOverrides names) {
		super(config.getCommand("name"), "override.name", config.getAliases("name"));
		command = config.getCommand("name");
		this.config = config;
		this.names = names;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length == 2) {
			String uuid = args[0];
			names.put(uuid, args[1]);
			sender.sendMessage(ChatColor.YELLOW + "Overriding Name for " + uuid + " to " + args[1]);
		} else {
			sender.sendMessage(ChatColor.RED + "Usage: /" + command + " [currentname/uuid] [newname]");
		}
	}
}
