package com.minecarts.verrier.tikitoolkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.minecarts.verrier.tikitoolkit.listener.*;

public class TikiToolkit extends JavaPlugin{
	
	private PlayerListener playerListener;
	private EntityListener entityListener;
	
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		
		//Set up our listeners
		playerListener = new PlayerListener(this);
		entityListener = new EntityListener(this);
		
		pm.registerEvent(Type.PLAYER_ANIMATION, this.playerListener, Event.Priority.Monitor, this);
		pm.registerEvent(Type.PLAYER_ITEM, this.playerListener, Event.Priority.Monitor, this);
		pm.registerEvent(Type.PLAYER_ITEM_HELD, this.playerListener, Event.Priority.Monitor, this);
		pm.registerEvent(Type.PLAYER_RESPAWN, this.playerListener, Event.Priority.Monitor, this);
		
		pm.registerEvent(Type.ENTITY_DEATH, this.entityListener, Event.Priority.Monitor, this);
	}
	
	public void onDisable() {
		
	}
	 
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args){
		return false;
	}
}
