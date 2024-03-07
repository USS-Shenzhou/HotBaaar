package cn.ussshenzhou.hotbaaaar.mixin;

import cn.ussshenzhou.hotbaaaar.Util;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.AttackIndicatorStatus;
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
    protected int screenWidth;
    @Shadow
    protected int screenHeight;
    @Shadow
    @Final
    protected Minecraft minecraft;

    @Shadow
    @Nullable
    protected abstract Player getCameraPlayer();

    @Shadow
    protected abstract void renderSlot(GuiGraphics pGuiGraphics, int pX, int pY, float pPartialTick, Player pPlayer, ItemStack pStack, int pSeed);

    /**
     * @author USS_Shenzhou
     * @reason Inject at HEAD would do the same, but overwrite is cheaper and more convenient.
     */
    @Overwrite
    public void renderHotbar(float pPartialTick, GuiGraphics graphics) {
        Player player = this.getCameraPlayer();
        if (player != null) {
            HumanoidArm humanoidarm = player.getMainArm().getOpposite();
            int xCenter = this.screenWidth / 2;
            graphics.pose().pushPose();
            graphics.pose().translate(0.0F, 0.0F, -90.0F);
            int hotbarAmount = Mth.clamp(this.screenWidth / HOTBAR_UNIT_LENGTH, 1, 4);
            int x0 = xCenter - hotbarAmount * HOTBAR_UNIT_LENGTH / 2;
            int x1 = x0 + hotbarAmount * HOTBAR_UNIT_LENGTH;
            //render background-----
            for (int i = 0; i < hotbarAmount; i++) {
                graphics.pose().translate(0.0F, 0.0F, -0.1F);
                graphics.blitSprite(HOTBAR_SPRITE, x0 + i * HOTBAR_UNIT_LENGTH, this.screenHeight - 22, HOTBAR_UNIT_LENGTH, HOTBAR_UNIT_HEIGHT);
            }
            graphics.pose().translate(0.0F, 0.0F, 0.4F);
            //render select frame-----
            graphics.blitSprite(HOTBAR_SELECTION_SPRITE, x0 - 1 + player.getInventory().selected * 20 + (player.getInventory().selected / 9 * 2), this.screenHeight - 22 - 1, 24, 23);
            //render offhand-----
            ItemStack offhandItem = player.getOffhandItem();
            if (!offhandItem.isEmpty()) {
                if (humanoidarm == HumanoidArm.LEFT) {
                    graphics.blitSprite(HOTBAR_OFFHAND_LEFT_SPRITE, x0 - 29, this.screenHeight - 23, 29, 24);
                } else {
                    graphics.blitSprite(HOTBAR_OFFHAND_RIGHT_SPRITE, x1 + 91, this.screenHeight - 23, 29, 24);
                }
            }
            graphics.pose().popPose();
            //render items-----
            int l = 1;
            for (int i = 0; i < hotbarAmount * 9; i++) {
                int x = x0 + i * 20 + 3 + (i / 9 * 2);
                int y = this.screenHeight - 16 - 3;
                this.renderSlot(graphics, x, y, pPartialTick, player, player.getInventory().items.get(i), l++);
            }
            //render offhand item-----
            if (!offhandItem.isEmpty()) {
                if (humanoidarm == HumanoidArm.LEFT) {
                    this.renderSlot(graphics, x0 - 26, this.screenHeight - 16 - 3, pPartialTick, player, offhandItem, l++);
                } else {
                    this.renderSlot(graphics, x1 + 10, this.screenHeight - 16 - 3, pPartialTick, player, offhandItem, l++);
                }
            }

            RenderSystem.enableBlend();
            if (this.minecraft.options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR) {
                float f = this.minecraft.player.getAttackStrengthScale(0.0F);
                if (f < 1.0F) {
                    int x = x0 + 6;
                    if (humanoidarm == HumanoidArm.RIGHT) {
                        x = x1 - 22;
                    }
                    int y = this.screenHeight - 20;
                    int l1 = (int) (f * 19.0F);
                    graphics.blitSprite(HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE, x, y, 18, 18);
                    graphics.blitSprite(HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE, 18, 18, 0, 18 - l1, x, y + 18 - l1, 18, l1);
                }
            }
            RenderSystem.disableBlend();
        }
    }
}
