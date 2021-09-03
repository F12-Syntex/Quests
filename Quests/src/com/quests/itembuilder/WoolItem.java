package com.quests.itembuilder;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.quests.main.Quests;

public class WoolItem extends ItemContructor{

	public WoolItem(ItemStack item) {
		super(item);
	}

	@SuppressWarnings("deprecation")
	public static WoolItem getInstance(DyeColor colour) {
		
		if(Quests.getInstance().version.isLegacy()) {
			ItemStack itemStack = new ItemStack(new ItemStack(Material.valueOf("WOOL"), 1, colour.getWoolData()));
			WoolItem item = new WoolItem(itemStack);
			return item;
		}
			
			ItemStack itemStack = new ItemStack(new ItemStack(Material.valueOf("WHITE_WOOL"), 1, colour.getWoolData()));
			WoolItem item = new WoolItem(itemStack);
			return item;
			
		
	}

	@Override
	public ItemStack get() {
		return this.itemStack;
	}

    
}
