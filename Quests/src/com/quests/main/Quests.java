package com.quests.main;
import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.quests.config.ConfigManager;
import com.quests.cooldown.CooldownManager;
import com.quests.cooldown.CooldownTick;
import com.quests.events.EventHandler;


public class Quests extends JavaPlugin implements Listener{


    private static Quests instance;
    public com.quests.commands.CommandManager CommandManager;
    public ConfigManager configManager;
    public EventHandler eventHandler;
    public CooldownManager cooldownManager;
    public CooldownTick cooldownTick;
	public File ParentFolder;
	public ServerVersion version;
	
	@Override
	public void onEnable(){
		
		this.version = new ServerVersion();
		
		ParentFolder = getDataFolder();
	    instance = this;
		
	    this.reload();
	    
	    eventHandler = new EventHandler();
	    eventHandler.setup();
	    
	    this.cooldownManager = new CooldownManager();

	    this.cooldownTick = new CooldownTick();
	    this.cooldownTick.schedule();
	    
	    this.CommandManager = new com.quests.commands.CommandManager();
	    this.CommandManager.setup(this);

	}
	
	
	@Override
	public void onDisable(){
		Quests.getInstance().configManager.playerData.update();
		this.eventHandler = null;
		HandlerList.getRegisteredListeners(instance);
	}
	
	public static void Log(String msg){
		  Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&c" + Quests.getInstance().getName() + "&7] &c(&7LOG&c): " + msg));
	}
	

	public void reload() {
		this.configManager = new ConfigManager();
		this.configManager.setup(this);
	}
		

	public static Quests getInstance() {
		return instance;
	}
		
	
}
