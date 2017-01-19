package com.minecarts.verrier.tikitoolkit.listener;

import com.minecarts.verrier.tikitoolkit.*;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.entity.Player;

import org.bukkit.inventory.ItemStack;
import java.util.List;
import java.util.UUID;
import java.util.Iterator;

import org.bukkit.event.*;

public class PlayerListener implements Listener {

	TikiToolkit plugin;

	public PlayerListener(TikiToolkit instance) {
		plugin = instance;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission("tiki.update") && TikiToolkit.update) {
			player.sendMessage("An update is available: " + TikiToolkit.name + ", a " + TikiToolkit.type + " for "
					+ TikiToolkit.version + " available at " + TikiToolkit.link);
			player.sendMessage("Type /tiki update if you would like to automatically update.");
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Action action = event.getAction();
		if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
			if (this.doToolCmd(event.getPlayer(), "click_left")) {
				// If we performed a command, cancel it so we don't
				// eat a fish, for example
				event.setCancelled(true);
			}
		} else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
			if (this.doToolCmd(event.getPlayer(), "click_right")) {
				// If we performed a command, cancel it so we don't
				// eat a fish, for example
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onItemHeldChange(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		int slot = event.getNewSlot();
		String name = plugin.config
				.getString("admins." + player.getUniqueId().toString().toLowerCase() + ".slot_" + slot + ".name");
		String type = getToolTypeAtSlot(player, slot);
		if (name != null) {
			// Only display the selected tool message if they have the have the
			// correct item in hand
			if (player.getInventory().getItem(slot).getType() == Material.getMaterial(type)) {
				if (plugin.config.getBoolean(
						"admins." + player.getUniqueId().toString().toLowerCase() + ".selected_msg", true)) {
					player.sendMessage(String.format("Tiki: %s%s%s selected", ChatColor.GOLD, name, ChatColor.WHITE));
				}
			}
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		Runnable setInventory = new setInventory(player.getUniqueId());
		// Since we don't have the actual player object that's going to respawn
		// lets fire off a task to do later? Is this the best way to do it?
		if (!player.isOp() && plugin.config
				.getBoolean("admins." + player.getUniqueId().toString().toLowerCase() + ".op_only", false)) {
			return;
		}
		if (plugin.config.getBoolean("admins." + player.getUniqueId().toString().toLowerCase() + ".respawn_wands",
				false)) {
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, setInventory, 1);
		}
	}

	public class setInventory implements Runnable {
		private UUID playerName;

		public setInventory(UUID uuid) {
			this.playerName = uuid;
		}

		public void run() {
			// Give the players their admin kit on respawn
			Player player = plugin.getServer().getPlayer(playerName);
			player.sendMessage("The tiki gods have restored your admin tools.");
			for (int i = 0; i < 9; i++) {
				String type = getToolTypeAtSlot(player, i);
				if (type != null) {
					// Assign the item
					player.getInventory().setItem(i, new ItemStack(Material.valueOf(type), 1));
				}
			}
		}
	}

	private boolean doToolCmd(Player player, String clickType) {
		int slot = player.getInventory().getHeldItemSlot();
		String type = getToolTypeAtSlot(player, slot);

		if (type != null) {
			// Check to see if the item in the hand is their configured wand
			if (player.getInventory().getItemInMainHand().getType() == Material.getMaterial(type)) {
				// Try to load the commands as a list
				List<String> cmds = this.plugin.config.getStringList(
						"admins." + player.getUniqueId().toString().toLowerCase() + ".slot_" + slot + "." + clickType);
				if (cmds.size() > 0) {
					Iterator<String> itr = cmds.iterator();
					while (itr.hasNext()) {
						String cmd = (String) itr.next();// Try to parse it as
															// an integer, if it
															// is, treat it as a
															// delay
						try {
							Integer delay = Integer.parseInt(cmd);
							ExecuteCommandLater commandExec = new ExecuteCommandLater(player, (String) itr.next());
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, commandExec, delay);
						} catch (Exception e) {
							// Wasn't an int, so it's a string command
							player.chat(cmd);
						}
					}
				} else {
					// Try fetching it as a string
					String cmd = plugin.config
							.getString("admins." + player.getUniqueId().toString().toLowerCase().toLowerCase()
									+ ".slot_" + slot + "." + clickType);
					if (cmd != null) {
						player.chat(cmd);
					}
				}
				return true;
			}
		}
		return false;
	}

	private String getToolTypeAtSlot(Player player, int slot) {
		return this.plugin.config
				.getString("admins." + player.getUniqueId().toString().toLowerCase() + ".slot_" + slot + ".type");
	}

	private class ExecuteCommandLater implements Runnable {
		private String command;
		private Player player;

		public ExecuteCommandLater(Player player, String command) {
			this.command = command;
			this.player = player;
		}

		public void run() {
			this.player.chat(command);
		}
	}
}
