package com.quests.types;

import com.quests.utils.StringMinipulation;

public enum QuestType {	
	KILL_MOB, BLOCK_PLACE, BLOCK_BREAK, RUN_DISTANCE, COMMAND_RUN;
	
	public static String getValuesAsString() {
		String value = "";
		for(QuestType i : values()) {
			value+= i.name() + ", ";
		}
		value = StringMinipulation.removeLastCharOptional(value, 2);
		return value;
		}
	
}
