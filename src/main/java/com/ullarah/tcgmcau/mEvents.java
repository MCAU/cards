package com.ullarah.tcgmcau;

import com.gmail.nossr50.events.experience.McMMOPlayerLevelUpEvent;

import com.ullarah.tcgmcau.command.cBrand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class mEvents implements Listener{

    private static HashMap<Location, Material> materialGlowstepOriginal = new HashMap<>();
    private static HashMap<Location, Material> getMaterialGlowstepOriginal() {
        return materialGlowstepOriginal;
    }

    final String youFound(String typeCard) {
        return "You have found " + ChatColor.translateAlternateColorCodes('&', typeCard + " card!");
    }

    public static boolean isBetween( int value, int lower, int upper ) {
        return value >= lower && value <= upper;
    }

    private String randomCard( Player player ) {

        List<Integer> category = new ArrayList<>();

        Integer rarePercent = new Random().nextInt( 101 );

        Integer rareIndex = 0;
        String rareName = null;

        Boolean enabledVeryCommon = mConfig.getPlayerConfig().getBoolean( player.getPlayerListName() + ".rarity.VERY_COMMON" );
        Boolean enabledCommon = mConfig.getPlayerConfig().getBoolean( player.getPlayerListName() + ".rarity.COMMON" );
        Boolean enabledLessCommon = mConfig.getPlayerConfig().getBoolean( player.getPlayerListName() + ".rarity.LESS_COMMON" );
        Boolean enabledRare = mConfig.getPlayerConfig().getBoolean( player.getPlayerListName() + ".rarity.RARE" );
        Boolean enabledVeryRare = mConfig.getPlayerConfig().getBoolean( player.getPlayerListName() + ".rarity.VERY_RARE" );
        Boolean enabledExtraRare = mConfig.getPlayerConfig().getBoolean( player.getPlayerListName() + ".rarity.EXTRA_RARE" );

        Integer rarityVeryCommon = mConfig.getChanceConfig().getInt( "rarity.VERY_COMMON" );
        Integer rarityCommon  = mConfig.getChanceConfig().getInt( "rarity.COMMON" );
        Integer rarityLessCommon = mConfig.getChanceConfig().getInt( "rarity.LESS_COMMON" );
        Integer rarityRare  = mConfig.getChanceConfig().getInt( "rarity.RARE" );
        Integer rarityVeryRare = mConfig.getChanceConfig().getInt( "rarity.VERY_RARE" );
        Integer rarityExtraRare = mConfig.getChanceConfig().getInt( "rarity.EXTRA_RARE" );

        if( isBetween( rarePercent, rarityVeryCommon , rarityCommon ) && enabledVeryCommon ) {
            rareIndex = Rarities.VERY_COMMON.getRareNum();
            rareName = "a &7Very Common&f";
        }

        if( isBetween( rarePercent, rarityCommon, rarityLessCommon ) && enabledCommon ) {
            rareIndex = Rarities.COMMON.getRareNum();
            rareName = "a &aCommon&f";
        }

        if( isBetween( rarePercent, rarityLessCommon, rarityRare ) && enabledLessCommon ) {
            rareIndex = Rarities.LESS_COMMON.getRareNum();
            rareName = "a &3Less Common&f";
        }

        if( isBetween( rarePercent, rarityRare, rarityVeryRare ) && enabledRare ) {
            rareIndex = Rarities.RARE.getRareNum();
            rareName = "a &9Rare&f";
        }

        if( isBetween( rarePercent, rarityVeryRare, rarityExtraRare ) && enabledVeryRare ) {
            rareIndex = Rarities.VERY_RARE.getRareNum();
            rareName = "a &dVery Rare&f";
        }

        if( isBetween( rarePercent, rarityExtraRare, 101 ) && enabledExtraRare ) {
            rareIndex = Rarities.EXTRA_RARE.getRareNum();
            rareName = "an &cExtra Rare&f";
        }

        if( rareName != null){

            category.clear();

            try {

                ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT * FROM cards WHERE category = ? AND active = '1'", new String[]{rareIndex.toString()});

                while ( res.next() ) category.add( res.getInt("id") );

            } catch (SQLException e) {

                e.printStackTrace();

            }

            if( category.size() == 0 ) return null;

            Integer cardIndex = category.get( new Random().nextInt( category.size() ) );

            switch( mCheck.checkTotal( player, cardIndex ) ) {

                case 0:
                    return mInit.getMsgPrefix() + mConfig.getLanguageConfig().getString("maximumcards");

                case 1:
                    break;

                case 2:
                    randomCard( player );

            }

            try {

                ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT * FROM stash WHERE card = ? AND user = ?", new String[]{cardIndex.toString(), player.getPlayerListName()});

                if ( res.next() ) {

                    Integer setAmount = res.getInt( "amount" ) + 1;

                    mInit.getSqlConnection().sqlUpdate("UPDATE stash SET amount = ? WHERE card = ? AND user = ?", new String[]{setAmount.toString(), cardIndex.toString(), player.getPlayerListName()});

                    return mInit.getMsgPrefix() + youFound( rareName );

                } else {

                    mInit.getSqlConnection().sqlUpdate("INSERT INTO stash VALUES ( null, ?, ?, '1' )", new String[]{player.getPlayerListName(), cardIndex.toString()});

                    return mInit.getMsgPrefix() + youFound( rareName );

                }

            } catch (SQLException e) {

                e.printStackTrace();

                return null;

            }

        } else {

            return null;

        }

    }

    @EventHandler
    public void onPlayerLevelUp( final McMMOPlayerLevelUpEvent event ) {

        if( !mInit.getMaintenanceCheck()){

            Player player = event.getPlayer();
            String gotCard = null;

            Integer ran = new Random().nextInt( 100 );

            if( mCheck.checkSkill( event.getSkill(), event.getSkillLevel(), ran ) && mCheck.checkRegion(player) ) {

                if( mCheck.checkRegister( player ) ) gotCard = randomCard(player);
                else player.sendMessage(mInit.getMsgPrefix() + mConfig.getLanguageConfig().getString("notregistered"));

                if( gotCard != null ) player.sendMessage( gotCard );

            }

        }

    }

    @EventHandler
    public void onEntityDeath( final EntityDeathEvent event ){

        if( !mInit.getMaintenanceCheck()){

            Entity entity = event.getEntity();

            Integer ran = new Random().nextInt( 100 );

            Entity mob = event.getEntity();

            String gotCard = null;

            if( !( entity instanceof Player ) ) if (entity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {

                EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) entity.getLastDamageCause();

                if (entityDamageByEntityEvent.getDamager() instanceof Player) {

                    Player player = (Player) entityDamageByEntityEvent.getDamager();

                    if ( mCheck.checkEntity( mob, ran ) && mCheck.checkRegion(player) ) {

                        if ( mCheck.checkRegister(player) ) gotCard = randomCard( player );
                        else player.sendMessage(mInit.getMsgPrefix() + mConfig.getLanguageConfig().getString("notregistered"));

                        if (gotCard != null) player.sendMessage(gotCard);

                    }

                }

            }

        }

    }

    @EventHandler
    public void creatureSpawnEvent( final CreatureSpawnEvent event ){

        if( event.getSpawnReason() == SpawnReason.SPAWNER ){

            LivingEntity entity = event.getEntity();

            mInit.entitySpawners.add( entity.getEntityId() );

        }

    }

    @EventHandler
    public void onPlayerJoinEvent( PlayerJoinEvent event ) {

        if( mInit.playerPrefix.containsKey( event.getPlayer().getUniqueId() ) ){
            cBrand.runUnbrandinator( event.getPlayer() );
            mInit.playerPrefix.remove( event.getPlayer().getUniqueId() );
        }

        if( mInit.playerSuffix.containsKey( event.getPlayer().getUniqueId() ) ){
            cBrand.runUnbrandinator( event.getPlayer() );
            mInit.playerSuffix.remove( event.getPlayer().getUniqueId() );
        }

        if( !mConfig.getPlayerConfig().contains( event.getPlayer().getPlayerListName() ) ) {

            mConfig.getPlayerConfig().createSection( event.getPlayer().getPlayerListName() + ".rarity", mInit.getCategoryState());
            mConfig.saveConfig();

        }

        mInit.getGhostManager().addPlayer(event.getPlayer());
        mInit.playerGhost.put( event.getPlayer().getUniqueId(), false );
        mInit.getGhostManager().addPlayer(event.getPlayer());

    }

    @EventHandler
    public void onMobTarget( EntityTargetEvent event ) {

        if ( event.getTarget() instanceof Player ) if ( mInit.playerGhost.get( event.getTarget().getUniqueId() ) )
            if ( mInit.getGhostManager().isGhost(((Player) event.getTarget()).getPlayer()) ) event.setCancelled( true );

    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerMove( PlayerMoveEvent event ) {

        if( mInit.playerSpider.containsKey( event.getPlayer().getUniqueId() ) ){

            Block blockHorizontal = event.getPlayer().getLocation().getBlock();

            if( blockHorizontal.getType() != Material.AIR ) return;

            Block blockVertical = blockHorizontal.getRelative(BlockFace.UP);
            Location blockLocation = event.getPlayer().getLocation();

            if( blockHorizontal.getRelative( BlockFace.NORTH ).getType().isBlock() ||
                 blockHorizontal.getRelative( BlockFace.SOUTH ).getType().isBlock() ||
                 blockHorizontal.getRelative( BlockFace.EAST ).getType().isBlock()  ||
                 blockHorizontal.getRelative( BlockFace.WEST ).getType().isBlock()  ){

                double y = blockLocation.getY();

                event.getPlayer().sendBlockChange( blockHorizontal.getLocation(), Material.VINE, (byte) 0 );

                if( y % 1 > .40 && blockVertical.getType() == Material.AIR )
                    event.getPlayer().sendBlockChange( blockVertical.getLocation(), Material.VINE, (byte) 0 );

            }

        }

        if( mInit.playerGlow.containsKey( event.getPlayer().getUniqueId() ) ){

            final Block blockUnderPlayer = event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
            final Block blockAboveGlow = event.getPlayer().getLocation().getBlock().getRelative(BlockFace.SELF);

            if( getMaterialGlowstepOriginal().containsKey(blockUnderPlayer.getLocation()) ) return;

            getMaterialGlowstepOriginal().put(blockUnderPlayer.getLocation(), blockUnderPlayer.getType());

            if( mInit.getAllowedGlowStepMaterials().contains(blockUnderPlayer.getType()) && blockAboveGlow.getType().equals( Material.AIR ) )
                blockUnderPlayer.setType( Material.GLOWSTONE );

            Bukkit.getScheduler().scheduleSyncDelayedTask( mInit.getPlugin(), new Runnable() {
                public void run() {
                    blockUnderPlayer.setType(getMaterialGlowstepOriginal().get(blockUnderPlayer.getLocation()));
                    getMaterialGlowstepOriginal().remove( blockUnderPlayer.getLocation() );
                }
            }, 10);

        }

        if( mInit.playerWaterLava.containsKey( event.getPlayer().getUniqueId() ) ){

            //PLACEHOLDER

        }

    }

    @EventHandler
    public void onBlockBreak( BlockBreakEvent event ){

        Player player = event.getPlayer();

        if( mInit.playerDoubleDrop.containsKey( player.getUniqueId() ) )
            for (Object doubledropBlock : mInit.playerDoubleDrop.get(player.getUniqueId())) {

                Block block = event.getBlock();
                String doubledropblockType = doubledropBlock.toString();
                String blockName = event.getBlock().getType().name();

                if (blockName.equals(doubledropblockType))
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.getMaterial(blockName), 1));

            }

    }

    private enum Rarities {

        VERY_COMMON(1), COMMON(2), LESS_COMMON(3),
        RARE(4), VERY_RARE(5), EXTRA_RARE(6);

        private final int rareNum;

        Rarities(int r) {
            rareNum = r;
        }

        public int getRareNum() {
            return rareNum;
        }
    }

}