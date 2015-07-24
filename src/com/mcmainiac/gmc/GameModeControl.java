package com.mcmainiac.gmc;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GameModeControl extends JavaPlugin implements Listener {
	private final String pre = "§7[§2Game§aMode§fControl§7] "; 
	private boolean forcegmenabled;
	private GameMode forcegmmode;
	
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(pre + "§7Initializing GMC...");
		try {
			initialize();
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage(pre + "§cGameModeControl crashed while initializing!");
			e.printStackTrace();
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		Bukkit.getConsoleSender().sendMessage(pre + "§aPlugin enabled!");
		Bukkit.getConsoleSender().sendMessage(pre + "§7Note: this is a §9[BETA]");
		Bukkit.getConsoleSender().sendMessage(pre + "§7If you need help with the commands");
		Bukkit.getConsoleSender().sendMessage(pre + "§7use §a/gmh§7.");
	}
	
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(pre + "§cPlugin disabled!");
	}
	
	public void initialize() throws IOException {
		this.saveDefaultConfig();
		Bukkit.getPluginManager().registerEvents(this, this);
		if (this.getConfig().getBoolean("force-gamemode.enable")) {
			this.forcegmenabled = true;
			int gm = this.getConfig().getInt("force-gamemode.mode");
			if (gm > -1 && gm < 4) {
				Bukkit.getConsoleSender().sendMessage(pre + "§7Forcing game mode §b" + gm + " §7on player join.");
			} else {
				Bukkit.getConsoleSender().sendMessage(pre + "§cInvalid game mode used in config: §b" + gm + "§c!");
				Bukkit.getConsoleSender().sendMessage(pre + "§cUsing default game mode §b0§7.");
				gm = 0;
			}
			this.forcegmmode = getGMbyInt(gm);
			this.getServer().setDefaultGameMode(getGMbyInt(gm));
		} else {
			this.forcegmenabled = false;
			this.forcegmmode = GameMode.SURVIVAL;
			Bukkit.getConsoleSender().sendMessage(pre + "§7Forcing game mode disabled.");
		}
	}
	
	public GameMode getGMbyInt(int gm) {
		switch (gm) {
		case 0: return GameMode.SURVIVAL;
		case 1: return GameMode.CREATIVE;
		case 2: return GameMode.ADVENTURE;
		case 3: return GameMode.SPECTATOR;
		default: return null;
		}
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		if (!this.forcegmenabled) return;
		Player p = e.getPlayer();
		if (p.getGameMode() != this.forcegmmode) p.setGameMode(this.forcegmmode);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        //survival
		if (cmdLabel.equalsIgnoreCase("gm0") || cmdLabel.equalsIgnoreCase("survival") || cmdLabel.equalsIgnoreCase("sur")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length == 0) {
					if (p.hasPermission("gmc.gm0.self") || p.hasPermission("gmc.gm0")) {
						survival("self", sender, null);
					} else {
						p.sendMessage(m("Other.no permission", p, sender));
					}
				} else if (args.length == 1) {
					if (p.hasPermission("gmc.gm0.others") || p.hasPermission("gmc.gm0")) {
						survival("other", sender, getPlayer(args[0]));
					} else {
						p.sendMessage(m("Other.no permission", p, sender));
					}
				} else {
					p.sendMessage(pre + "§cUsage: /survival;/gm0 §7[player]");
        			return true;
				}
			} else {
				if (args.length == 0) {
					sender.sendMessage("Please type in a player!");
    				return true;
				} else if (args.length == 1) {
					survival("other", sender, getPlayer(args[0]));
				} else {
					sender.sendMessage("[GameModeControl] Usage: /survival;/gm0 <player>");
    				return true;
				}
			}
		} else 
        
        //creative
        if (cmdLabel.equalsIgnoreCase("gm1") || cmdLabel.equalsIgnoreCase("creative") || cmdLabel.equalsIgnoreCase("cre") || cmdLabel.equalsIgnoreCase("gimmethedamncreativemode")) {
        	if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length == 0) {
					if (p.hasPermission("gmc.gm1.self") || p.hasPermission("gmc.gm1")) {
						creative("self", sender, null);
					} else {
						p.sendMessage(m("Other.no permission", p, sender));
					}
				} else if (args.length == 1) {
					if (p.hasPermission("gmc.gm1.others") || p.hasPermission("gmc.gm1")) {
						creative("other", sender, getPlayer(args[0]));
					} else {
						p.sendMessage(m("Other.no permission", p, sender));
					}
				} else {
					p.sendMessage(pre + "§cUsage: /creative;/gm1 §7[player]");
        			return true;
				}
			} else {
				if (args.length == 0) {
					sender.sendMessage("Please type in a player!");
    				return true;
				} else if (args.length == 1) {
					creative("other", sender, null);
				} else {
					sender.sendMessage("Usage: /creative;/gm1 <player>");
    				return true;
				}
			}
        } else 
        
        //adventure
        if (cmdLabel.equalsIgnoreCase("gm2") || cmdLabel.equalsIgnoreCase("adventure") || cmdLabel.equalsIgnoreCase("adv")) {
        	if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length == 0) {
					if (p.hasPermission("gmc.gm2.self") || p.hasPermission("gmc.gm2")) {
						adventure("self", sender, null);
					} else {
						p.sendMessage(m("Other.no permission", p, sender));
					}
				} else if (args.length == 1) {
					if (p.hasPermission("gmc.gm2.others") || p.hasPermission("gmc.gm2")) {
						adventure("other", sender, getPlayer(args[0]));
					} else {
						p.sendMessage(m("Other.no permission", p, sender));
					}
				} else {
					p.sendMessage(pre + "§cUsage: /adventure;/gm2 §7[player]");
        			return true;
				}
			} else {
				if (args.length == 0) {
					sender.sendMessage("Please type in a player!");
    				return true;
				} else if (args.length == 1) {
					adventure("other", sender, null);
				} else {
					sender.sendMessage("Usage: /adventure;/gm2 <player>");
    				return true;
				}
			}
        } else 
        
        //spectator
        if (cmdLabel.equalsIgnoreCase("gm3") || cmdLabel.equalsIgnoreCase("spectator") || cmdLabel.equalsIgnoreCase("spec")) {
        	if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length == 0) {
					if (p.hasPermission("gmc.gm3.self") || p.hasPermission("gmc.gm3")) {
						spectator("self", sender, null);
					} else {
						p.sendMessage(m("Other.no permission", p, sender));
					}
				} else if (args.length == 1) {
					if (p.hasPermission("gmc.gm3.others") || p.hasPermission("gmc.gm3")) {
						spectator("other", sender, getPlayer(args[0]));
					} else {
						p.sendMessage(m("Other.no permission", p, sender));
					}
				} else {
					p.sendMessage(pre + "§cUsage: /spectator;/gm3 §7[player]");
        			return true;
				}
			} else {
				if (args.length == 0) {
					sender.sendMessage("Please type in a player!");
    				return true;
				} else if (args.length == 1) {
					spectator("other", sender, null);
				} else {
					sender.sendMessage("Usage: /spectator;/gm3 <player>");
    				return true;
				}
			}
        } else 
        
        //gm
        if (cmdLabel.equalsIgnoreCase("gm")) {
        	if (sender instanceof Player) {
        		Player p = (Player) sender;
        		if (args.length == 0) {
        			p.sendMessage(pre + "§cPlease choose a game mode!");
        			return true;
        		} else if (args.length == 1) {
        			if (args[0].equalsIgnoreCase("survival") || args[0].equals("0")) {
        				if (p.hasPermission("gmc.gm0.self") || p.hasPermission("gmc.gm0")) {
            				survival("self", sender, null);
        				} else {
        					p.sendMessage(m("Other.no permission", p, sender));
        				}
        			} else if (args[0].equalsIgnoreCase("creative") || args[0].equals("1")) {
        				if (p.hasPermission("gmc.gm1.self") || p.hasPermission("gmc.gm1")) {
            				creative("self", sender, null);
        				} else {
        					p.sendMessage(m("Other.no permission", p, sender));
        				}
        			} else if (args[0].equalsIgnoreCase("adventure") || args[0].equals("2")) {
        				if (p.hasPermission("gmc.gm2.self") || p.hasPermission("gmc.gm2")) {
            				adventure("self", sender, null);
        				} else {
        					p.sendMessage(m("Other.no permission", p, sender));
        				}
        			} else if (args[0].equalsIgnoreCase("spectator") || args[0].equals("3")) {
        				if (p.hasPermission("gmc.gm3.self") || p.hasPermission("gmc.gm3")) {
        					spectator("self", sender, null);
        				} else {
        					p.sendMessage(m("Other.no permission", p, sender));
        				}
        			} else {
            			p.sendMessage(pre + "§cUsage: /gm <game mode> [player]");
        			}
        			return true;
        		} else if (args.length == 2) {
        			if (args[0].equalsIgnoreCase("survival") || args[0].equals("0")) {
        				if (p.hasPermission("gmc.gm0.other") || p.hasPermission("gmc.gm0") || p.hasPermission("gmc.*")) {
            				survival("other", sender, getPlayer(args[1]));
        				} else {
        					p.sendMessage(m("Other.no permission", p, sender));
        				}
        			} else if (args[0].equalsIgnoreCase("creative") || args[0].equals("1")) {
        				if (p.hasPermission("gmc.gm1.other") || p.hasPermission("gmc.gm1") || p.hasPermission("gmc.*")) {
            				creative("other", sender, getPlayer(args[1]));
        				} else {
        					p.sendMessage(m("Other.no permission", p, sender));
        				}
        			} else if (args[0].equalsIgnoreCase("adventure") || args[0].equals("2")) {
        				if (p.hasPermission("gmc.gm2.other") || p.hasPermission("gmc.gm2") || p.hasPermission("gmc.*")) {
            				adventure("other", sender, getPlayer(args[1]));
        				} else {
        					p.sendMessage(m("Other.no permission", p, sender));
        				}
        			} else if (args[0].equalsIgnoreCase("spectator") || args[0].equals("3")) {
        				if (p.hasPermission("gmc.gm3.other") || p.hasPermission("gmc.gm3") || p.hasPermission("gmc.*")) {
        					spectator("other", sender, getPlayer(args[1]));
        				} else {
        					p.sendMessage(m("Other.no permission", p, sender));
        				}
        			} else {
            			p.sendMessage(pre + "§cUsage: /gm <game mode> [player]");
        			}
        			return true;
        		} else {
        			p.sendMessage(pre + "§cUsage: /gm <game mode> [player]");
        		}
        		return true;
        	} else {
        		if (args.length == 0) {
        			Bukkit.getConsoleSender().sendMessage(pre + "§cPlease choose a game mode and a player!");
        			return true;
        		} else if (args.length == 1) {
        			Bukkit.getConsoleSender().sendMessage(pre + "§cPlease type in a player!");
        			return true;
        		} else if (args.length == 2) {
        			if (args[0].equalsIgnoreCase("survival") || args[0].equals("0")) {
        				survival("other", sender, getPlayer(args[1]));
        			} else if (args[0].equalsIgnoreCase("creative") || args[0].equals("1")) {
        				creative("other", sender, getPlayer(args[1]));
        			} else if (args[0].equalsIgnoreCase("adventure") || args[0].equals("2")) {
        				adventure("other", sender, getPlayer(args[1]));
        			} else if (args[0].equalsIgnoreCase("spectator") || args[0].equals("3")) {
        				spectator("other", sender, getPlayer(args[1]));
        			} else {
            			Bukkit.getConsoleSender().sendMessage(pre + "§cUsage: /gm <game mode> <player>");
        			}
        			return true;
        		} else {
        			Bukkit.getConsoleSender().sendMessage(pre + "§cUsage: /gm <game mode> <player>");
        			return true;
        		}
        	}
        } else 

        //help
        if (cmdLabel.equalsIgnoreCase("gmh")) {
        	if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("gmc.help") || p.hasPermission("gmc.*")) {
                	//if (args.length == 0) {
                        p.sendMessage("§f----- §7[§2Game§aMode§fControl§7] §f-----");
                		p.sendMessage("§2[] §6= optional; §a<> §6= required");
                		p.sendMessage("§7- §a/survival§6;§a/gm0 §2[player]§7: set the game mode to survival");
                		p.sendMessage("§7- §a/creative§6;§a/gm1 §2[player]§7: set the game mode to creative");
                		p.sendMessage("§7- §a/adventure§6;§a/gm2 §2[player]§7: set the game mode to adventure");
                		p.sendMessage("§7- §a/spectator§6;§a/gm3 §2[player]§7: set the game mode to spectator");
                		p.sendMessage("§7- §a/gm <game mode> §2[player]§7: set the game mode of [player]");
                		p.sendMessage("§7- §a/gmh§7: shows these help pages");
                		p.sendMessage("§7- §a/gmi§7: shows information about GMC");
                        p.sendMessage("§7- §a/gmr§7: reloads the config");
                		p.sendMessage("§f------ §3[ §7page  (1/1) §3]§f ------");
            			return true;
            			/*} else if (args.length == 1) {
                            p.sendMessage("§f----- §7[§2Game§aMode§fControl§7] §f-----");
                    		p.sendMessage("§2variants:");
                    		p.sendMessage("§b- /survival §2[player]");
                    		p.sendMessage("§b- /gm0 §2[player]");
                    		p.sendMessage("§b- /gm 0 §2[player]");
                    		p.sendMessage("§7Changes the game mode of the specific player,");
                    		p.sendMessage("§7if no player is given, it changes your");
                            p.sendMessage("§7game mode.");
                    		p.sendMessage("§f------ §3[§7command: /gm0§3]§f ------");
                		} else {
                			p.sendMessage(pre + "§cCommand not found: §6" + args[0]);
                			return true;
                		}
                	} else {
            			p.sendMessage(pre + "§cUsage: /gmh");
            			return true;
                	}*/
                } else {
					p.sendMessage(m("Other.no permission", p, sender));
        			return true;
                }
            } else {
        		Bukkit.getConsoleSender().sendMessage("§f----- §7[§2Game§aMode§fControl§7] §f-----");
        		Bukkit.getConsoleSender().sendMessage("§2[] §6= optional; §a<> §6= required");
        		Bukkit.getConsoleSender().sendMessage("§7- §a/survival§6;§a/gm0 §2[player]§7: set the game mode to survival");
        		Bukkit.getConsoleSender().sendMessage("§7- §a/creative§6;§a/gm1 §2[player]§7: set the game mode to creative");
        		Bukkit.getConsoleSender().sendMessage("§7- §a/adventure§6;§a/gm2 §2[player]§7: set the game mode to adventure");
        		Bukkit.getConsoleSender().sendMessage("§7- §a/spectator§6;§a/gm3 §2[player]§7: set the game mode to spectator");
        		Bukkit.getConsoleSender().sendMessage("§7- §a/gm <game mode> §2[player]§7: set the game mode of [player]");
        		Bukkit.getConsoleSender().sendMessage("§7- §a/gmh§7: shows these help pages");
        		Bukkit.getConsoleSender().sendMessage("§7- §a/gmi§7: shows information about GMC");
        		Bukkit.getConsoleSender().sendMessage("§7- §a/gmr§7: reloads the config");
        		Bukkit.getConsoleSender().sendMessage("§f------ §3[ §7page  (1/1) §3]§f ------");
				return true;
            }
   	    } else 
        
        //reload
        if (cmdLabel.equalsIgnoreCase("gmr")) {
        	if (sender instanceof Player) {
        		Player p = (Player) sender;
        		if (p.hasPermission("gmc.reload") || p.hasPermission("gmc.*")) {
                    this.reloadConfig();
                    p.sendMessage(pre + "§aThe config has been successfully reloaded!");
            		return true;
        		} else {
					p.sendMessage(m("Other.no permission", p, sender));
        		}
        	} else {
            	this.getServer().reload();
            	this.reloadConfig();
        		Bukkit.getConsoleSender().sendMessage(pre + "§aThe config has been successfully reloaded!");
    			return true;
        	}
        } else 
        
        // info
        if (cmdLabel.equalsIgnoreCase("gmi")) {
        	if (sender instanceof Player) {
        		Player p = (Player) sender;
        		if (p.hasPermission("gmc.info") || p.hasPermission("gmc.*")) {
        			p.sendMessage("§f----- §7[§2Game§aMode§fControl§7] §f-----");
        			p.sendMessage("§aVersion§7: §9[Beta] §f1.2.1");
        			p.sendMessage("§aAuthor§7: §fMCMainiac");
        			p.sendMessage("§aWebsite§7: §5§nhttp://bit.ly/MC-GMC");
        			p.sendMessage("§f-----------------------------");
        			return true;
        		} else {
        			p.sendMessage(m("Other.no permission", p, sender));
        			return true;
        		}
        	} else {
        		Bukkit.getConsoleSender().sendMessage("§f----- §7[§2Game§aMode§fControl§7] §f-----");
        		Bukkit.getConsoleSender().sendMessage("§aVersion§7: §9[Beta] §f1.2.1");
        		Bukkit.getConsoleSender().sendMessage("§aAuthor§7: §fMCMainiac");
        		Bukkit.getConsoleSender().sendMessage("§aWebsite§7: §5§nhttp://bit.ly/MC-GMC");
        		Bukkit.getConsoleSender().sendMessage("§f-----------------------------");
        		return true;
        	}
        }
        return true;
	}
	
	public Player getPlayer(String name) {
		for (Player p: Bukkit.getOnlinePlayers()) {
			if (p.getName() == name) {
				return p;
			}
		}
		return null;
	}
	
	public void survival(String mode, CommandSender sender, Player p2) {
		if (mode == "self") {
			Player p = (Player) sender;
			p.setGameMode(GameMode.SURVIVAL);
			p.sendMessage(m("Survival.self", p2, sender));
		} else if (mode == "other") {
			Bukkit.broadcastMessage(p2.getName());
			/*if (p2 != null && p2.isOnline()) {
				p2.setGameMode(GameMode.SURVIVAL);
				p2.sendMessage(m("Survival.from", p2, sender));
				sender.sendMessage(m("Survival.to", p2, sender));
			} else {
				sender.sendMessage(m("Other.not found", p2, sender));
			}*/
		}
	}
	
	public void creative(String mode, CommandSender sender, Player p2) {
		if (mode == "self") {
			Player p = (Player) sender;
			p.setGameMode(GameMode.CREATIVE);
			p.sendMessage(m("Creative.self", p2, sender));
		} else if (mode == "other") {
			if (p2 != null && p2.isOnline()) {
				p2.setGameMode(GameMode.CREATIVE);
				p2.sendMessage(m("Creative.from", p2, sender));
				sender.sendMessage(m("Creative.to", p2, sender));
			} else {
				sender.sendMessage(m("Other.not found", p2, sender));
			}
		}
	}
	
	public void adventure(String mode, CommandSender sender, Player p2) {
		if (mode == "self") {
			Player p = (Player) sender;
			p.setGameMode(GameMode.ADVENTURE);
			p.sendMessage(m("Adventure.self", p2, sender));
		} else if (mode == "other") {
			if (p2 != null && p2.isOnline()) {
				p2.setGameMode(GameMode.ADVENTURE);
				p2.sendMessage(m("Adventure.from", p2, sender));
				sender.sendMessage(m("Adventure.to", p2, sender));
			} else {
				sender.sendMessage(m("Other.not found", p2, sender));
			}
		}
	}
	
	public void spectator(String mode, CommandSender sender, Player p2) {
		if (mode == "self") {
			Player p = (Player) sender;
			p.setGameMode(GameMode.SPECTATOR);
			p.sendMessage(m("Spectator.self", p2, sender));
		} else if (mode == "other") {
			if (p2 != null && p2.isOnline()) {
				p2.setGameMode(GameMode.SPECTATOR);
				p2.sendMessage(m("Spectator.from", p2, sender));
				sender.sendMessage(m("Spectator.to", p2, sender));
			} else {
				sender.sendMessage(m("Other.not found", p2, sender));
			}
		}
	}
	
	public String m(String path, Player p, CommandSender sender) {
		String m = this.getConfig().getString(path);
		m = m.replaceAll("&", "§");
		if (p != null) m = m.replaceAll("%player", p.getName());
		if (sender instanceof Player) {
			m = m.replaceAll("%sender", sender.getName());
		} else {
			m = m.replaceAll("%sender", "the console");
		}
		return m;
	}
}
