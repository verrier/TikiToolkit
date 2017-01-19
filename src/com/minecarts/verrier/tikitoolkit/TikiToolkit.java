package com.minecarts.verrier.tikitoolkit;

import java.util.logging.Logger;

import com.minecarts.verrier.tikitoolkit.Updater.ReleaseType;
import com.minecarts.verrier.tikitoolkit.helper.StringHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

import java.util.HashMap;

import com.minecarts.verrier.tikitoolkit.listener.*;

public class TikiToolkit extends JavaPlugin {

	private PlayerListener playerListener;
	private EntityListener entityListener;

	public HashMap<String, Integer> playerSlot;
	public Configuration config;

	public final Logger log = Logger.getLogger("Minecraft");

	public static boolean update = false;
	public static String name = "";
	public static ReleaseType type = null;
	public static String version = "";
	public static String link = "";
	private final int id = 30761;

	public void onEnable() {
		this.saveDefaultConfig();

		PluginManager pm = getServer().getPluginManager();

		// Set up our listeners
		playerListener = new PlayerListener(this);
		entityListener = new EntityListener(this);

		pm.registerEvents(this.playerListener, this);
		pm.registerEvents(this.entityListener, this);

		this.config = getConfig();

		PluginDescriptionFile pdf = getDescription();
		this.log.info("[" + pdf.getName() + "] version " + pdf.getVersion() + " enabled.");

		Updater updater = new Updater(this, id, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false); // Start
																										// Updater
																										// but
																										// just
																										// do
																										// a
																										// version
																										// check
		update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE; // Determine
																				// if
																				// there
																				// is
																				// an
																				// update
																				// ready
																				// for
																				// us
		name = updater.getLatestName(); // Get the latest name
		version = updater.getLatestGameVersion(); // Get the latest game version
		type = updater.getLatestType(); // Get the latest file's type
		link = updater.getLatestFileLink(); // Get the latest link
	}

	public void onDisable() {

	}

	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		if (cmdLabel.equalsIgnoreCase("tiki")) {
			if (args.length > 0) {
				// Reload the config
				if (args[0].equalsIgnoreCase("reload")) {
					// If a player issued the command
					if (sender instanceof Player) {
						// Check to see if they're an op
						if (((Player) sender).isOp()) {
							this.reloadConfig();
							sender.sendMessage("TikiToolkit config reloaded.");
						}
					} else {
						// Console issued the command
						this.reloadConfig();
					}
					log.info("TikiToolkit config reloaded.");
					return true;
					// Identify what item you have in hand
				} else if (args[0].equalsIgnoreCase("identify")) {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						player.sendMessage("Tiki: You are holding: " + ChatColor.GOLD
								+ player.getInventory().getItemInMainHand().getType().name());
						return true;
					}
					// Give the player their tools
				} else if (args[0].equalsIgnoreCase("tools")) {
					if (sender instanceof Player) {
						Player player = (Player) sender;

						if (!player.isOp() && config.getBoolean(
								"admins." + player.getUniqueId().toString().toLowerCase().toString() + ".op_only",
								false)) {
							return false;
						}

						Runnable setInventory = playerListener.new setInventory(player.getUniqueId());
						getServer().getScheduler().scheduleSyncDelayedTask(this, setInventory, 1);
						return true;
					}
				} else if (args[0].equalsIgnoreCase("bind")) {
					if (args.length >= 2) {
						if (sender instanceof Player) {
							Player player = (Player) sender;
							config.set(
									"admins." + player.getUniqueId().toString().toLowerCase() + ".slot_"
											+ player.getInventory().getHeldItemSlot() + ".type",
									player.getInventory().getItemInMainHand().getType().toString());

							if (args[1].equalsIgnoreCase("click_left") || args[1].equalsIgnoreCase("click_right")) {
								// Check to see if there are multiple args after
								// this, and if so, try to figure out how to
								// break it apart
								// for now, we're just going to assume it's a
								// single command (eg no chaining (yet))

								String combinedArgs = StringHelper.join(args, 2);

								config.set(
										"admins." + player.getUniqueId().toString().toLowerCase() + ".slot_"
												+ player.getInventory().getHeldItemSlot() + "." + args[1].toLowerCase(),
										combinedArgs);
							} else {
								player.sendMessage("Please use click_left or click_right to define the click action.");
							}
							this.saveConfig();
							return true;
						}
					}
				} else if (args[0].equalsIgnoreCase("update")) {
					if (sender.hasPermission("tiki.update")) {
						new Updater(this, id, this.getFile(), Updater.UpdateType.NO_VERSION_CHECK, true); // Go
																											// straight
																											// to
																											// downloading,
																											// and
																											// announce
																											// progress
																											// to
																											// console.
						sender.sendMessage("The Update is downloading");
						return true;
					}
				}
			}
		}
		return false;
	}
}
