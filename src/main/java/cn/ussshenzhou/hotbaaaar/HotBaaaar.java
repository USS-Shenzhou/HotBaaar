package cn.ussshenzhou.hotbaaaar;

import cn.ussshenzhou.hotbaaaar.network.ModNetworkRegistry;
import net.fabricmc.api.ModInitializer;

/**
 * @author USS_Shenzhou
 */
public class HotBaaaar implements ModInitializer {

    public static final String MOD_ID = "hotbaaaar";

    //TODO 配置 数字键选择当前快捷栏
    //TODO 创造模式物品栏显示全部4格
    @Override
    public void onInitialize() {
        ModNetworkRegistry.register();
    }
}