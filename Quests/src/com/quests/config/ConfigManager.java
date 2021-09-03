package com.quests.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {

    public ArrayList<Config> config = new ArrayList<Config>();
	
    public Messages messages;
    public Permissions permissions;
    public Cooldown cooldown;
    public Configs configs;
    public Quests quests;
    public PlayerData playerData;
    
    public void setup(Plugin plugin) {
    
    	//System.out.println("setup has been called by " + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() + " line number: " + Thread.currentThread().getStackTrace()[2].getLineNumber());
		
    	this.messages = new Messages("messages", 1.7);
    	this.permissions = new Permissions("permissions", 1.7);
    	this.cooldown = new Cooldown("cooldown", 1.4);
    	this.configs = new Configs("configs", 1.4);
    	this.quests = new Quests("quests", 1.0);
    	this.playerData = new PlayerData("players", 1.0);
    	
    	this.config.clear();

    	this.config.add(messages);
    	this.config.add(permissions);
    	this.config.add(cooldown);
    	this.config.add(configs);
    	this.config.add(quests);
    	this.config.add(playerData);
    	
    	this.configure(plugin, config);
    }

    public void configure(Plugin plugin, List<Config> configs) {
    	
    	for(int i = 0; i < config.size(); i++) {
        	
    		Config currentConfig = configs.get(i);
    		
    		currentConfig.setup();
    	
    		if(currentConfig.getConfiguration().contains("identity.version") && (currentConfig.getConfiguration().getDouble("identity.version") == currentConfig.getVerison())) {
    			currentConfig.initialize();
        		continue;
    		}
    		
	    		File file = currentConfig.backup();
	 
    			new File(plugin.getDataFolder(), currentConfig.getName() + ".yml").delete();
	    		plugin.saveResource(currentConfig.getName() + ".yml", false);

       			if(file != null) {
   					final FileConfiguration oldConfig = YamlConfiguration.loadConfiguration(file);
   					
   					oldConfig.getKeys(true).forEach(o -> {
   						if(currentConfig.getConfiguration().contains(o)) {
   							currentConfig.getConfiguration().set(o, oldConfig.get(o));
   						}
   					});
   		    
   					currentConfig.setDefault();
   					
   					currentConfig.getConfiguration().set("identity.version", currentConfig.getVerison());
   					
   					currentConfig.save();
   		    		currentConfig.initialize();
   		    		
   				}

    	}
    }
    
    
    public Config getConfig(Configuration configuration) {
    	return config.stream().filter(i -> i.configuration() == configuration).findFirst().get();
    }

}
