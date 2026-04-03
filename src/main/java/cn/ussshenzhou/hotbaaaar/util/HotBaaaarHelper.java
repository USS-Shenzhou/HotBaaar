package cn.ussshenzhou.hotbaaaar.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.neoforged.fml.loading.FMLEnvironment;

import javax.annotation.Nullable;
import java.security.InvalidParameterException;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * @author USS_Shenzhou
 */
public class HotBaaaarHelper {

    private static final Supplier<Integer> CLIENT = new Supplier<Integer>() {
        @Override
        public Integer get() {
            return Mth.clamp(Minecraft.getInstance().getWindow().getGuiScaledWidth() / Util.HOTBAR_UNIT_LENGTH, 1, 4);
        }
    };

    public static int getHotBaaaarAmount(@Nullable UUID uuid) {
        if (FMLEnvironment.getDist().isClient()) {
            return CLIENT.get();
        } else {
            if (uuid == null) {
                throw new InvalidParameterException("uuid is null on server");
            }
            return Mth.clamp(HotBaaaarServerManager.HOTBAR_AMOUNT.get(uuid) / Util.HOTBAR_UNIT_LENGTH, 1, 4);
        }
    }
}
