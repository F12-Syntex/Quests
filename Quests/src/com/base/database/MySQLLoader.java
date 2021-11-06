package com.base.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import com.quests.config.Config;
import com.quests.config.ConfigItem;
import com.quests.config.Configuration;
import com.quests.config.DatabaseConfig;
import com.quests.main.Quests;

public class MySQLLoader {

	private List<MySql> databases;
	private List<Config> configuration;
	
	public MySQLLoader() {
		this.databases = new ArrayList<>();
		this.configuration = new ArrayList<>();
	}
	
	
	public void loadDatabases() {
		MySql playerData = new PlayerData();
		Config playerDataConfig = this.registerCredentialsConfig(playerData);
		this.configuration.add(playerDataConfig);	
		this.databases.add(playerData);
	}
	
	public void connect() {
		for(MySql database : this.databases) {
			Quests.Log("&aLoading database " + database.config().name());
			try {
				database.loadConfig(Quests.getInstance().configManager.getDatabaseConfig(database.config()));
				database.connect();
				database.setup();
				Quests.Log("&a" + database.config().name().toLowerCase() + ".sql has connected!");
			} catch (SQLException e) {
				Quests.Log("&c" + e.getLocalizedMessage());
				Quests.Log("&cDisabling (" + database.config().name().toLowerCase() + ".sql)");
				//Plugin plugin = Quests.getInstance();
				//Quests.getInstance().getPluginLoader().disablePlugin(plugin);
			}
		}
	}
	
	public void disconnect() {
		for(MySql database : this.databases) {
			database.disconnect();
		}
	}

	public Config registerCredentialsConfig(MySql database) {
		
		Config config = new DatabaseConfig(database.config(), 1.0) {
			
			@Override
			public void initialize() {
				
				ConfigurationSection credentialSection = this.getConfiguration().getConfigurationSection("database.credentials");
				ConfigurationSection updaterSection = this.getConfiguration().getConfigurationSection("database.updater");
				
				MySQLCredentials credentials = new MySQLCredentials(credentialSection.getString("host"),
																	credentialSection.getString("port"),
																	credentialSection.getString("database"),
																	credentialSection.getString("username"),
																	credentialSection.getString("password"));
				
				MySQLUpdater updater = new MySQLUpdater(updaterSection.getLong("ticks"));
				
				this.setCredentials(credentials);
				this.setUpdater(updater);
				
			}

			@Override
			public Configuration configuration() {
				// TODO Auto-generated method stub
				return Configuration.RECORD;
			}
			
		};
		
		
		config.items.add(new ConfigItem("database.credentials.host", "localhost"));
		config.items.add(new ConfigItem("database.credentials.port", "3306"));
		config.items.add(new ConfigItem("database.credentials.database", config.getName()));
		config.items.add(new ConfigItem("database.credentials.username", "root"));
		config.items.add(new ConfigItem("database.credentials.password", ""));
		
		//default time is 72000L or 1 hour.
		config.items.add(new ConfigItem("database.updater.ticks", 20L*60*60));
		
		return config;
		
	}
	
	public List<MySql> getDatabases() {
		return databases;
	}


	public void setDatabases(List<MySql> databases) {
		this.databases = databases;
	}


	public List<Config> getConfiguration() {
		return configuration;
	}


	public void setConfiguration(List<Config> configuration) {
		this.configuration = configuration;
	}

	public MySql getDatabase(Databases config) {
		return this.databases.stream()
							.filter(i -> i.config() == config)
							.findFirst()
							.get();
	}
	
	
}
