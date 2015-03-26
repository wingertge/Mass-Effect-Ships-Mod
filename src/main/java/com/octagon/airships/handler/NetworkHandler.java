package com.octagon.airships.handler;

import com.octagon.airships.network.MessageTileEntityUpdate;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import ibxm.Player;

public class NetworkHandler {

    private static SimpleNetworkWrapper networkWrapper;

    public static void init() {
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("MassEffectShips");

        registerPackets();
    }

    private static void registerPackets() {

    }

    public static void broadcastMessageAround(IMessage message, NetworkRegistry.TargetPoint target) {
        networkWrapper.sendToAllAround(message, target);
    }
}
