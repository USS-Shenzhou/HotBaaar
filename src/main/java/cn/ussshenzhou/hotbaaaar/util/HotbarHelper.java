package cn.ussshenzhou.hotbaaaar.util;

import cn.ussshenzhou.hotbaaaar.HotbaaaarConfig;
import cn.ussshenzhou.t88.config.ConfigHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.neoforged.fml.loading.FMLEnvironment;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * @author USS_Shenzhou
 */
public class HotbarHelper {

    public static int getHotbaaaarAmount(@Nullable UUID uuid) {
        return FMLEnvironment.getDist().isClient() ? ConfigHelper.getConfigRead(HotbaaaarConfig.class).actualHotbarAmount : HotbarServerManager.getHotbaaaarAmount(uuid);
    }
}
