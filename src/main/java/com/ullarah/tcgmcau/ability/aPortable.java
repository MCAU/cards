package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.ext.eAnvil;

import net.minecraft.server.v1_7_R3.EntityPlayer;
import net.minecraft.server.v1_7_R3.PacketPlayOutOpenWindow;

import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import org.json.simple.JSONObject;

public class aPortable {

    public static void openInterface( Player player, JSONObject json ) {

        String portableType = (String) json.get("type");

        switch( portableKeyword.valueOf( portableType ) ) {

            case WORKBENCH:
                player.openWorkbench( null, true);
                break;

            case ENCHANT:
                player.openEnchanting( null, true );
                break;

            case ENDERCHEST:
                player.openInventory( player.getEnderChest() );
                break;

            case ANVIL:
                openAnvil( player );
                break;

            case FURNACE:
                // # This is currently broken, doesn't want to burn items.
                // # Oh, and it crashes the server really hard. :)
                //player.openInventory( player.getServer().createInventory( null, InventoryType.FURNACE ) );
                break;

        }

    }

    private enum portableKeyword {
        WORKBENCH, ENCHANT,
        ENDERCHEST, FURNACE,
        ANVIL
    }

    private static void openAnvil(Player player){

        EntityPlayer p = ( (CraftPlayer) player ).getHandle();

        eAnvil container = new eAnvil(p);

        int c = p.nextContainerCounter();

        p.playerConnection.sendPacket( new PacketPlayOutOpenWindow( c, 8, "Repairing", 9, true ) );
        p.activeContainer = container;
        p.activeContainer.windowId = c;
        p.activeContainer.addSlotListener(p);

    }

}


