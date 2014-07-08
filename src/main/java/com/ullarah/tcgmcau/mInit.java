package com.ullarah.tcgmcau;

import com.ullarah.tcgmcau.ability.aChat;
import com.ullarah.tcgmcau.ability.aChest;
import com.ullarah.tcgmcau.ability.aPotion;
import com.ullarah.tcgmcau.ext.eGhost;
import com.ullarah.tcgmcau.sql.sFunction;
import com.ullarah.tcgmcau.sql.sQuery;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map;

public class mInit extends JavaPlugin {

    private static Plugin plugin;

    private static sFunction sqlConnection;

    public static final HashMap<String, String> cardCode = new HashMap<>();
    public static final HashMap<String, Long> redeemCooldown = new HashMap<>();

    public static final HashMap<String, String> chatString = new HashMap<>();
    public static final HashMap<String, Long> chatDuration = new HashMap<>();

    public static final HashMap<UUID, String> playerPrefix = new HashMap<>();
    public static final HashMap<UUID, String> playerSuffix = new HashMap<>();

    public static final HashMap<UUID, Boolean> playerGhost = new HashMap<>();
    public static final HashMap<UUID, Boolean> playerGlow = new HashMap<>();
    public static final HashMap<UUID, Boolean> playerSpider = new HashMap<>();
    public static final HashMap<UUID, Boolean> playerWaterLava = new HashMap<>();

    public static final HashMap<UUID, List<?>> playerDoubleDrop = new HashMap<>();

    public static final HashMap<String, Short> cardMapID = new HashMap<>();

    private static HashMap<String, Boolean> categoryState;

    public static final List<Integer> entitySpawners = new ArrayList<>();

    private static int cooldownTime;

    private static String msgPrefix = null;
    private static String msgPermDeny = null;
    private static String msgNoConsole = null;

    private static Economy vaultEcon = null;
    private static Chat vaultChat = null;

    private static Boolean maintenanceCheck;
    private static String maintenanceMessage;

    private static eGhost ghostManager;

    public static Plugin getPlugin() { return plugin; }
    private static void setPlugin( Plugin plugin ) { mInit.plugin = plugin; }

    public static sFunction getSqlConnection() {
        return sqlConnection;
    }
    public static void setSqlConnection(sFunction sqlConnection) {
        mInit.sqlConnection = sqlConnection;
    }

    public static Map<String,Boolean> getCategoryState() {
        return Collections.unmodifiableMap(categoryState);
    }
    public static void setCategoryState(HashMap<String, Boolean> categoryState) {
        mInit.categoryState = categoryState;
    }

    public static int getCooldownTime() {
        return cooldownTime;
    }
    public static void setCooldownTime(int cooldownTime) {
        mInit.cooldownTime = cooldownTime;
    }

    public static String getMsgPrefix() {
        return msgPrefix;
    }
    public static void setMsgPrefix(String msgPrefix) {
        mInit.msgPrefix = msgPrefix;
    }

    public static String getMsgPermDeny() {
        return msgPermDeny;
    }
    public static void setMsgPermDeny(String msgPermDeny) {
        mInit.msgPermDeny = msgPermDeny;
    }

    public static String getMsgNoConsole() {
        return msgNoConsole;
    }
    public static void setMsgNoConsole(String msgNoConsole) {
        mInit.msgNoConsole = msgNoConsole;
    }

    public static Economy getVaultEcon() {
        return vaultEcon;
    }
    public static void setVaultEcon(Economy vaultEcon) {
        mInit.vaultEcon = vaultEcon;
    }

    public static Chat getVaultChat() {
        return vaultChat;
    }
    public static void setVaultChat(Chat vaultChat) {
        mInit.vaultChat = vaultChat;
    }

    public static Boolean getMaintenanceCheck() {
        return maintenanceCheck;
    }
    public static void setMaintenanceCheck(Boolean maintenanceCheck) {
        mInit.maintenanceCheck = maintenanceCheck;
    }

    public static String getMaintenanceMessage() {
        return maintenanceMessage;
    }
    public static void setMaintenanceMessage(String maintenanceMessage) {
        mInit.maintenanceMessage = maintenanceMessage;
    }

    public static eGhost getGhostManager() {
        return ghostManager;
    }
    public static void setGhostManager(eGhost ghostManager) {
        mInit.ghostManager = ghostManager;
    }

    private static List<Material> allowedGlowStepMaterials = Arrays.asList(
            Material.BRICK, Material.COAL_BLOCK, Material.COAL_ORE, Material.COBBLESTONE, Material.DIAMOND_BLOCK, Material.DIAMOND_ORE,
            Material.DIRT, Material.EMERALD_BLOCK, Material.EMERALD_ORE, Material.GLASS, Material.GOLD_BLOCK, Material.GOLD_ORE, Material.GRASS,
            Material.GRAVEL, Material.HARD_CLAY, Material.ICE, Material.IRON_BLOCK, Material.IRON_ORE, Material.LAPIS_BLOCK,
            Material.LAPIS_ORE, Material.MOSSY_COBBLESTONE, Material.MYCEL, Material.NETHER_BRICK, Material.NETHERRACK, Material.OBSIDIAN,
            Material.PACKED_ICE, Material.QUARTZ_BLOCK, Material.QUARTZ_ORE, Material.REDSTONE_BLOCK, Material.REDSTONE_ORE, Material.SAND,
            Material.SANDSTONE, Material.SMOOTH_BRICK, Material.SNOW_BLOCK, Material.SOUL_SAND, Material.SPONGE, Material.STONE
    );

    public static List<Material> getAllowedGlowStepMaterials() {
        return Collections.unmodifiableList( allowedGlowStepMaterials );
    }

    @SuppressWarnings("serial")
    public void onEnable() {

        setMsgPrefix(ChatColor.DARK_PURPLE + "[Cards] " + ChatColor.WHITE);

        setPlugin(this);

        PluginManager pluginManager = getServer().getPluginManager();

        new mConfig();
        mConfig.createAllFiles();

        setSqlConnection( new sFunction() );

        getCommand( "cards"  ).setExecutor( new mCommands( ) );
        getCommand( "redeem" ).setExecutor( new mCommands( ) );
        getCommand( "gift"   ).setExecutor( new mCommands( ) );

        pluginManager.registerEvents( new mEvents(), getPlugin() );
        pluginManager.registerEvents( new aChest(),  getPlugin() );
        pluginManager.registerEvents( new aPotion(), getPlugin() );
        pluginManager.registerEvents( new aChat(),   getPlugin() );

        mTask.setupChat();
        mTask.setupEconomy();

        //Load card images from cache
        mTask.loadCardImages();

        setCategoryState(new HashMap<String, Boolean>()
            {{ put("VERY_COMMON", true); put("COMMON", true); put("LESS_COMMON", true); put("RARE", true); put("VERY_RARE", true); put("EXTRA_RARE", true); }});

        setCooldownTime(mConfig.getConfig().getInt( "cards.cooldown" ));

        setMsgPermDeny(getMsgPrefix() + ChatColor.RED + mConfig.getLanguageConfig().getString( "nopermission" ));
        setMsgNoConsole(getMsgPrefix() + ChatColor.RED + mConfig.getLanguageConfig().getString( "noconsole" ));

        setMaintenanceCheck(mInit.getPlugin().getConfig().getBoolean( "maintenance" ));
        setMaintenanceMessage(getMsgPrefix() + ChatColor.RED + mConfig.getLanguageConfig().getString( "maintenance" ));

        setGhostManager(new eGhost(this));

        //Add current online players to the ghostManager
        mTask.addGhostManager();

        //Refresh all managed cards
        sQuery.runRefreshCards();

        mTask.runCleanStash();
        mTask.runCleanUsers();

    }

    public void onDisable() {

        Bukkit.getLogger().info( "[" + mInit.getPlugin().getName() + "] Saving Player Card States" );
        mConfig.saveConfig( "PLAYER" );

        Bukkit.getLogger().info( "[" + mInit.getPlugin().getName() + "] Saving Chance Values" );
        mConfig.saveConfig( "CHANCE" );

        Bukkit.getLogger().info( "[" + mInit.getPlugin().getName() + "] Saving Card Map Cache" );
        mConfig.saveConfig( "CARDMAP" );

        Bukkit.getLogger().info( "[" + mInit.getPlugin().getName() + "] Closing SQL Connection Pool" );
        getSqlConnection().sqlCloseConnection();

    }

}