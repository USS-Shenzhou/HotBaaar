package cn.ussshenzhou.hotbaaaar.network;

import cn.ussshenzhou.hotbaaaar.HotBaaaar;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

/**
 * @author USS_Shenzhou
 */
public class ModNetworkRegistry {

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(DisplayResolutionPacket.TYPE, (server, player, handler, buf, responseSender) -> {
            DisplayResolutionPacket packet = DisplayResolutionPacket.read(buf);
            server.execute(() -> packet.handle(player));
        });
    }
}