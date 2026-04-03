package cn.ussshenzhou.hotbaaaar.network;

import cn.ussshenzhou.hotbaaaar.HotBaaaar;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

/**
 * @author USS_Shenzhou
 */
@EventBusSubscriber
public class ModNetworkRegistry {

    @SubscribeEvent
    public static void networkPacketRegistry(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(HotBaaaar.MOD_ID);

        registrar.playToServer(DisplayResolutionPacket.TYPE, DisplayResolutionPacket.STREAM_CODEC, DisplayResolutionPacket::handle);
    }
}
