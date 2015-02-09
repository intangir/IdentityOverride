package com.github.intangir.IdentityOverride;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.cubespace.Yamler.Config.Config;
import net.cubespace.Yamler.Config.InvalidConfigurationException;
import net.md_5.bungee.api.plugin.Plugin;

public class NameOverrides extends Config 
{
	public NameOverrides() {}
	public NameOverrides(Plugin plugin) {
		this.log = plugin.getProxy().getLogger();
		
		CONFIG_FILE = new File(plugin.getDataFolder(), "nameoverrides.yml");
		
		names = new HashMap<String, String>();
	}

	transient Logger log;
	
	private Map<String, String> names;
	
	@Override
	public void init() {
		try {
			super.init();
		} catch (InvalidConfigurationException e) {
			log.warning("Couldn't Load " + CONFIG_FILE);
			e.printStackTrace();
		}
	}

	@Override
	public void save() {
		try {
			super.save();
		} catch (InvalidConfigurationException e) {
			log.warning("Couldn't Save " + CONFIG_FILE);
			e.printStackTrace();
		}
	}

	public boolean contains(String uuid) {
		return names.containsKey(uuid);
	}
	
	public void put(String uuid, String name) {
		names.put(uuid, name);
		save();
	}
	
	public String get(String uuid) {
		return names.get(uuid);
	}
}
