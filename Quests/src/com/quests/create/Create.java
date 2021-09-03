
package com.quests.create;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.quests.GUI.CreateConfirmation;
import com.quests.commands.AutoComplete;
import com.quests.commands.SubCommand;
import com.quests.main.Quests;
import com.quests.types.QuestType;
import com.quests.utils.Numbers;

public class Create extends SubCommand {

    @Override
    public void onCommand(Player player, String[] args) {
    	
    	if(args.length < 5) {
    		Quests.getInstance().configManager.messages.send(player, "create_invalid_syntax");
    		return;
    	}
    	
    	String name = args[1];
    	String type = args[2];
    	String amount = args[3];
    	
    	String reward = "";
    	
    	if(!Numbers.isNumber(amount)) {
    		Quests.getInstance().configManager.messages.send(player, "create_invalid_amount", "%amount%", amount);
    		return;
    	}
    	
    	for(int i = 4; i < args.length; i++) {
    		reward+=args[i] + " ";
    	}
    	
    	reward = reward.trim();
    	
    	boolean validQuest = false;
    	
    	for(QuestType i : QuestType.values()) {
    		if(i.name().equalsIgnoreCase(type)) {
    			validQuest = true;
    			break;
    		}
    	}

    	if(Quests.getInstance().configManager.quests.questExists(name)) {
    		Quests.getInstance().configManager.messages.send(player, "create_quest_exists", "%quest%", name);
    		return;
    	}
    	
    	if(validQuest && !reward.isEmpty()) {
    		QuestType questType = QuestType.valueOf(type.toUpperCase());
        	CreationData creationData = new CreationData(name, questType, reward, Integer.parseInt(amount));
        	CreateConfirmation confirmation = new CreateConfirmation(player, creationData);
        	confirmation.open();
    	}else {
    		Quests.getInstance().configManager.messages.send(player, "create_invalid_type", "%type%", type);
    		return;
    	}
    	
    }

    @Override

    public String name() {
        return "create";
    }

    @Override
    public String info() {
        return "creates a new quest.";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

	@Override
	public String permission() {
		return  Quests.getInstance().configManager.permissions.create;
	}
	
	@Override
	public AutoComplete autoComplete(CommandSender sender, String[] args) {
		AutoComplete tabCompleter = new AutoComplete();
		
		for(QuestType type : QuestType.values()) {
			if(args.length == 3) {
				tabCompleter.createEntry(args[1] + "." + type.name());
				tabCompleter.createEntry(args[1] + "." + type.name());
				tabCompleter.createEntry(args[1] + "." + type.name());
			}
		}
		return tabCompleter;
	}
	

}