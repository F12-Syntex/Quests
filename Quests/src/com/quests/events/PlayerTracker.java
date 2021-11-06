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

import com.base.database.PlayerData;
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
		this.incQuest(QuestType.BLOCK_BREAK, e.getPlayer());
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		this.incQuest(QuestType.BLOCK_PLACE, e.getPlayer());
	}
	
	@EventHandler
	public void onCommandRun(PlayerCommandPreprocessEvent e) {
		this.incQuest(QuestType.COMMAND_RUN, e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		raceTrack.updateUser(e.getPlayer());
		this.incQuest(QuestType.RUN_DISTANCE, e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerKillMob(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			if(e.getEntity() instanceof Zombie) {
				if(e.getEntity().isDead()) {
					System.out.println("User has killed mob");
					this.incQuest(QuestType.KILL_MOB, (Player)e.getDamager());
				}
			}
		}
	}
	
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
		if(e.getEntity().getKiller() instanceof Player) {
			if(e.getEntity() instanceof Mob) {
				if(e.getEntity().isDead()) {
					this.incQuest(QuestType.KILL_MOB, (Player)e.getEntity().getKiller());
				}
			}
		}
    }
    
    public void incQuest(QuestType quest, Player player) {
		for(CreationData filteredQuests : Quests.getInstance().configManager.quests.getQuests()) {
			if(filteredQuests.getTYPE() == quest) {
				playerRecord.getPlayer(player).IncQuestProgress(filteredQuests.getNAME(), 1);		
			}
		}
    }
	
	
}
