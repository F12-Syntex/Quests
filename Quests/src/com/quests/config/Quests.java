package com.quests.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.bukkit.configuration.ConfigurationSection;

import com.quests.create.CreationData;
import com.quests.types.QuestType;

public class Quests extends Config{

	private List<CreationData> quests;
	
	public Quests(String name, double version) {
		super(name, version);
		
		this.quests = new ArrayList<CreationData>();
		
		for(QuestType quest : QuestType.values()) {
			
			String prettyName = WordUtils.capitalize(quest.name().toLowerCase().replace("_", " "));
			
			this.items.add(new ConfigItem("Quests." + prettyName + ".type", quest.name()));	
			this.items.add(new ConfigItem("Quests." + prettyName + ".amount", 10));
			this.items.add(new ConfigItem("Quests." + prettyName + ".reward", "give %player% diamond 10"));	
	
		}
		
	}

	@Override
	public Configuration configuration() {
		// TODO Auto-generated method stub
		return Configuration.QUESTS;
	}
	
	@Override
	public void initialize() {
		
		ConfigurationSection questSection = this.getConfiguration().getConfigurationSection("Quests");
		
		for(String name : questSection.getKeys(false)) {
			
			ConfigurationSection quest = questSection.getConfigurationSection(name);
			
			QuestType type = QuestType.valueOf(quest.getString("type"));
			int amount = quest.getInt("amount");
			String reward = quest.getString("reward");
			
			CreationData data = new CreationData(name, type, reward, amount);
			
			quests.add(data);
		}
		
	}
	
	public void addQuest(CreationData data) {
		this.quests.add(data);
		this.update();
	}
	
	public List<CreationData> getQuests(){
		return this.quests;
	}
	
	private void update() {
		for(CreationData data : this.quests) {
			this.getConfiguration().set("Quests." + data.getNAME() + ".type", data.getTYPE().name());
			this.getConfiguration().set("Quests." + data.getNAME() + ".amount", data.getAMOUNT());
			this.getConfiguration().set("Quests." + data.getNAME() + ".reward", data.getREWARD());
		}
		this.save();
	}

	public boolean questExists(String name) {
		return this.quests.stream().filter(i -> i.getNAME().equalsIgnoreCase(name)).count() > 0;
	}
	
	
}
