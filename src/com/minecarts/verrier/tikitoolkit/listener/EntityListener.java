package com.minecarts.verrier.tikitoolkit.listener;

import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.Player;

import com.minecarts.verrier.tikitoolkit.*;

public class EntityListener extends org.bukkit.event.entity.EntityListener {
	private TikiToolkit plugin;
	public EntityListener(TikiToolkit instance)
	{
		plugin = instance;
	}
	
	public void onEntityDeath(EntityDeathEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			//Check to see if their drops are off
			if(!(plugin.config.getBoolean("admins."+player.getName()+".drop_items", true))){
				event.getDrops().clear();
			}
		}
	}
	
}
