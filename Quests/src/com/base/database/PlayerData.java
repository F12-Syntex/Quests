package com.base.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.quests.create.CreationData;
import com.quests.main.Quests;
import com.quests.players.PlayerRecord;
import com.quests.types.QuestType;
import com.quests.utils.StringMinipulation;

public class PlayerData extends MySql{

	private List<PlayerRecord> records;
	
	@Override
	public Databases config() {
		// TODO Auto-generated method stub
		return Databases.PLAYER_DATA;
	}

	@Override
	public void update() throws SQLException {
		
		//save user data
		for(PlayerRecord user : this.records) {
			
			/*
			 * INSERT INTO table (id, name, age) VALUES(1, "A", 19) ON DUPLICATE KEY UPDATE    
				name="A", age=19
			 */
			
			PreparedStatement preparedStatement;
			
			try {

					String keyFields = "";
					String valueFields = "";
					String duplicate = "";
					
					//for(String record : user.getProgress().keySet()) {
					for(QuestType record : QuestType.values()) {
						
					 //String show = record.toUpperCase().replace(" ", "_");
					
					String show = record.name();
					
					String key = WordUtils.capitalizeFully(record.name().toLowerCase().replace("_", " "));
					
					//Quests.Log("data for " + key + " is " + user.getProgress().get(key));
					
					keyFields += show + ", "; 
					valueFields += user.getProgress().get(key) + ", ";
					duplicate += show + "=" + user.getProgress().get(key) + ", ";				 
				
				}
				//}
				
			 String query = "INSERT INTO USERS " + "(UUID, " + StringMinipulation.removeLastCharOptional(keyFields, 2) + ")"
						  + "VALUES(\"" + user.getUser().toString() + "\", " + StringMinipulation.removeLastCharOptional(valueFields, 2) + ") "
					 	  + "ON DUPLICATE KEY UPDATE " + StringMinipulation.removeLastCharOptional(duplicate, 2) + ";";
				
			 //Quests.Log("UPDATE: " + query);
			 
		    preparedStatement = this.getConnection().prepareStatement(query);

			preparedStatement.executeUpdate();
			preparedStatement.close();
			
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
			
		}
		
	}

	@Override
	protected void setup() {
		
		this.records = new ArrayList<>();
		
		String fields = "";
		
		for(QuestType i : QuestType.values()) {
			fields += i.name() + " INT, ";
		}
		
		//create an empty table
		
		
		//Quests.Log("Logging data.");
		
		this.prepareStatement("CREATE TABLE IF NOT EXISTS USERS (UUID VARCHAR(37), " + fields  + "  PRIMARY KEY (UUID))");
		
		//get all the values ( empty if none exist )
		
		//load database
				BukkitRunnable runnable = new BukkitRunnable() {	
					@Override
					public void run() {
						
						PreparedStatement keys;
						try {
							
							keys = getConnection().prepareStatement("SELECT UUID FROM USERS");
							ResultSet results = keys.executeQuery();
					
							Map<String, Integer> questMapping = new HashMap<>();
							
							while(results.next()) {
								
								UUID result = UUID.fromString(results.getString(results.getRow()));
								
								//Quests.Log("getting data from " + result.toString());
								
								PreparedStatement query = getConnection().prepareStatement("SELECT * FROM USERS WHERE UUID=?");
								query.setString(1, result.toString());
								
								ResultSet resultSet = query.executeQuery();
								
								if(!resultSet.next()) {
									//Quests.Log("&cSomething went wrong!");
									Bukkit.getServer().shutdown();
								}
								
								for(QuestType quest : QuestType.values()) {
								
									String key = WordUtils.capitalizeFully(quest.name().toLowerCase().replace("_", " "));
									int value = resultSet.getInt(quest.name());
								
									//Quests.Log("key{" + key + ":" + value + "}");
									
									questMapping.put(key, value);
									
									PlayerRecord record = new PlayerRecord(result, questMapping);
									
									records.add(record);
									
								}
								
								
								query.close();
								resultSet.close();
							}
							
							
							
							results.close();
							keys.close();
						
						} catch (SQLException e) {
							e.printStackTrace();
						}
						
						
					}
				};
				
				runnable.runTaskAsynchronously(Quests.getInstance());
		
		
		
		
		
	}
	
	public List<PlayerRecord> getRecords() {
		return records;
	}

	public void setRecords(List<PlayerRecord> records) {
		this.records = records;
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
