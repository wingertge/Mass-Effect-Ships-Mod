package com.octagon.airships.client.render.model;

import com.octagon.airships.reference.Models;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

@SideOnly(Side.CLIENT)
public class ModelSphere
{
    private IModelCustom modelSphere;

    public ModelSphere()
    {
        modelSphere = AdvancedModelLoader.loadModel(Models.SPHERE);
    }

    public void render()
    {
        modelSphere.renderAll();
    }

    public void renderPart(String partName)
    {
        modelSphere.renderPart(partName);
    }
}
