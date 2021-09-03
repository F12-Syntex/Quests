
package com.quests.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.quests.main.Quests;

public class Reload extends SubCommand {

    @Override
    public void onCommand(Player player, String[] args) {
    
    	if(args.length == 1) {
        	Quests.getInstance().reload();
        	Quests.getInstance().configManager.messages.send(player, "plugin_reload");
        	return;
    	}
    
    }

    @Override

    public String name() {
        return "reload";
    }

    @Override
    public String info() {
        return "reloads the plugin.";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

	@Override
	public String permission() {
		return Quests.getInstance().configManager.permissions.reload;	
	}
	
	@Override
	public AutoComplete autoComplete(CommandSender sender, String[] args) {
		AutoComplete tabCompleter = new AutoComplete();
		return tabCompleter;
	}
	

}