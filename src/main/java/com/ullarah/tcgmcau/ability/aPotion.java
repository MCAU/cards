package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;
import com.ullarah.tcgmcau.particle.pEffect;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.List;

public class aPotion implements Listener {

    public static void givePotion( final Player player, JSONArray json ){

        for ( Object potionCurrentObject : json ) {

            JSONObject potionCurrentObjectJSON = (JSONObject) potionCurrentObject;

            Potion itemPotion;

            String potionType = (String) potionCurrentObjectJSON.get( "type" );
            Long potionAmount = (Long) potionCurrentObjectJSON.get( "qty" );
            Boolean potionSplash = (Boolean) potionCurrentObjectJSON.get( "splash" );

            ItemStack itemStack = new ItemStack( Material.POTION, potionAmount.intValue() );
            ItemMeta itemMeta = itemStack.getItemMeta();

            if( potionCurrentObjectJSON.containsKey( "name" ) )
                itemMeta.setDisplayName(aItem.setItemName(player, (String) potionCurrentObjectJSON.get("name")));

            if( potionCurrentObjectJSON.containsKey( "lore" ) )
                itemMeta.setLore( aItem.setItemLore( player, (String) potionCurrentObjectJSON.get( "lore" ) ) );

            if( potionType.equals( "DISGUISE" ) || potionType.equals( "CONVERT" ) || potionType.equals( "CLONE" ) ) itemPotion = new Potion(0);
            else itemPotion = new Potion( PotionType.valueOf(potionType) );

            itemPotion.setSplash( potionSplash );
            itemPotion.apply( itemStack );

            itemStack.setItemMeta( itemMeta );

            player.getWorld().dropItem( player.getLocation().add(0,1,0), itemStack );
            player.sendMessage( mInit.getMsgPrefix() + "You have received " + ChatColor.GREEN + potionAmount + " " + potionType + " Potion" + ChatColor.WHITE + "." );

        }

    }

    @EventHandler
    @SuppressWarnings("unchecked")
    public void getPotionSplash( PotionSplashEvent event ){

        List<String> potionLore = event.getPotion().getItem().getItemMeta().getLore();
        Collection<LivingEntity> potionAffected = event.getAffectedEntities();

        if( !potionAffected.isEmpty() && potionLore != null ){

            LivingEntity potionEntity = event.getAffectedEntities().iterator().next();

            Location potionEntityLocation = potionEntity.getLocation();

            JSONObject potionObject = new JSONObject();
            JSONObject potionEntityLocationObject = new JSONObject();

            potionEntityLocationObject.put( "world", potionEntityLocation.getWorld() );
            potionEntityLocationObject.put( "x", potionEntityLocation.getX() );
            potionEntityLocationObject.put( "y", potionEntityLocation.getY() );
            potionEntityLocationObject.put( "z", potionEntityLocation.getZ() );

            pEffect.EXPLODE.display( potionEntityLocation, 0.1F, 500 );

            if( potionLore.get(0).contains("Disguise As: ") ){

                String potionDisguise = event.getPotion().getItem().getItemMeta().getLore().iterator().next().replace("Disguise As: ", "").toUpperCase() ;

                potionObject.put("type", ChatColor.stripColor(potionDisguise));
                potionObject.put("view", false );

                if( potionLore.get(1).contains( "AREA" ) ){
                    for( Object entityObject : potionAffected ){
                        LivingEntity currentEntity = (LivingEntity) entityObject;
                        if( currentEntity.getType() != EntityType.PLAYER ) {
                            aDisguise.entityDisguise( currentEntity, potionObject );
                        }
                    }
                } else {
                    if( potionEntity.getType() != EntityType.PLAYER ) {
                        aDisguise.entityDisguise( potionEntity, potionObject );
                    }
                }

            }

            if( potionLore.get(0).contains("Convert To: ") ){

                String potionDisguise = event.getPotion().getItem().getItemMeta().getLore().iterator().next().replace("Convert To: ", "").toUpperCase() ;

                potionObject.put("type", ChatColor.stripColor(potionDisguise));
                potionObject.put("location", potionEntityLocationObject );

                if( potionLore.get(1).contains( "AREA" ) ){
                    for( Object entityObject : potionAffected ){
                        LivingEntity currentEntity = (LivingEntity) entityObject;
                        if( currentEntity.getType() != EntityType.PLAYER ) {
                            currentEntity.remove();
                            aSummon.playerSummon( currentEntity, potionObject );
                        }
                    }
                } else {
                    if( potionEntity.getType() != EntityType.PLAYER ) {
                        potionEntity.remove();
                        aSummon.playerSummon(potionEntity, potionObject);
                    }
                }

            }

            if( potionLore.get(0).contains("Clone Experiment") ){

                potionObject.put("type", potionEntity.getType().toString());
                potionObject.put("location", potionEntityLocationObject );

                if( potionLore.get(1).contains( "AREA" ) ){
                    for( Object entityObject : potionAffected ){
                        LivingEntity currentEntity = (LivingEntity) entityObject;
                        if( currentEntity.getType() != EntityType.PLAYER ) {
                            aSummon.playerSummon( currentEntity, potionObject );
                        }
                    }
                } else {
                    if( potionEntity.getType() != EntityType.PLAYER ) {
                        aSummon.playerSummon( potionEntity, potionObject );
                    }
                }

            }

        }

    }

}

