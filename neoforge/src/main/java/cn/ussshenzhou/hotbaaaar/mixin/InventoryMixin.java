package cn.ussshenzhou.hotbaaaar.mixin;

import cn.ussshenzhou.hotbaaaar.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.OnlyIns;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author USS_Shenzhou
 */
@Mixin(Inventory.class)
public class InventoryMixin {

    @Shadow
    public int selected;

    @Shadow
    @Final
    public Player player;

    @Shadow
    @Final
    public NonNullList<ItemStack> items;

    @ModifyConstant(method = "isHotbarSlot", constant = @Constant(intValue = 9))
    private static int hotBaaaarAllSelectable(int constant) {
        return 36;
    }

    /**
     * @author USS_Shenzhou
     * @reason Inject at HEAD would do the same, but overwrite is cheaper and more convenient.
     */
    @Overwrite
    public void swapPaint(double pDirection) {
        int i = (int) Math.signum(pDirection);

        int max = 9;
        if (this.player.level().isClientSide) {
            max = hotBaaaarGetHotbarAmount() * 9;
            i = i * hotBaaaarGetScrollStep();
        }
        this.selected -= i;
        while (this.selected < 0) {
            this.selected += max;
        }

        while (this.selected >= max) {
            this.selected -= max;
        }
    }

    @Unique
    @OnlyIn(Dist.CLIENT)
    private int hotBaaaarGetHotbarAmount() {
        return Mth.clamp(Minecraft.getInstance().getWindow().getGuiScaledWidth() / Util.HOTBAR_UNIT_LENGTH, 1, 4);
    }

    @Unique
    @OnlyIn(Dist.CLIENT)
    private int hotBaaaarGetScrollStep() {
        if (Minecraft.getInstance().options.keyShift.isDown()) {
            return hotBaaaarGetHotbarAmount();
        } else if (Minecraft.getInstance().options.keySprint.isDown()) {
            return 9;
        }
        return 1;
    }

    /**
     * @author USS_Shenzhou
     * @reason Inject at HEAD would do the same, but overwrite is cheaper and more convenient.
     */
    @Overwrite
    public static int getSelectionSize() {
        return 36;
    }

    /**
     * @author USS_Shenzhou
     * @reason Inject at HEAD would do the same, but overwrite is cheaper and more convenient.
     */
    @Overwrite
    public int getSuitableHotbarSlot() {
        int max = 9;
        if (this.player.level().isClientSide) {
            if (this.player.level().isClientSide) {
                max = hotBaaaarGetHotbarAmount() * 9;
            }
        }
        for (int i = 0; i < max; ++i) {
            int j = (this.selected + i) % max;
            if (this.items.get(j).isEmpty()) {
                return j;
            }
        }
        for (int k = 0; k < max; ++k) {
            int l = (this.selected + k) % max;
            if (!this.items.get(l).isNotReplaceableByPickAction(this.player, l)) {
                return l;
            }
        }
        return this.selected;
    }
}
