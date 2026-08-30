package cn.ussshenzhou.hotbaaaar.util;

import cn.ussshenzhou.hotbaaaar.network.DisplayResolutionPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

/**
 * @author USS_Shenzhou
 */
public class SendDisplayResolutionListener {

    public static void init() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) ->
                DisplayResolutionPacket.send(client.getWindow().getGuiScaledWidth())
        );
    }
}