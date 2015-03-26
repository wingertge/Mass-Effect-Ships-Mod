package com.octagon.airships.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public class MessageTileEntityUpdate implements IMessage {
    private NBTTagCompound data;

    public MessageTileEntityUpdate(NBTTagCompound data) {
        this.data = data;

    }

    public MessageTileEntityUpdate() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        data = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, data);
    }

    public static class Handler implements IMessageHandler<MessageTileEntityUpdate, IMessage> {

        @Override
        public IMessage onMessage(MessageTileEntityUpdate message, MessageContext ctx) {
            return null;
        }
    }
}
