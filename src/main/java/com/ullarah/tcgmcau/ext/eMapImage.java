package com.ullarah.tcgmcau.ext;

import com.ullarah.tcgmcau.mConfig;
import com.ullarah.tcgmcau.mInit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.io.File;
import java.io.IOException;

public class eMapImage {

    @SuppressWarnings("deprecation")
    public static void getCardImage( Player player, String cardImage ){

        String cardURL = "http://cards.mcau.org/img/card/" + cardImage + ".JPG";

        MapView cardMap = null;
        ItemStack newMapItem;
        World playerWorld = player.getWorld();

        if( mInit.cardMapID.containsKey( cardImage ) ) cardMap = Bukkit.getMap( mInit.cardMapID.get( cardImage ) );

        if( cardMap == null ) {
            cardMap = Bukkit.createMap( playerWorld );
            if ( mInit.cardMapID.containsKey( cardURL ) ) mInit.cardMapID.remove( cardURL );
            if ( cardMap != null ) mInit.cardMapID.put( cardURL, cardMap.getId() );
        }

        assert cardMap != null;
        for( MapRenderer renderer : cardMap.getRenderers() ) cardMap.removeRenderer( renderer );

        MapRenderer renderer;

        try{

            renderer = new eMapRender( cardURL, false );
            cardMap.addRenderer( renderer );

            newMapItem = new ItemStack( Material.MAP, 1, cardMap.getId() );
            player.getWorld().dropItemNaturally(player.getLocation(), newMapItem);

            cardImage = cardImage.replace("/",".").replace(".JPG","");
            mConfig.getCardMap().set( cardImage, cardMap.getId() );

        } catch ( IOException e ){

            player.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + "Card failed to render. That's not good.");

        }

    }

    @SuppressWarnings("deprecation")
    public static void reloadCardImage(String cardImage, short cardID){

        MapView cardMap = Bukkit.getServer().getMap( cardID );
        for( MapRenderer renderer : cardMap.getRenderers() ) cardMap.removeRenderer( renderer );

        File file = new File( mInit.getPlugin().getDataFolder() + File.separator + "maps" + File.separator + cardImage + ".JPG" );

        try {
            if( file.exists() ) cardMap.addRenderer( new eMapRender( file.toString(), true ) );
            else cardMap.addRenderer( new eMapRender( "http://cards.mcau.org/img/card/" + cardImage + ".JPG", false ) );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}