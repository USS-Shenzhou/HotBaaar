package cn.ussshenzhou.hotbaaaar.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import org.jetbrains.annotations.Nullable;
import java.security.InvalidParameterException;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * @author USS_Shenzhou
 */
public class HotBaaaarHelper {

    private static final Supplier<Integer> CLIENT = new Supplier<Integer>() {
        @SuppressWarnings("ConstantValue")
        @Override
        public Integer get() {
            var window = Minecraft.getInstance().getWindow();
            if (window == null) {
                return 36;
            }
            return Mth.clamp(window.getGuiScaledWidth() / Util.HOTBAR_UNIT_LENGTH, 1, 4);
        }
    };

    public static int getHotBaaaarAmount(@Nullable UUID uuid) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            return CLIENT.get();
        } else {
            if (uuid == null) {
                throw new InvalidParameterException("uuid is null on server");
            }
            return Mth.clamp(HotBaaaarServerManager.HOTBAR_AMOUNT.get(uuid) / Util.HOTBAR_UNIT_LENGTH, 1, 4);
        }
    }
}