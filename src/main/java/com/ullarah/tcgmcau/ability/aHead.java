package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import org.json.simple.JSONObject;

public class aHead {

    public static void giveHead( final Player player, JSONObject json ){

        String headName = (String) json.get( "id" );

        ItemStack headItem = new ItemStack( Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal() );
        SkullMeta metaHead = (SkullMeta) headItem.getItemMeta();

        metaHead.setOwner(headName);
        headItem.setItemMeta(metaHead);

        player.getWorld().dropItem( player.getLocation().add(0,1,0), headItem );
        player.sendMessage( mInit.getMsgPrefix() + "You have received " + ChatColor.GREEN + headName + ChatColor.WHITE + "'s head.");

    }

}