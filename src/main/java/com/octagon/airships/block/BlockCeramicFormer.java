package com.octagon.airships.block;

import net.minecraft.block.material.Material;

public class BlockCeramicFormer extends BlockMachineBase {
    public BlockCeramicFormer() {
        super("Ceramics Former", Material.iron, "ceramicFormer", "ceramicFormer_on", "ceramicFormer_top", "ceramicFormer_top_on", "machine", "machine"); //TODO: Localize name
        setBlockName("ceramicFormer");
    }
}
