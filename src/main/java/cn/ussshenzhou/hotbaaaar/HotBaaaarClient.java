package cn.ussshenzhou.hotbaaaar;

import cn.ussshenzhou.hotbaaaar.util.SendDisplayResolutionListener;
import net.fabricmc.api.ClientModInitializer;

/**
 * @author USS_Shenzhou
 */
public class HotBaaaarClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SendDisplayResolutionListener.init();
    }
}