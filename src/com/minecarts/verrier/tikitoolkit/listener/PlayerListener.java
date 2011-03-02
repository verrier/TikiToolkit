package com.minecarts.verrier.tikitoolkit.listener;

import com.minecarts.verrier.tikitoolkit.*;
import org.bukkit.event.player.*;
import org.bukkit.event.entity.EntityDeathEvent;

import org.bukkit.entity.Player;

public class PlayerListener extends org.bukkit.event.player.PlayerListener{

	TikiToolkit plugin;
	
	public PlayerListener(TikiToolkit instance)
	{
		plugin = instance;
	}

	
	
	public void onPlayerAnimation(PlayerAnimationEvent event) {
		
	}
	
	public void onPlayerItem(PlayerItemEvent event){
		
	}
	
	public void onPlayerItemHeld(PlayerItemHeldEvent event){
		Player player = event.getPlayer();
		player.sendMessage(String.format("New inventory slot: %s",event.getNewSlot()));
	}
	
	public void onPlayerRespawn(PlayerRespawnEvent event){
		
	}	
}
