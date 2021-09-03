package com.quests.events;

import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.quests.config.PlayerData;
import com.quests.create.CreationData;
import com.quests.main.Quests;
import com.quests.racetrack.RaceTrack;
import com.quests.types.QuestType;

public class PlayerTracker extends SubEvent{

	private PlayerData playerRecord;
	private RaceTrack raceTrack;
	
	public PlayerTracker() {
		playerRecord = Quests.getInstance().configManager.playerData;
		this.raceTrack = new RaceTrack();
	}
	
	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Tracks user interactions";
	}
	
	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "handles user interactions.";
	}

	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		for(CreationData filteredQuests : Quests.getInstance().configManager.quests.getQuests()) {
			if(filteredQuests.getTYPE() == QuestType.BLOCK_BREAK) {
				playerRecord.getPlayer(e.getPlayer()).IncQuestProgress(filteredQuests.getNAME(), 1);		
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		for(CreationData filteredQuests : Quests.getInstance().configManager.quests.getQuests()) {
			if(filteredQuests.getTYPE() == QuestType.BLOCK_PLACE) {
				playerRecord.getPlayer(e.getPlayer()).IncQuestProgress(filteredQuests.getNAME(), 1);		
			}
		}
	}
	
	@EventHandler
	public void onCommandRun(PlayerCommandPreprocessEvent e) {
		for(CreationData filteredQuests : Quests.getInstance().configManager.quests.getQuests()) {
			if(filteredQuests.getTYPE() == QuestType.COMMAND_RUN) {
				playerRecord.getPlayer(e.getPlayer()).IncQuestProgress(filteredQuests.getNAME(), 1);		
			}
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		raceTrack.updateUser(e.getPlayer());
		for(CreationData filteredQuests : Quests.getInstance().configManager.quests.getQuests()) {
			if(filteredQuests.getTYPE() == QuestType.RUN_DISTANCE) {
				playerRecord.getPlayer(e.getPlayer()).IncQuestProgress(filteredQuests.getNAME(), raceTrack.getRunningDistance(e.getPlayer()));		
			}
		}
	}
	
	@EventHandler
	public void onPlayerKillMob(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			if(e.getEntity() instanceof Zombie) {
				if(e.getEntity().isDead()) {
					System.out.println("User has killed mob");
					for(CreationData filteredQuests : Quests.getInstance().configManager.quests.getQuests()) {
						if(filteredQuests.getTYPE() == QuestType.KILL_MOB) {
							playerRecord.getPlayer((Player)e.getDamager()).IncQuestProgress(filteredQuests.getNAME(), 1);		
						}
					}
				}
			}
		}
	}
	
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
		if(e.getEntity().getKiller() instanceof Player) {
			if(e.getEntity() instanceof Mob) {
				if(e.getEntity().isDead()) {
					for(CreationData filteredQuests : Quests.getInstance().configManager.quests.getQuests()) {
						if(filteredQuests.getTYPE() == QuestType.KILL_MOB) {
							playerRecord.getPlayer((Player)e.getEntity().getKiller()).IncQuestProgress(filteredQuests.getNAME(), 1);		
						}
					}
				}
			}
		}
    }
	
	
}
