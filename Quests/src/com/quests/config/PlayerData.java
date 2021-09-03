package com.quests.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.quests.create.CreationData;
import com.quests.players.PlayerRecord;

public class PlayerData extends Config{

	private List<PlayerRecord> records;
	
	public PlayerData(String name, double version) {
		super(name, version);
		this.records = new ArrayList<PlayerRecord>();
	}

	@Override
	public Configuration configuration() {
		// TODO Auto-generated method stub
		return Configuration.RECORD;
	}
	
	@Override
	public void initialize() {
		ConfigurationSection uuids = this.getConfiguration();
		
		for(String uuid : uuids.getKeys(false)) {
			if(uuid.equalsIgnoreCase("identity")) continue;
			
			Map<String, Integer> amounts = new HashMap<String, Integer>();
			
		    for(CreationData quest : com.quests.main.Quests.getInstance().configManager.quests.getQuests()) {
		    	if(this.getConfiguration().contains(uuid + "." + quest.getNAME())) {
			    	amounts.put(quest.getNAME(), this.getConfiguration().getInt(uuid + "." + quest.getNAME()));	
		    	}else {
		    		amounts.put(quest.getNAME(), 0);
		    	}
		    }
			
			PlayerRecord record = new PlayerRecord(UUID.fromString(uuid), amounts);
			records.add(record);
		}
	}
	
	public void update() {
		for(PlayerRecord record : records) {
			Map<String, Integer> records = record.getProgress();
			
		    for(CreationData quest : com.quests.main.Quests.getInstance().configManager.quests.getQuests()) {
		    	
		    	if(!records.containsKey(quest.getNAME())) {
		    		records.put(quest.getNAME(), 0);
		    	}
		    	
		    	this.getConfiguration().set(record.getUser().toString() + "." + quest.getNAME(), records.get(quest.getNAME()));
		    }
		
		}
		this.save();
	}

	public PlayerRecord getPlayer(Player player) {
		
		if(playerExists(player.getUniqueId())) {
			return this.records.stream().filter(i -> i.getUser().compareTo(player.getUniqueId()) == 0).findFirst().get();
		}
		
		return this.createPlayer(player.getUniqueId());
	}
	
	public PlayerRecord createPlayer(UUID user) {
		
		Map<String, Integer> defaultProgress = new HashMap<String, Integer>();
		
	    for(CreationData quest : com.quests.main.Quests.getInstance().configManager.quests.getQuests()) {
			defaultProgress.put(quest.getNAME(), 0);
		}
		
		PlayerRecord playerRecord = new PlayerRecord(user, defaultProgress);
		this.records.add(playerRecord);
		return playerRecord;
	}
	
	public boolean playerExists(UUID uuid) {
		return this.records.stream().filter(i -> i.getUser().compareTo(uuid) == 0).count() > 0;
	}
	
}
