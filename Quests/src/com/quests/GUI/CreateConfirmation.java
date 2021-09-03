package com.quests.GUI;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import com.quests.create.CreationData;
import com.quests.itembuilder.ItemGenerator;
import com.quests.main.Quests;
import com.quests.utils.MessageUtils;

public class CreateConfirmation extends GUI{
	
	private boolean commit = false;
	private CreationData creationData;
	
	public CreateConfirmation(Player player, CreationData creationData) {
		super(player);
		this.setCreationData(creationData);
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return MessageUtils.translateAlternateColorCodes("&6Confirmation");
	}

	@Override
	public String permission() {
		// TODO Auto-generated method stub
		return Quests.getInstance().configManager.permissions.basic;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 27;
	}

	@Override
	public Sound sound() {
		// TODO Auto-generated method stub
		return Sound.values()[0];
	}

	@Override
	public float soundLevel() {
		// TODO Auto-generated method stub
		return 0f;
	}

	@Override
	public boolean canTakeItems() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClickInventory(InventoryClickEvent e) {
		
		if(e.getSlot() == 12) {
			//create quest
			
			Quests.getInstance().configManager.quests.addQuest(creationData);
			
			Quests.getInstance().configManager.messages.send(player, "create_successfull");
			this.commit = true;
			this.player.closeInventory();
			return;
		}
		
		if(e.getSlot() == 14) {
			Quests.getInstance().configManager.messages.send(player, "create_unsucessfull");
			this.commit = true;
			this.player.closeInventory();
			return;
		}
		
	}

	@Override
	public void onOpenInventory(InventoryOpenEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCloseInventory(InventoryCloseEvent e) {
		if(!this.commit) {
			Quests.getInstance().configManager.messages.send(player, "create_unsucessfull");
		}
	}

	@Override
	public void Contents(Inventory inv) {
		this.fillEmpty(ItemGenerator.getInstance(Material.BLACK_STAINED_GLASS_PANE).get());
		
		inv.setItem(12, ItemGenerator.getInstance(Material.GREEN_WOOL)
				.setName("&aAccept")
				.setLore("&7This will accept current task.", "&7Click to confirm.", "")
				.addLore("&7Name: &6" + this.creationData.getNAME())
				.addLore("&7Target amount: &6" + this.creationData.getAMOUNT())
				.addLore("&7Quest Type: &6" + WordUtils.capitalize(this.creationData.getTYPE().name().replace("_", " ").toLowerCase()))
				.addLore("&7Quest Reward: &6" + this.creationData.getREWARD())
				.get());
		
		
		
		inv.setItem(14, ItemGenerator.getInstance(Material.RED_WOOL)
				.setName("&cCancel")
				.setLore("&7This will reject the current task.", "&7Click to confirm.")
				.get());
		
	}

	public CreationData getCreationData() {
		return creationData;
	}

	public void setCreationData(CreationData creationData) {
		this.creationData = creationData;
	}

}
