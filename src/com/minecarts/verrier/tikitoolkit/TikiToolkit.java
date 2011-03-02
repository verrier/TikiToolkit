package com.minecarts.verrier.tikitoolkit;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

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
				
		pm.registerEvent(Type.PLAYER_ANIMATION, this.playerListener, Event.Priority.Monitor, this);
		pm.registerEvent(Type.PLAYER_ITEM, this.playerListener, Event.Priority.Monitor, this);
		pm.registerEvent(Type.PLAYER_ITEM_HELD, this.playerListener, Event.Priority.Monitor, this);
		pm.registerEvent(Type.PLAYER_RESPAWN, this.playerListener, Event.Priority.Monitor, this);
		
		pm.registerEvent(Type.ENTITY_DEATH, this.entityListener, Event.Priority.High, this);
		
		this.config = getConfiguration();
	
		PluginDescriptionFile pdf = getDescription();
	    this.log.info("[" + pdf.getName() + "] version " + pdf.getVersion() + " loaded.");
	}
	
	public void onDisable() {
		
	}
	 
	
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args){
		if(cmdLabel.toLowerCase().equals("tiki")){
			if(args.length == 1){
				if(args[0].toLowerCase().equals("reload")){
					this.config.load();
					log.info("TikiToolkit config reloaded.");
					if(sender instanceof Player){
						((Player)sender).sendMessage("TikiToolkit config reloaded.");
					}
					return true;
				} else 
				if(args[0].toLowerCase().equals("identify")){
					if(sender instanceof Player){
						Player player = (Player) sender;
						player.sendMessage("Tiki: You are holding: " + ChatColor.GOLD + player.getInventory().getItemInHand().getType().name());
						return true;
					}
				}
			}
		}
		return false;
	}
}
