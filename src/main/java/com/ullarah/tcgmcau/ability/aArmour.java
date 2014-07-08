package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class aArmour {

    public static void giveArmour( final Player player, JSONArray json ){

        for ( Object armourCurrentObject : json ) {

            JSONObject armourCurrentObjectJSON = (JSONObject) armourCurrentObject;

            String armourType = (String) armourCurrentObjectJSON.get( "type" );
            Long armourAmount = (Long) armourCurrentObjectJSON.get( "qty" );

            ItemStack armourStack = new ItemStack( Material.getMaterial(armourType), armourAmount.intValue() );
            LeatherArmorMeta armourMeta = (LeatherArmorMeta) armourStack.getItemMeta();

            if( armourCurrentObjectJSON.containsKey( "name" ) )
                armourMeta.setDisplayName( aItem.setItemName( player, (String) armourCurrentObjectJSON.get( "name" ) ) );

            if( armourCurrentObjectJSON.containsKey( "lore" ) )
                armourMeta.setLore( aItem.setItemLore( player, (String) armourCurrentObjectJSON.get( "lore" ) ) );

            if( armourCurrentObjectJSON.containsKey( "colour" ) ){

                JSONArray itemColour = (JSONArray) armourCurrentObjectJSON.get( "colour" );

                Long colourRed = (Long) itemColour.get(0);
                Long colourGreen = (Long) itemColour.get(1);
                Long colourBlue = (Long) itemColour.get(2);

                armourMeta.setColor(Color.fromRGB(colourRed.intValue(), colourGreen.intValue(), colourBlue.intValue()));

            }

            if( armourCurrentObjectJSON.containsKey( "durability" ) )
                armourStack.setDurability( aItem.setItemDurability( (Long) armourCurrentObjectJSON.get( "durability" ) ) );

            armourStack.setItemMeta(armourMeta);

            if( armourCurrentObjectJSON.containsKey( "enchant" ) )
                armourStack.addUnsafeEnchantments( aItem.setItemEnchant( (JSONArray) armourCurrentObjectJSON.get( "enchant" ) ) );

            player.getWorld().dropItem( player.getLocation().add(0,1,0), armourStack );
            player.sendMessage( mInit.getMsgPrefix() + "You have received " + ChatColor.GREEN + armourAmount + " " + armourType + ChatColor.WHITE + "." );

        }

    }

}
