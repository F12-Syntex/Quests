package com.base.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.quests.config.DatabaseConfig;
import com.quests.main.Quests;

public abstract class MySql {

	private DatabaseConfig data;
	
	private Connection connection;
	
	private BukkitRunnable updater;
	
	public abstract Databases config();
	
	public boolean isConnected() {
		return connection != null;
	}
	
	public void connect() throws SQLException {
		if(!this.isConnected()) {
			this.connection = DriverManager.getConnection("jdbc:mysql://" + this.data.getCredentials().getHost() + ":"
																		  + this.data.getCredentials().getPort() + "/"
																		  + this.data.getCredentials().getDatabase() + "?useSSL=false", 
																		  	this.data.getCredentials().getUsername(),
																		  	this.data.getCredentials().getPassword());
		}
		this.initialiseUpdater();
	}

	
	public abstract void update() throws SQLException;
	protected abstract void setup();
	
	public void loadConfig(DatabaseConfig data) {
		this.data = data;
	}
	
	public boolean exists(String path) {
		return false;
	}
	
	public void createDatabase(String name, String args) {
		
		PreparedStatement preparedStatement;
		
		try {
			preparedStatement = this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + name + " " + args);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void prepareStatement(String statement) {
		PreparedStatement preparedStatement;
		
		try {
			
			preparedStatement = this.getConnection().prepareStatement(statement);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void executeUpdate(String statement) {
		try {
			PreparedStatement preparedStatement = this.getConnection().prepareStatement(statement);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet executeQuery(String statement) {
		try {
			PreparedStatement keys = this.getConnection().prepareStatement(statement);
			ResultSet results = keys.executeQuery();
			return results;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void disconnect() {
		if(this.isConnected()) {
			Bukkit.getScheduler().runTaskAsynchronously(Quests.getInstance(), () -> {
				try {
					this.update();
					this.updater.cancel();
					this.connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
		}
	}

	public Connection getConnection() {
		return this.connection;
	}

	public void initialiseUpdater() {
		
		BukkitRunnable runnable = new BukkitRunnable() {	
			@Override
			public void run() {
				try {
					update();
				}catch (Exception e) {
					Quests.Log("&c" + e.getLocalizedMessage());
				}
			}
		};
		
		runnable.runTaskTimerAsynchronously(Quests.getInstance(), this.data.getUpdater().interval, this.data.getUpdater().interval);

		this.updater = runnable;
	}
	
	
}
