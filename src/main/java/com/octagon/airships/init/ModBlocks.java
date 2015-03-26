package com.octagon.airships.init;

import com.octagon.airships.block.*;
import com.octagon.airships.block.fluid.BlockLiquidDeuterium;
import com.octagon.airships.block.item.ItemEezoCoreBase;
import com.octagon.airships.block.item.ItemMachine;
import com.octagon.airships.block.tileentity.*;
import com.octagon.airships.reference.Reference;
import cpw.mods.fml.common.registry.GameRegistry;
import openmods.config.BlockInstances;
import openmods.config.game.RegisterBlock;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocks implements BlockInstances {
    @RegisterBlock(name = "alloyMixer", tileEntity = TileEntityAlloyMixer.class, itemBlock = ItemMachine.class)
    public static BlockAlloyMixer alloyMixer;

    @RegisterBlock(name = "ceramicFormer", tileEntity = TileEntityCeramicFormer.class, itemBlock = ItemMachine.class)
    public static BlockCeramicFormer ceramicFormer;

    @RegisterBlock(name = "eezoCoreBase", tileEntity = TileEntityEezoCoreMultiblock.class, itemBlock = ItemEezoCoreBase.class)
    public static BlockEezoCoreBase eezoCoreBase;

    @RegisterBlock(name = "eezoCoreHull", tileEntity = TileEntityMultiblockPart.class)
    public static BlockEezoCoreHull eezoCoreHull;

    @RegisterBlock(name = "refinedEezoBlock", tileEntity = TileEntityMultiblockPart.class)
    public static BlockRefinedEezo refinedEezoBlock;

    @RegisterBlock(name = "guiTest", tileEntity = TileEntityGuiTest.class)
    public static BlockGuiTest guiTest;

    @RegisterBlock(name = "launchPadController", tileEntity = TileEntityLaunchPadMultiblock.class)
    public static BlockLaunchPadController launchPadController;

    @RegisterBlock(name = "launchControllerExtension", tileEntity = TileEntityMultiblockPart.class)
    public static BlockLaunchControllerExtension launchControllerExtension;

    @RegisterBlock(name = "launchPad", tileEntity = TileEntityMultiblockPart.class)
    public static BlockLaunchPad launchPad;

    @RegisterBlock(name = "deuteriumLiquid")
    public static BlockLiquidDeuterium deuteriumLiquid;
}
