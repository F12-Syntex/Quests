package com.base.database;

public class MySQLUpdater {
	
	public long interval;

	public MySQLUpdater(long interval) {
		this.interval = interval;
	}
	
	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

}
