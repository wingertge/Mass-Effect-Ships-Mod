package com.octagon.airships.reference;

import com.octagon.airships.util.ResourceLocationHelper;
import net.minecraft.util.ResourceLocation;

public class Textures {
    public static final class Gui {
        private static final String GUI_SHEET_LOCATION = "textures/gui/";
        public static final ResourceLocation MACHINE_GENERIC = ResourceLocationHelper.getResourceLocation(GUI_SHEET_LOCATION + "machineGeneric.png");
        public static final ResourceLocation MACHINE_ALLOY_MIXER = ResourceLocationHelper.getResourceLocation(GUI_SHEET_LOCATION + "alloyMixer.png");
        public static final ResourceLocation MACHINE_CERAMIC_FORMER = ResourceLocationHelper.getResourceLocation(GUI_SHEET_LOCATION + "ceramicFormer.png");
        public static final ResourceLocation EEZO_CORE_BASE = ResourceLocationHelper.getResourceLocation(GUI_SHEET_LOCATION + "eezoCoreBase.png");
        public static final ResourceLocation MACHINE_ELEMENTS = ResourceLocationHelper.getResourceLocation(GUI_SHEET_LOCATION + "elements/machine.png");
    }

    public static final class Model {
        private static final String MODEL_TEXTURE_LOCATION = "textures/models/";

        public static final ResourceLocation CORE_ACTIVE = ResourceLocationHelper.getResourceLocation(MODEL_TEXTURE_LOCATION + "coreTexture.png");
        public static final ResourceLocation CORE_IDLE = ResourceLocationHelper.getResourceLocation(MODEL_TEXTURE_LOCATION + "coreTexture.png");
        public static final ResourceLocation CORE_BOTTOM = ResourceLocationHelper.getResourceLocation(MODEL_TEXTURE_LOCATION + "coreTemp.png");
        public static final ResourceLocation CORE_BASE = ResourceLocationHelper.getResourceLocation(MODEL_TEXTURE_LOCATION + "coreBase.png");
        public static final ResourceLocation CORE_MID = ResourceLocationHelper.getResourceLocation(MODEL_TEXTURE_LOCATION + "coreMid.png");
    }
}
