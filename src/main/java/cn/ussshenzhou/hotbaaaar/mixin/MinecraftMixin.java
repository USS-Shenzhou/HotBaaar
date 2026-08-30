package cn.ussshenzhou.hotbaaaar.mixin;

import cn.ussshenzhou.hotbaaaar.network.DisplayResolutionPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author USS_Shenzhou
 */
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow
    @Nullable
    public abstract ClientPacketListener getConnection();

    @Inject(method = "resizeDisplay", at = @At("RETURN"))
    private void hotbaaaarSendNewSizeToServer(CallbackInfo ci) {
        if (this.getConnection() != null) {
            DisplayResolutionPacket.send(Minecraft.getInstance().getWindow().getGuiScaledWidth());
        }
    }
}