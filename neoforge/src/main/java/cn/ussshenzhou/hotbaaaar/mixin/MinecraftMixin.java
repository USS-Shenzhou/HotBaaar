package cn.ussshenzhou.hotbaaaar.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * @author USS_Shenzhou
 */
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @SuppressWarnings("LocalMayBeArgsOnly")
    @ModifyConstant(method = "pickBlock", constant = @Constant(intValue = 36))
    public int hotbaaaarFromInventorySlotToMenuSlot(int value, @Local Inventory inventory) {
        if (inventory.selected >= 9) {
            return 0;
        }
        return 36;
    }
}
