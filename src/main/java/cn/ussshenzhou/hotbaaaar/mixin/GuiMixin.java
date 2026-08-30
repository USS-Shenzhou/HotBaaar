package cn.ussshenzhou.hotbaaaar.mixin;

import cn.ussshenzhou.hotbaaaar.util.Util;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static cn.ussshenzhou.hotbaaaar.util.Util.HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE;
import static cn.ussshenzhou.hotbaaaar.util.Util.HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE;
import static cn.ussshenzhou.hotbaaaar.util.Util.HOTBAR_OFFHAND_LEFT_SPRITE;
import static cn.ussshenzhou.hotbaaaar.util.Util.HOTBAR_OFFHAND_RIGHT_SPRITE;
import static cn.ussshenzhou.hotbaaaar.util.Util.HOTBAR_SELECTION_SPRITE;
import static cn.ussshenzhou.hotbaaaar.util.Util.HOTBAR_SPRITE;

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
    private void renderSlot(GuiGraphics guiGraphics, int x, int y, float partialTick, Player player, ItemStack itemStack, int seed) {
    }

    /**
     * @author USS_Shenzhou
     * @reason The vanilla hotbar only supports 9 slots; overwrite centres on the whole screen and repeats per unit width.
     */
    @Overwrite
    public void renderHotbar(float partialTick, GuiGraphics guiGraphics) {
        Player player = this.getCameraPlayer();
        if (player != null) {
            ItemStack offhand = player.getOffhandItem();
            HumanoidArm offhandArm = player.getMainArm().getOpposite();
            int screenCenter = guiGraphics.guiWidth() / 2;
            final int oneHotbarLength = Util.HOTBAR_UNIT_LENGTH;
            final int halfHotbar = oneHotbarLength / 2;
            final int hotbarHeight = Util.HOTBAR_UNIT_HEIGHT;
            int hotbarAmount = Mth.clamp(guiGraphics.guiWidth() / oneHotbarLength, 1, 4);
            int x0 = screenCenter - hotbarAmount * halfHotbar;
            int x1 = x0 + hotbarAmount * oneHotbarLength;

            //render background-----
            for (int i = 0; i < hotbarAmount; i++) {
                guiGraphics.blitSprite(HOTBAR_SPRITE, x0 + i * oneHotbarLength, guiGraphics.guiHeight() - hotbarHeight, oneHotbarLength, hotbarHeight);
            }

            //render select frame-----
            int selectedSlot = player.getInventory().selected;
            guiGraphics.blitSprite(
                    HOTBAR_SELECTION_SPRITE,
                    x0 - 1 + selectedSlot * 20 + (selectedSlot / 9 * 2),
                    guiGraphics.guiHeight() - hotbarHeight - 1,
                    24,
                    23
            );

            //render offhand-----
            if (!offhand.isEmpty()) {
                if (offhandArm == HumanoidArm.LEFT) {
                    guiGraphics.blitSprite(HOTBAR_OFFHAND_LEFT_SPRITE, x0 - 29, guiGraphics.guiHeight() - 23, 29, 24);
                } else {
                    guiGraphics.blitSprite(HOTBAR_OFFHAND_RIGHT_SPRITE, x1, guiGraphics.guiHeight() - 23, 29, 24);
                }
            }

            //render items-----
            int seed = 1;
            for (int i = 0; i < hotbarAmount * 9; i++) {
                int x = x0 + i * 20 + 3 + (i / 9 * 2);
                int y = guiGraphics.guiHeight() - 16 - 3;
                this.renderSlot(guiGraphics, x, y, partialTick, player, player.getInventory().getItem(i), seed++);
            }

            //render offhand item-----
            if (!offhand.isEmpty()) {
                int y = guiGraphics.guiHeight() - 16 - 3;
                if (offhandArm == HumanoidArm.LEFT) {
                    this.renderSlot(guiGraphics, x0 - 26, y, partialTick, player, offhand, seed++);
                } else {
                    this.renderSlot(guiGraphics, x1 + 10, y, partialTick, player, offhand, seed++);
                }
            }

            RenderSystem.enableBlend();
            if (this.minecraft.options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR) {
                float attackStrengthScale = this.minecraft.player.getAttackStrengthScale(0.0F);
                if (attackStrengthScale < 1.0F) {
                    int y = guiGraphics.guiHeight() - 20;
                    int x = x1 + 6;
                    if (offhandArm == HumanoidArm.RIGHT) {
                        x = x0 - 22;
                    }

                    int progress = (int) (attackStrengthScale * 19.0F);
                    guiGraphics.blitSprite(HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE, x, y, 18, 18);
                    guiGraphics.blitSprite(HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE, 18, 18, 0, 18 - progress, x, y + 18 - progress, 18, progress);
                }
            }
            RenderSystem.disableBlend();
        }
    }
}