package com.octagon.airships.item;

import com.octagon.airships.reference.Reference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class ItemCFRCPlate extends AirshipsItem {
    public static final class Icons {
        public static IIcon slotIcon;
    }

    public ItemCFRCPlate() {
        super();
        setUnlocalizedName("cfrcPlate");
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);

        Icons.slotIcon = iconRegister.registerIcon(Reference.MOD_ID + ":cfrcPlateSlot");
    }
}
