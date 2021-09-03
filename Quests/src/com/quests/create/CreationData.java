package com.quests.create;

import com.quests.types.QuestType;

public class CreationData {
	
	private final String NAME;
	private final QuestType TYPE;
	private final String REWARD;
	private final int AMOUNT;
	
	public CreationData(String NAME, QuestType TYPE, String REWARD, int AMOUNT) {
		this.NAME = NAME;
		this.TYPE = TYPE;
		this.REWARD = REWARD;
		this.AMOUNT = AMOUNT;
	}

	public String getNAME() {
		return NAME;
	}

	public QuestType getTYPE() {
		return TYPE;
	}

	public String getREWARD() {
		return REWARD;
	}

	public int getAMOUNT() {
		return AMOUNT;
	}

}
