package com.octagon.airships.init;

import com.octagon.airships.item.ItemCFRCPlate;
import com.octagon.airships.item.ItemProbe;
import com.octagon.airships.item.ItemSCAlloy;
import com.octagon.airships.item.fluid.ItemPressureVessel;
import com.octagon.airships.reference.Reference;
import cpw.mods.fml.common.registry.GameRegistry;
import openmods.config.ItemInstances;
import openmods.config.game.RegisterItem;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModItems implements ItemInstances {

    @RegisterItem(name = "superConductingAlloy")
    public static ItemSCAlloy superConductingAlloy;

    @RegisterItem(name = "cfrcPlate")
    public static ItemCFRCPlate cfrcPlate;

    @RegisterItem(name = "probe")
    public static ItemProbe probe;

    @RegisterItem(name = "probeMining", unlocalizedName = "probe")
    public static ItemProbe probeMining;

    @RegisterItem(name = "probeGas", unlocalizedName = "probe")
    public static ItemProbe probeGas;

    @RegisterItem(name = "pressureVessel")
    public static ItemPressureVessel pressureVessel;
}
