package com.ullarah.tcgmcau;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class mConfig {

    private static File configFile;
    private static File chanceFile;
    private static File languageFile;
    private static File playerFile;
    private static File cardMapFile;

    private static FileConfiguration config;
    private static FileConfiguration chanceConfig;
    private static FileConfiguration languageConfig;
    private static FileConfiguration playerConfig;
    private static FileConfiguration cardMap;

    public mConfig(){

        configFile = new File( mInit.getPlugin().getDataFolder(), "config.yml" );
        chanceFile = new File( mInit.getPlugin().getDataFolder(), "chance.yml" );
        languageFile = new File( mInit.getPlugin().getDataFolder(), "lang.yml" );
        playerFile = new File( mInit.getPlugin().getDataFolder(), "player.yml" );
        cardMapFile = new File( mInit.getPlugin().getDataFolder(), "cardmap.yml" );

        config = YamlConfiguration.loadConfiguration( configFile );
        chanceConfig = YamlConfiguration.loadConfiguration( chanceFile );
        languageConfig = YamlConfiguration.loadConfiguration( languageFile );
        playerConfig = YamlConfiguration.loadConfiguration( playerFile );
        cardMap = YamlConfiguration.loadConfiguration( cardMapFile );

    }

    public static void createAllFiles(){

        if( !( configFile.exists() ) ) mInit.getPlugin().saveResource("config.yml", true);
        if( !( chanceFile.exists() ) ) mInit.getPlugin().saveResource("chance.yml", true);
        if( !( languageFile.exists() ) ) mInit.getPlugin().saveResource("lang.yml", true);
        if( !( playerFile.exists() ) ) mInit.getPlugin().saveResource("player.yml", true);
        if( !( cardMapFile.exists() ) ) mInit.getPlugin().saveResource("cardmap.yml", true);

        loadConfig();

    }

    public static FileConfiguration getConfig(){ return config; }

    public static FileConfiguration getChanceConfig(){ return chanceConfig; }

    public static FileConfiguration getLanguageConfig(){ return languageConfig; }

    public static FileConfiguration getPlayerConfig(){ return playerConfig; }

    public static FileConfiguration getCardMap(){ return cardMap; }

    public static boolean loadConfig(){

        try{

            config = YamlConfiguration.loadConfiguration( new File( mInit.getPlugin().getDataFolder(), "config.yml" ) );
            chanceConfig = YamlConfiguration.loadConfiguration( new File( mInit.getPlugin().getDataFolder(), "chance.yml" ) );
            languageConfig = YamlConfiguration.loadConfiguration( new File( mInit.getPlugin().getDataFolder(), "lang.yml" ) );
            playerConfig = YamlConfiguration.loadConfiguration( new File( mInit.getPlugin().getDataFolder(), "player.yml" ) );
            cardMap = YamlConfiguration.loadConfiguration( new File( mInit.getPlugin().getDataFolder(), "cardmap.yml" ) );

            mInit.getPlugin().reloadConfig();

            return true;

        } catch (Exception e){

            e.printStackTrace();
            return false;

        }

    }

    public static void loadConfig( String configuration ){

        try {

            switch( validConfigs.valueOf(configuration.toUpperCase()) ) {

                case MAIN:
                    config = YamlConfiguration.loadConfiguration( new File( mInit.getPlugin().getDataFolder(), "config.yml" ) );
                    break;

                case CHANCE:
                    chanceConfig = YamlConfiguration.loadConfiguration( new File( mInit.getPlugin().getDataFolder(), "chance.yml" ) );
                    break;

                case LANGUAGE:
                    languageConfig = YamlConfiguration.loadConfiguration( new File( mInit.getPlugin().getDataFolder(), "lang.yml" ) );
                    break;

                case PLAYER:
                    playerConfig = YamlConfiguration.loadConfiguration( new File( mInit.getPlugin().getDataFolder(), "player.yml" ) );
                    break;

                case CARDMAP:
                    cardMap = YamlConfiguration.loadConfiguration( new File( mInit.getPlugin().getDataFolder(), "cardmap.yml" ) );
                    break;

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void saveConfig(){

        try {

            config.save( configFile );
            chanceConfig.save( chanceFile );
            languageConfig.save( languageFile );
            playerConfig.save( playerFile );
            cardMap.save( cardMapFile );


        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public static void saveConfig( String configuration ){

        try {

            switch( validConfigs.valueOf(configuration.toUpperCase()) ) {

                case MAIN:
                    config.save( configFile );
                    break;

                case CHANCE:
                    chanceConfig.save( chanceFile );
                    break;

                case LANGUAGE:
                    languageConfig.save( languageFile );
                    break;

                case PLAYER:
                    playerConfig.save( playerFile );
                    break;

                case CARDMAP:
                    cardMap.save( cardMapFile );
                    break;

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    private enum validConfigs {
        MAIN, CHANCE, LANGUAGE,
        PLAYER, CARDMAP
    }

}
