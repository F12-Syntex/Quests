package com.quests.racetrack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RaceTrack {
	
	private Map<UUID, Location> lastLoc = new HashMap<UUID, Location>();
	private Map<UUID, Integer> blockRan = new HashMap<UUID, Integer>();

	public void updateUser(Player player) {
		
		if(lastLoc.containsKey(player.getUniqueId())) {
			Location location = player.getLocation();
			Location saved = lastLoc.get(player.getUniqueId());
			
			if(location.getWorld().getName().equals(saved.getWorld().getName())) {
				
				double x = location.getX();
				double x2 = saved.getX();
				
				double z = location.getZ();
				double z2 = saved.getZ();
				
				double dx = (x2 - x) * (x2 - x);
				double dz = (z2 - z) * (z2 - z);
				
				double difference = Math.sqrt(dx + dz);
				
				if(difference >= 1) {
					blockRan.put(player.getUniqueId(), (int)difference);
				}else {
					return;
				}
			}
			
		}
		
		lastLoc.put(player.getUniqueId(), player.getLocation());
	}
	
	public int getRunningDistance(Player player) {
		if(this.blockRan.containsKey(player.getUniqueId())){
			int ran = this.blockRan.get(player.getUniqueId());
			if(ran >= 1){
				blockRan.put(player.getUniqueId(), 0);
				return ran;
			}
		}
		return 0;
	}
	
	
}
