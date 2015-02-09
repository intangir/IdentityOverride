package com.github.intangir.IdentityOverride;

import net.md_5.bungee.api.plugin.Plugin;
import lombok.Getter;

public class IdentityOverride extends Plugin implements Listener
{
	@Getter
	public MainConfig config;

    @Override
    public void onEnable() {
        config = new MainConfig(this);
        try {
			config.init();
		} catch (InvalidConfigurationException e) {
			getLogger().severe("Couldn't Load config.yml");
		}
        getProxy().getPluginManager().registerListener(this, this);
    }

    @EventHandler
    public void onLoggedIn(final PostLoginEvent e) {
    }

}