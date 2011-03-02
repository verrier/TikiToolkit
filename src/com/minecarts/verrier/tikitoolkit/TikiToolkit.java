package com.minecarts.verrier.tikitoolkit;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.util.config.Configuration;

import java.util.HashMap;

import com.minecarts.verrier.tikitoolkit.listener.*;

public class TikiToolkit extends JavaPlugin{
	
	private PlayerListener playerListener;
	private EntityListener entityListener;
	
	public HashMap<String,Integer> playerSlot;
	public Configuration config;

	public final Logger log = Logger.getLogger("Minecraft");
	
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		
		//Set up our listeners
		playerListener = new PlayerListener(this);
		entityListener = new EntityListener(this);
		
		playerSlot = new HashMap<String,Integer>();
		
		pm.registerEvent(Type.PLAYER_ANIMATION, this.playerListener, Event.Priority.Monitor, this);
		pm.registerEvent(Type.PLAYER_ITEM, this.playerListener, Event.Priority.Monitor, this);
		pm.registerEvent(Type.PLAYER_ITEM_HELD, this.playerListener, Event.Priority.Monitor, this);
		pm.registerEvent(Type.PLAYER_RESPAWN, this.playerListener, Event.Priority.Monitor, this);
		
		pm.registerEvent(Type.ENTITY_DEATH, this.entityListener, Event.Priority.Monitor, this);
		
		this.config = getConfiguration();
		//saveConfiguration();
	
		PluginDescriptionFile pdf = getDescription();
	    this.log.info("[" + pdf.getName() + "] version " + pdf.getVersion() + " loaded.");
	}
	
	public void onDisable() {
		
	}
	 
	private void saveConfiguration(){
		   	this.config.setProperty("admins.verrier.wand.type","STICK");
		   	this.config.setProperty("admins.verrier.wand.slot_1.name","Test Tool");
		   	this.config.setProperty("admins.verrier.wand.slot_1.click_left","/hpos1");
		   	this.config.setProperty("admins.verrier.wand.slot_1.click_right","/hpos2");
		   	this.config.save();
	   }
	
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args){
		return false;
	}
}
