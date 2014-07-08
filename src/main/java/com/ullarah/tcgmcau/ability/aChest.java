package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class aChest implements Listener {

    private static Inventory chestInventory = null;

    public static void showChest( final Player player, JSONObject json ){

        final String chestName = (String) json.get( "name" );
        final Long chestSize = (Long) json.get( "size" );
        final JSONArray chestItemArray = (JSONArray) json.get( "item" );

        chestInventory = Bukkit.createInventory( null, chestSize.intValue(), ChatColor.translateAlternateColorCodes('&', chestName) );

        for ( Object itemCurrentObject : chestItemArray ) {

            JSONObject itemCurrentObjectJSON = (JSONObject) itemCurrentObject;

            String itemType = (String) itemCurrentObjectJSON.get( "id" );
            Long itemAmount = (Long) itemCurrentObjectJSON.get( "qty" );

            ItemStack itemStack = new ItemStack( Material.valueOf( itemType ), itemAmount.intValue() );
            ItemMeta itemMeta = itemStack.getItemMeta();

            if( itemCurrentObjectJSON.containsKey( "name" ) )
                itemMeta.setDisplayName( aItem.setItemName( player, (String) itemCurrentObjectJSON.get( "name" ) ) );

            if( itemCurrentObjectJSON.containsKey( "lore" ) )
                itemMeta.setLore( aItem.setItemLore( player, (String) itemCurrentObjectJSON.get( "lore" ) ) );

            if( itemCurrentObjectJSON.containsKey( "durability" ) )
                itemStack.setDurability( aItem.setItemDurability( (Long) itemCurrentObjectJSON.get( "durability" ) ) );

            itemStack.setItemMeta( itemMeta );

            if( itemCurrentObjectJSON.containsKey( "enchant" ) )
                itemStack.addUnsafeEnchantments( aItem.setItemEnchant( (JSONArray) itemCurrentObjectJSON.get( "enchant" ) ) );

            chestInventory.addItem( itemStack );

        }

        player.openInventory( chestInventory );

    }

    @EventHandler
    public void customChestAccess( final InventoryClickEvent event ){

        Player chestPlayer = (Player) event.getWhoClicked();

        ItemStack chestClicked = event.getCurrentItem();
        Inventory chestInventory = event.getInventory();

        if ( aChest.chestInventory != null && chestInventory.getName().equals( aChest.chestInventory.getName() ) ) {

            if( chestClicked != null && chestClicked.getType() != Material.AIR && event.getRawSlot() < chestInventory.getSize() ){

                ItemStack itemStack = new ItemStack( chestClicked.getType(), chestClicked.getAmount() );
                ItemMeta chestItemMeta = chestClicked.getItemMeta();

                itemStack.setDurability( chestClicked.getDurability() );

                itemStack.setItemMeta( chestItemMeta );

                chestPlayer.getWorld().dropItem( chestPlayer.getLocation().add(0,1,0), itemStack );
                chestPlayer.sendMessage( mInit.getMsgPrefix() + "You have received " + ChatColor.GREEN + chestClicked.getAmount() + " " + chestClicked.getType().toString() + ChatColor.WHITE + "." );

                event.setCancelled(true);
                chestPlayer.closeInventory();

            } else event.setCancelled(true);

        }

    }

}
