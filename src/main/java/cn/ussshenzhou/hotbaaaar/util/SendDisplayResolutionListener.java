package cn.ussshenzhou.hotbaaaar.util;

import cn.ussshenzhou.hotbaaaar.network.DisplayResolutionPacket;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

/**
 * @author USS_Shenzhou
 */
@EventBusSubscriber(Dist.CLIENT)
public class SendDisplayResolutionListener {

    @SubscribeEvent
    public static void onLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        ClientPacketDistributor.sendToServer(new DisplayResolutionPacket(Minecraft.getInstance().getWindow().getGuiScaledWidth()));
    }
}
