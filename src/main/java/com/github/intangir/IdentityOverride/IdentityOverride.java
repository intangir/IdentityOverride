package com.github.intangir.IdentityOverride;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import com.github.intangir.IdentityOverride.Commands.NameCommand;

import net.cubespace.Yamler.Config.InvalidConfigurationException;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.md_5.bungee.BungeeCord;
import lombok.Getter;

public class IdentityOverride extends Plugin implements Listener
{
	@Getter
	static private MainConfig config;
	static private NameOverrides names;
	static private Logger log;
	private Field nameField;

    @Override
    public void onEnable() {
        log = getLogger();
        config = new MainConfig(this);
        try {
			config.init();
		} catch (InvalidConfigurationException e) {
			getLogger().severe("Couldn't Load config.yml");
		}
        names = new NameOverrides(this);
        names.init();

        getProxy().getPluginManager().registerListener(this, this);
        getProxy().getPluginManager().registerCommand(this, new NameCommand(config, names));

        // allow overriding name completely
        try {
        	// override name field
        	nameField = UserConnection.class.getDeclaredField("name");
        	nameField.setAccessible(true);
		} catch (Exception e) {
			log.severe("Error overriding access on name field");
			e.printStackTrace();
		}
    }

    static public void debug(String message) {
		if(config.isDebug()) {
			log.info(message);
		}
	}

    @SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
    public void onLoggedIn(final PostLoginEvent e) {
    	ProxiedPlayer player = e.getPlayer();
    	String overridename = names.get(player.getUniqueId().toString());
    	
    	// check if there is an override for this name
    	debug("checking for override for " + player.getUniqueId().toString());
    	if(overridename != null) {
    		if(!player.getName().equals(overridename)) {
    		
    			// make all of the proper notifications
    			debug("Overriding Name for " + player.getUniqueId() +  " from " + player.getName() + " to " + overridename);
    			if(config.isNotify()) {
    				player.sendMessage(ChatColor.YELLOW + "Overriding Name to " + overridename + ".");
    				
    				if(!config.getLinkName().isEmpty() && !config.getLink().isEmpty())
    				{
	    				TextComponent notice = new TextComponent("If you want to change it, goto ");
	    				notice.setColor(ChatColor.YELLOW);
	    				
			    		TextComponent link = new TextComponent(config.getLinkName());
			    		link.setClickEvent(new ClickEvent( ClickEvent.Action.OPEN_URL, config.getLink()));
			    		link.setColor(ChatColor.LIGHT_PURPLE);
			    		link.setUnderlined(true);
			    		notice.addExtra(link);
			    		player.sendMessage(notice);
    				}
    			}

    			// this event is called after already being added to connections collection
    			// we have to remove it from the connections collection before we change the name
    			UserConnection conn = (UserConnection) player;
    			((BungeeCord) getProxy()).removeConnection(conn);
	    		
    			// change the name in the connection object
	    		try {
					nameField.set(conn, overridename);
	    		} catch (Exception ex) {
	    			log.severe("Error setting on name field");
	    			ex.printStackTrace();
	    		}
	    		
	    		// now that is has the proper name, readd it to the connections collection    		
	    		((BungeeCord) getProxy()).addConnection(conn);

	    		// update the initialhandler object just incase
	    		InitialHandler handler = (InitialHandler) player.getPendingConnection();
	    		handler.getLoginRequest().setData(overridename);
	    		
	    		// lastly, set the display name for the player
	    		player.setDisplayName(overridename);
    		}

    	} else if(config.isLockName()) {
    		// they don't have an override set, but lockmode is on, so save this one incase they change it
    		log.info("Locking Name for " + player.getUniqueId() + " to " + player.getName());
    		names.put(player.getUniqueId().toString(), player.getName());
    	}
    	
    }
   
}