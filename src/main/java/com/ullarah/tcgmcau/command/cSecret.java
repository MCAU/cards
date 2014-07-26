package com.ullarah.tcgmcau.command;

import com.ullarah.tcgmcau.mInit;

import net.minecraft.server.v1_7_R4.PacketPlayOutGameStateChange;
import net.minecraft.util.org.apache.commons.io.output.StringBuilderWriter;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Random;

public class cSecret {

    public static void runGayAgenda(CommandSender sender){

        int i = 0;

        String[] letterArray = {"G","A","Y","A","G","E","N","D","A"};
        String[] colourArray = {"&c&l&k","&e&l&k","&a&l&k","&b&l&k","&9&l&k","&d&l&k","&f&l&k"};

        StringBuilderWriter spamPlayer = new StringBuilderWriter();

        for( int x = 0; x < 5; x++ ) {

            while( i < 25 ){

                for( String letterCurrent : letterArray ) {

                    String getRandomColour = ChatColor.translateAlternateColorCodes( '&', colourArray[ new Random().nextInt( colourArray.length ) ] );

                    try {
                        spamPlayer.append(getRandomColour).append(letterCurrent);
                    } catch (IOException e) {
                        sender.sendMessage( ChatColor.RED + "There is something wrong with the Gay Agenda..." );
                    }

                }

                i++;

            }

            sender.sendMessage( spamPlayer.toString() );

        }
        spamPlayer.close();

    }

    public static void toggleGhost(CommandSender sender) {

        if( sender.hasPermission("cards.staff.ghost") ) if (sender instanceof Player) {
            Player player = (Player) sender;

            if (mInit.getGhostManager().isGhost(player)) {
                mInit.getGhostManager().setGhost(player, false);
                mInit.playerGhost.put( player.getUniqueId(), false );
                player.sendMessage( mInit.getMsgPrefix() + "Ghost Mode " + ChatColor.RED + "Disabled" + ChatColor.WHITE + "." );
            }
            else {
                mInit.getGhostManager().setGhost(player, true);
                mInit.playerGhost.put( player.getUniqueId(), true );
                player.sendMessage( mInit.getMsgPrefix() + "Ghost Mode " + ChatColor.GREEN + "Enabled" + ChatColor.WHITE + "." );
            }

        }

    }

    public static void showCredits(CommandSender sender) {

        ( (CraftPlayer) sender ).getHandle().playerConnection.sendPacket( new PacketPlayOutGameStateChange( 4,0 ) );

    }

}