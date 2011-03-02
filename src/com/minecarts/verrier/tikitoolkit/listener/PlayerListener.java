package com.minecarts.verrier.tikitoolkit.listener;

import com.minecarts.verrier.tikitoolkit.*;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.*;
import org.bukkit.entity.Player;

public class PlayerListener extends org.bukkit.event.player.PlayerListener{

	TikiToolkit plugin;
	
	public PlayerListener(TikiToolkit instance)
	{
		plugin = instance;
	}

	
	
	public void onPlayerAnimation(PlayerAnimationEvent event) {
		if (event.getAnimationType() == PlayerAnimationType.ARM_SWING) {
			this.doWandCmd(event.getPlayer(),"click_left");
		}
	}
	
	public void onPlayerItem(PlayerItemEvent event){
		this.doWandCmd(event.getPlayer(),"click_right");
	}
	
	public void onItemHeldChange(PlayerItemHeldEvent event){
		Player player = event.getPlayer();
		int slot = event.getNewSlot();
		
		//Store our new (current) slot
		plugin.playerSlot.put(player.getName(), slot);
		String name = plugin.config.getString("admins."+player.getName()+".slot_"+slot+".name");
		if(name != null){
			player.sendMessage(String.format("Tiki: %s %s %s selected",ChatColor.GOLD,name,ChatColor.WHITE));
		}
	}
	
	public void onPlayerRespawn(PlayerRespawnEvent event){
		
	}
	
	
	
	private void doWandCmd(Player player, String clickType){
		if(!plugin.playerSlot.containsKey(player.getName())){
			player.sendMessage("Please select cycle your wands so we can start tracking them");
			return;
		}
		
		int slot = plugin.playerSlot.get(player.getName());
		String type = plugin.config.getString("admins."+player.getName()+".slot_"+slot+".type","STICK");
		
		//Check to see if the item in the hand is their configured wand
		if (player.getInventory().getItemInHand().getType() == Material.getMaterial(type)){
			//Execute the command
			String cmd = plugin.config.getString("admins."+player.getName()+".slot_"+slot+"."+clickType);
			if(cmd!= null){
				player.performCommand(cmd);
			}
		}
	}
}
