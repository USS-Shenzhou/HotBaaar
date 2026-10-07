package cn.ussshenzhou.hotbaaaar.util;

import cn.ussshenzhou.hotbaaaar.HotbaaaarConfig;
import cn.ussshenzhou.hotbaaaar.network.SetPreferredHotbarAmountPacket;
import cn.ussshenzhou.t88.config.ConfigHelper;
import cn.ussshenzhou.t88.network.NetworkHelper;
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
        NetworkHelper.sendToServer(new SetPreferredHotbarAmountPacket());
    }
}
