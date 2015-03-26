package com.octagon.airships.block;

import com.octagon.airships.MassEffectShips;
import com.octagon.airships.block.tileentity.TileEntityMachineBase;
import com.octagon.airships.reference.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.block.BlockRotationMode;
import openmods.block.OpenBlock;

public class BlockMachineBase extends OpenBlock {
    //private final Random random = new Random();

    private IIcon front;
    private IIcon left;
    private IIcon right;
    private IIcon back;
    private IIcon top;
    private IIcon bottom;

    private IIcon frontActive;
    private IIcon leftActive;
    private IIcon rightActive;
    private IIcon backActive;
    private IIcon topActive;
    private IIcon bottomActive;

    private String frontName;
    private String leftName;
    private String rightName;
    private String backName;
    private String topName;
    private String bottomName;

    private String frontActiveName;
    private String leftActiveName;
    private String rightActiveName;
    private String backActiveName;
    private String topActiveName;
    private String bottomActiveName;


    public BlockMachineBase(String name, Material material, String textureFront, String textureFrontActive, String textureOther, String textureOtherActive) {
        this(name, material, textureFront, textureFrontActive, textureOther, textureOtherActive, textureOther, textureOtherActive);
    }

    public BlockMachineBase(String name, Material material, String textureFront, String textureFrontActive, String textureTop, String textureTopActive, String textureOther, String textureOtherActive) {
        this(name, material, textureFront, textureFrontActive, textureOther, textureOtherActive, textureOther, textureOtherActive, textureOther, textureOtherActive, textureTop, textureTopActive, textureOther, textureOtherActive);
    }

    public BlockMachineBase(String name, Material material, String textureFront, String textureFrontActive, String textureLeft, String textureLeftActive, String textureRight, String textureRightActive, String textureBack, String textureBackActive, String textureTop, String textureTopActive, String textureBottom, String textureBottomActive) {
        super(material);
        frontName = Reference.MOD_ID + ":" + textureFront;
        frontActiveName = Reference.MOD_ID + ":" + textureFrontActive;
        leftName = Reference.MOD_ID + ":" + textureLeft;
        leftActiveName = Reference.MOD_ID + ":" + textureLeftActive;
        rightName = Reference.MOD_ID + ":" + textureRight;
        rightActiveName = Reference.MOD_ID + ":" + textureRightActive;
        backName = Reference.MOD_ID + ":" + textureBack;
        backActiveName  = Reference.MOD_ID + ":" + textureBackActive;
        topName = Reference.MOD_ID + ":" + textureTop;
        topActiveName = Reference.MOD_ID + ":" + textureTopActive;
        bottomName = Reference.MOD_ID + ":" + textureBottom;
        bottomActiveName = Reference.MOD_ID + ":" + textureBottomActive;

        setRotationMode(BlockRotationMode.FOUR_DIRECTIONS);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        super.registerBlockIcons(register);

        front = register.registerIcon(frontName);
        left = register.registerIcon(leftName);
        right = register.registerIcon(rightName);
        back = register.registerIcon(backName);
        top = register.registerIcon(topName);
        bottom = register.registerIcon(bottomName);

        frontActive = register.registerIcon(frontActiveName);
        leftActive = register.registerIcon(leftActiveName);
        rightActive = register.registerIcon(rightActiveName);
        backActive = register.registerIcon(backActiveName);
        topActive = register.registerIcon(topActiveName);
        bottomActive = register.registerIcon(bottomActiveName);

        setTexture(ForgeDirection.SOUTH, front);
        setTexture(ForgeDirection.NORTH, back);
        setTexture(ForgeDirection.EAST, right);
        setTexture(ForgeDirection.WEST, left);
        setTexture(ForgeDirection.UP, top);
        setTexture(ForgeDirection.DOWN, bottom);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
        boolean active = false;

        TileEntity tileEntity = blockAccess.getTileEntity(x, y, z);
        if(tileEntity != null && tileEntity instanceof TileEntityMachineBase)
            active = ((TileEntityMachineBase) tileEntity).isActive();

        setTexture(ForgeDirection.SOUTH, active ? frontActive : front);
        setTexture(ForgeDirection.NORTH, active ? backActive : back);
        setTexture(ForgeDirection.EAST, active ? rightActive : right);
        setTexture(ForgeDirection.WEST, active ? leftActive : left);
        setTexture(ForgeDirection.UP, active ? topActive : top);
        setTexture(ForgeDirection.DOWN, active ? bottomActive : bottom);

        return super.getIcon(blockAccess, x, y, z, side);
    }

    @Override
    protected Object getModInstance() {
        return MassEffectShips.instance;
    }
}
