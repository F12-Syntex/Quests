package com.quests.config;

import com.base.database.Databases;
import com.base.database.MySQLCredentials;
import com.base.database.MySQLUpdater;

public abstract class DatabaseConfig extends Config{

	private MySQLCredentials credentials;
	private MySQLUpdater updater;
	
	public DatabaseConfig(Databases name, double version) {
		super(name.name().toLowerCase(), version);
	}

	public MySQLCredentials getCredentials() {
		return credentials;
	}

	public void setCredentials(MySQLCredentials credentials) {
		this.credentials = credentials;
	}

	public MySQLUpdater getUpdater() {
		return updater;
	}

	public void setUpdater(MySQLUpdater updater) {
		this.updater = updater;
	}
		
}
