package com.quests.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;

import com.quests.main.Quests;

public class EventHandler {

    public List<SubEvent> events = new ArrayList<SubEvent>();
	
    private Plugin plugin = Quests.getInstance();
    
	public void setup() {
		this.events.add(new InputHandler());
		this.events.add(new PlayerTracker());
		this.events.forEach(i -> plugin.getServer().getPluginManager().registerEvents(i, plugin));
	}
	
}
