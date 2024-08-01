package cn.ussshenzhou.hotbaaaar.mixin;

import cn.ussshenzhou.hotbaaaar.Util;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static cn.ussshenzhou.hotbaaaar.Util.*;

/**
 * @author USS_Shenzhou
 */
@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Nullable
    protected abstract Player getCameraPlayer();

    @Shadow
    protected abstract void renderSlot(GuiGraphics guiGraphics, int i, int j, DeltaTracker deltaTracker, Player player, ItemStack itemStack, int k);

    /**
     * @author USS_Shenzhou
     * @reason Inject at HEAD would do the same, but overwrite is cheaper and more convenient.
     */
    @Overwrite
    private void renderItemHotbar(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Player player = this.getCameraPlayer();
        if (player != null) {
            ItemStack itemstack = player.getOffhandItem();
            HumanoidArm humanoidarm = player.getMainArm().getOpposite();
            int xCenter = graphics.guiWidth() / 2;
            final int oneHotbarLength = 182;
            final int halfHotbarLength = 91;
            final int hotbarHeight = 22;
            int hotbarAmount = Mth.clamp(graphics.guiWidth() / oneHotbarLength, 1, 4);
            int x0 = xCenter - hotbarAmount * halfHotbarLength;
            int x1 = x0 + hotbarAmount * oneHotbarLength;
            RenderSystem.enableBlend();
            graphics.pose().pushPose();
            graphics.pose().translate(0.0F, 0.0F, -90.0F);
            //render background-----
            for (int i = 0; i < hotbarAmount; i++) {
                graphics.blitSprite(HOTBAR_SPRITE, x0 + i * oneHotbarLength, graphics.guiHeight() - hotbarHeight, oneHotbarLength, hotbarHeight);
            }
            //render select frame-----
            graphics.blitSprite(HOTBAR_SELECTION_SPRITE, x0 - 1 + player.getInventory().selected * 20 + (player.getInventory().selected / 9 * 2), graphics.guiHeight() - 22 - 1, 24, 23);
            //render offhand-----
            if (!itemstack.isEmpty()) {
                if (humanoidarm == HumanoidArm.LEFT) {
                    graphics.blitSprite(HOTBAR_OFFHAND_LEFT_SPRITE, x0 - 29, graphics.guiHeight() - 23, 29, 24);
                } else {
                    graphics.blitSprite(HOTBAR_OFFHAND_RIGHT_SPRITE, x1 + halfHotbarLength, graphics.guiHeight() - 23, 29, 24);
                }
            }
            graphics.pose().popPose();
            RenderSystem.disableBlend();
            //render items-----
            int l = 1;
            for (int i = 0; i < hotbarAmount * 9; i++) {
                int x = x0 + i * 20 + 3 + (i / 9 * 2);
                int y = graphics.guiHeight() - 16 - 3;
                this.renderSlot(graphics, x, y, deltaTracker, player, player.getInventory().items.get(i), l++);
            }
            //render offhand item-----
            if (!itemstack.isEmpty()) {
                int i2 = graphics.guiHeight() - 16 - 3;
                if (humanoidarm == HumanoidArm.LEFT) {
                    this.renderSlot(graphics, x0 - 26, i2, deltaTracker, player, itemstack, l++);
                } else {
                    this.renderSlot(graphics, x1 + 10, i2, deltaTracker, player, itemstack, l++);
                }
            }

            if (this.minecraft.options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR) {
                RenderSystem.enableBlend();
                float f = this.minecraft.player.getAttackStrengthScale(0.0F);
                if (f < 1.0F) {
                    int j2 = graphics.guiHeight() - 20;
                    int k2 = x1 + 6;
                    if (humanoidarm == HumanoidArm.RIGHT) {
                        k2 = x0 - 22;
                    }

                    int l1 = (int) (f * 19.0F);
                    graphics.blitSprite(HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE, k2, j2, 18, 18);
                    graphics.blitSprite(HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE, 18, 18, 0, 18 - l1, k2, j2 + 18 - l1, 18, l1);
                }

                RenderSystem.disableBlend();
            }
        }
    }
}
