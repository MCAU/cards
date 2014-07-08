package com.ullarah.tcgmcau;

import com.ullarah.tcgmcau.ext.eMapImage;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

class mTask {

    public static void runCleanUsers(){

        if ( mConfig.getConfig().getLong("connection.args.cleanusers") > 0 ) {

            Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(mInit.getPlugin(), new Runnable() {

                public void run() {

                    mInit.getSqlConnection().sqlQuery("TRUNCATE TABLE users_temp", new String[]{});
                    Bukkit.getLogger().info("[" + mInit.getPlugin().getName() + "] " + mConfig.getLanguageConfig().getString("cleantemp"));

                }

            }, 72000L, mConfig.getConfig().getLong("connection.args.cleanusers") * 20);

        }

    }

    public static void runCleanStash(){

        if ( mConfig.getConfig().getLong("connection.args.cleanstash") > 0 ) {

            Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(mInit.getPlugin(), new Runnable() {

                public void run() {

                    mInit.getSqlConnection().sqlUpdate("DELETE FROM stash WHERE amount = '0'", new String[]{});
                    Bukkit.getLogger().info("[" + mInit.getPlugin().getName() + "] " + mConfig.getLanguageConfig().getString("cleanstash"));

                }

            }, 72000L, mConfig.getConfig().getLong("connection.args.cleanstash") * 20);

        }

    }

    public static void setupChat() {

        RegisteredServiceProvider<Chat> rsp = mInit.getPlugin().getServer().getServicesManager().getRegistration(Chat.class);

        mInit.setVaultChat(rsp.getProvider());

    }

    public static void setupEconomy() {

        if (mInit.getPlugin().getServer().getPluginManager().getPlugin("Vault") == null) return;

        RegisteredServiceProvider<Economy> rsp = mInit.getPlugin().getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) return;

        mInit.setVaultEcon(rsp.getProvider());

    }

    public static void addGhostManager() {

        Bukkit.getLogger().info( "[" + mInit.getPlugin().getName() + "] Adding Online Players to the GhostManager" );

        for( Player player : Bukkit.getOnlinePlayers() ){

            mInit.playerGhost.put( player.getUniqueId(), false );
            mInit.getGhostManager().addPlayer(player);

        }

    }

    public static void loadCardImages() {

        Bukkit.getLogger().info( "[" + mInit.getPlugin().getName() + "] Rendering Card Images to Maps" );

        mInit.getPlugin().getServer().getScheduler().runTaskAsynchronously(mInit.getPlugin(), new Runnable() {

            public void run() {

                for( String cardImage : mConfig.getCardMap().getKeys(true) ){

                    short cardID = (short) mConfig.getCardMap().getLong( cardImage );

                    if( cardID > 0 ){

                        String cardURL = cardImage.replace(".","/");

                        try{

                            eMapImage.reloadCardImage(cardURL, cardID);
                            mInit.cardMapID.put( cardURL, cardID );

                        } catch (NullPointerException e){

                            Bukkit.getLogger().info( "[" + mInit.getPlugin().getName() + "] " + cardImage + " has been deleted. Removing from Card Maps." );
                            mConfig.getCardMap().set( cardImage, null );

                        }


                    }

                }

            }

        });

    }

}
