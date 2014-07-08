package com.ullarah.tcgmcau.ext;

import net.minecraft.server.v1_7_R3.ContainerAnvil;
import net.minecraft.server.v1_7_R3.EntityHuman;

public class eAnvil extends ContainerAnvil {

    public eAnvil(EntityHuman entity) {

        super(entity.inventory, entity.world, 0, 0, 0, entity);

    }

    @Override
    public boolean a(EntityHuman entityhuman) {

        return true;

    }

}
