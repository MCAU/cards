package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class aItem {

    public static void giveItem( final Player player, JSONArray json ){

        for ( Object itemCurrentObject : json ) {

            JSONObject itemCurrentObjectJSON = (JSONObject) itemCurrentObject;

            String itemID = (String) itemCurrentObjectJSON.get( "id" );
            Long itemAmount = (Long) itemCurrentObjectJSON.get( "qty" );

            ItemStack itemStack = new ItemStack( Material.getMaterial( itemID ), itemAmount.intValue() );
            ItemMeta itemMeta = itemStack.getItemMeta();

            if( itemCurrentObjectJSON.containsKey( "name" ) )
                itemMeta.setDisplayName( setItemName( player, (String) itemCurrentObjectJSON.get( "name" ) ) );

            if( itemCurrentObjectJSON.containsKey( "lore" ) )
                itemMeta.setLore( setItemLore( player, (String) itemCurrentObjectJSON.get( "lore" ) ) );

            if( itemCurrentObjectJSON.containsKey( "data" ) ){

                Long itemData = (Long) itemCurrentObjectJSON.get( "data" );

                itemStack.setDurability( itemData.shortValue() );

            }

            if( itemCurrentObjectJSON.containsKey( "potion" ) ){

                Potion itemPotion;

                JSONObject potionObject = (JSONObject) itemCurrentObjectJSON.get( "potion" );

                String potionType = (String) potionObject.get( "type" );
                Boolean potionSplash = (Boolean) potionObject.get( "splash" );

                if( potionType.equals( "DISGUISE" ) || potionType.equals( "CONVERT" ) || potionType.equals( "CLONE" ) ) itemPotion = new Potion(0);
                else itemPotion = new Potion( PotionType.valueOf( potionType ) );

                itemPotion.setSplash( potionSplash );

                itemPotion.apply( itemStack );

            }

            if( itemCurrentObjectJSON.containsKey( "durability" ) )
                itemStack.setDurability( setItemDurability( (Long) itemCurrentObjectJSON.get( "durability" ) ) );

            itemStack.setItemMeta( itemMeta );

            if( itemCurrentObjectJSON.containsKey( "enchant" ) )
                itemStack.addUnsafeEnchantments(setItemEnchant((JSONArray) itemCurrentObjectJSON.get("enchant")));

            player.getWorld().dropItem( player.getLocation().add(0,1,0), itemStack );
            player.sendMessage( mInit.getMsgPrefix() + "You have received " + ChatColor.GREEN + itemAmount + " " + itemID + ChatColor.WHITE + "." );

        }

    }

    public static String setItemName( Player player, String itemName ){

        itemName = itemName.replaceAll( "_", " " );
        itemName = itemName.replaceAll( "%user%", player.getPlayerListName() );

        return ChatColor.translateAlternateColorCodes('&', itemName);

    }

    public static List<String> setItemLore( Player player, String itemLore ){

        List<String> itemLoreList = new ArrayList<>();

        String itemLoreArray[] = itemLore.split( "\\|" );

        for( String s : itemLoreArray ) {

            s = s.replaceAll( "_"," " );
            s = s.replaceAll( "%user%",player.getPlayerListName() );

            itemLoreList.add( ChatColor.translateAlternateColorCodes( '&', s ) );

        }

        return Collections.unmodifiableList(itemLoreList);

    }

    public static Map<Enchantment, Integer> setItemEnchant( JSONArray itemEnchantArray ){

        Map<Enchantment, Integer> itemEnchantMap = new HashMap<>();

        for ( Object itemCurrentEnchant : itemEnchantArray ) {

            JSONObject itemCurrentEnchantJSON = (JSONObject) itemCurrentEnchant;

            Enchantment itemCurrentObjectEnchantType = Enchantment.getByName( (String) itemCurrentEnchantJSON.get("type") );
            Long itemCurrentObjectEnchantLevel = (Long) itemCurrentEnchantJSON.get("level");

            itemEnchantMap.put( itemCurrentObjectEnchantType,itemCurrentObjectEnchantLevel.intValue() );

        }

        return itemEnchantMap;

    }

    public static Short setItemDurability( Long itemDurability ){

        return itemDurability.shortValue();

    }

}
