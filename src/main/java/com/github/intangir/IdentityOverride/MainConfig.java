package com.github.intangir.IdentityOverride;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

import net.cubespace.Yamler.Config.Comment;
import net.cubespace.Yamler.Config.Config;
import net.cubespace.Yamler.Config.InvalidConfigurationException;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class MainConfig extends Config {

	@Comment("Print debugging information")
	private boolean debug;
	
	@Comment("Save the first name we see from a UUID and automatically override to it in the future")
	private boolean lockName;

	@Comment("Notify person that their name is overridden")
	private boolean notify;

	@Comment("Link url given with notify")
	private String link;

	@Comment("Link name given with notify")
	private String linkName;

	@Comment("List of commands")
	private Map<String, String> commands;

	public MainConfig(Plugin plugin) {
		CONFIG_FILE = new File(plugin.getDataFolder(), "config.yml");
		
		commands = new HashMap<String, String>();
		commands.put("name", "overridename rename changename");
		
		linkName = "";
		link = "";
	}
	
	@Override
	public void init() throws InvalidConfigurationException {
		super.init();
	}
	
	public String getCommand(String key) {
		return commands.get(key).split(" ")[0];
	}
	
	public String[] getAliases(String key) {
		return commands.get(key).split(" ");
	}

	
}
