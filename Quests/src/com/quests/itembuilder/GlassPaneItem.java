package com.quests.itembuilder;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.quests.main.Quests;

public class GlassPaneItem extends ItemContructor{

	public GlassPaneItem(ItemStack item) {
		super(item);
	}

	@SuppressWarnings("deprecation")
	public static GlassPaneItem getInstance(DyeColor colour) {
		
		
		if(Quests.getInstance().version.isLegacy()) {
			ItemStack itemStack = new ItemStack(new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, colour.getWoolData()));
			GlassPaneItem item = new GlassPaneItem(itemStack);
			return item;
		}
			
		ItemStack itemStack = new ItemStack(new ItemStack(Material.valueOf("WHITE_STAINED_GLASS_PANE"), 1, colour.getWoolData()));
		GlassPaneItem item = new GlassPaneItem(itemStack);
		return item;
		

	}

	@Override
	public ItemStack get() {
		return this.itemStack;
	}

    
}
