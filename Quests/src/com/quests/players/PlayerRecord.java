package com.quests.players;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.quests.create.CreationData;
import com.quests.main.Quests;

public class PlayerRecord {

	private UUID user;
	private Map<String, Integer> progress;
	
	public PlayerRecord(UUID user, Map<String, Integer> progress) {
		this.user = user;
		this.progress = progress;
	}

	public UUID getUser() {
		return user;
	}

	public void setUser(UUID user) {
		this.user = user;
	}

	public Map<String, Integer> getProgress() {
		return progress;
	}

	public void setProgress(Map<String, Integer> progress) {
		this.progress = progress;
		this.saveChanges();
	}

	public Integer getQuestProgress(String quest) {
		if(!progress.containsKey(quest)) {
			this.progress.put(quest, 0);
		}
		return this.progress.get(quest);
	}
	
	public void setQuestProgress(String quest, int value) {
		if(!progress.containsKey(quest)) {
			this.progress.put(quest, value);
			return;
		}
		this.progress.put(quest, value);
		this.saveChanges();
	}
	
	public void IncQuestProgress(String quest, int value) {
		if(!progress.containsKey(quest)) {
			this.progress.put(quest, value);
			return;
		}
		int newValue = progress.get(quest) + value;
		this.progress.put(quest, newValue);
		this.saveChanges();
	}
	
	public void resetQuestProgress(String quest) {
		this.progress.put(quest, 0);
	}
	
	public void saveChanges() {
		//Quests.getInstance().configManager.playerData.update();
		for(CreationData i : Quests.getInstance().configManager.quests.getQuests()) {
			if(i.getAMOUNT() <= this.getQuestProgress(i.getNAME())) {
				this.resetQuestProgress(i.getNAME());
				OfflinePlayer player = Bukkit.getOfflinePlayer(this.user);
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), i.getREWARD().replace("%player%", player.getName()));
				if(player.isOnline()) {
					Player onlinePlayer = Bukkit.getPlayer(this.user);
					Quests.getInstance().configManager.messages.send(onlinePlayer, "quest_completed", "%quest%", i.getNAME());
				}
			}
		}
	}
	
}
