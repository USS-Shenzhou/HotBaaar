package cn.ussshenzhou.hotbaaaar.mixin;

import cn.ussshenzhou.hotbaaaar.HotbaaaarConfig;
import cn.ussshenzhou.hotbaaaar.network.SetPreferredHotbarAmountPacket;
import cn.ussshenzhou.t88.config.ConfigHelper;
import cn.ussshenzhou.t88.network.NetworkHelper;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static cn.ussshenzhou.hotbaaaar.util.Util.*;

/**
 * @author USS_Shenzhou
 */
@Mixin(Hud.class)
public abstract class HudMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Nullable
    protected abstract Player getCameraPlayer();

    @Shadow
    protected abstract void extractSlot(GuiGraphicsExtractor graphics, int x, int y, DeltaTracker deltaTracker, Player player, ItemStack itemStack, int seed);

    @Inject(method = "extractItemHotbar", at = @At("HEAD"), order = 9999, cancellable = true)
    private void hotbaaaarOverwriteExtractItemHotbar(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        Player player = this.getCameraPlayer();
        if (player != null) {
            ItemStack offhand = player.getOffhandItem();
            HumanoidArm offhandArm = player.getMainArm().getOpposite();
            int screenCenter = graphics.guiWidth() / 2;
            final int oneHotbarLength = 182;
            final int halfHotbar = 91;
            final int hotbarHeight = 22;
            int maxHotbarAmount = Mth.clamp(graphics.guiWidth() / oneHotbarLength, 1, 4);
            var cfg = ConfigHelper.getConfigRead(HotbaaaarConfig.class);
            int hotbarAmount = Mth.clamp(cfg.preferredHotbarAmount, 1, maxHotbarAmount);
            if (cfg.actualHotbarAmount != hotbarAmount) {
                ConfigHelper.getConfigWrite(HotbaaaarConfig.class, c -> c.actualHotbarAmount = hotbarAmount);
                NetworkHelper.sendToServer(new SetPreferredHotbarAmountPacket());
            }
            int x0 = screenCenter - hotbarAmount * halfHotbar;
            int x1 = x0 + hotbarAmount * oneHotbarLength;
            //render background-----
            for (int i = 0; i < hotbarAmount; i++) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, x0 + i * oneHotbarLength, graphics.guiHeight() - hotbarHeight, oneHotbarLength, hotbarHeight);
            }
            //render select frame-----
            graphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    HOTBAR_SELECTION_SPRITE,
                    x0 - 1 + player.getInventory().getSelectedSlot() * 20 + (player.getInventory().getSelectedSlot() / 9 * 2),
                    graphics.guiHeight() - 22 - 1,
                    24,
                    23
            );
            //render offhand-----
            if (!offhand.isEmpty()) {
                if (offhandArm == HumanoidArm.LEFT) {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_LEFT_SPRITE, x0 - 29, graphics.guiHeight() - 23, 29, 24);
                } else {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_RIGHT_SPRITE, x1 + halfHotbar, graphics.guiHeight() - 23, 29, 24);
                }
            }

            //render items-----
            int seed = 1;
            for (int i = 0; i < hotbarAmount * 9; i++) {
                int x = x0 + i * 20 + 3 + (i / 9 * 2);
                int y = graphics.guiHeight() - 16 - 3;
                this.extractSlot(graphics, x, y, deltaTracker, player, player.getInventory().getItem(i), seed++);
            }

            //render offhand item-----
            if (!offhand.isEmpty()) {
                int y = graphics.guiHeight() - 16 - 3;
                if (offhandArm == HumanoidArm.LEFT) {
                    this.extractSlot(graphics, x0 - 26, y, deltaTracker, player, offhand, seed++);
                } else {
                    this.extractSlot(graphics, x1 + 10, y, deltaTracker, player, offhand, seed++);
                }
            }

            if (this.minecraft.options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR) {
                float attackStrengthScale = this.minecraft.player.getAttackStrengthScale(0.0F);
                if (attackStrengthScale < 1.0F) {
                    int y = graphics.guiHeight() - 20;
                    int x = x1 + 6;
                    if (offhandArm == HumanoidArm.RIGHT) {
                        x = x0 - 22;
                    }

                    int progress = (int) (attackStrengthScale * 19.0F);
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE, x, y, 18, 18);
                    graphics.blitSprite(
                            RenderPipelines.GUI_TEXTURED, HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE, 18, 18, 0, 18 - progress, x, y + 18 - progress, 18, progress
                    );
                }
            }
        }
        ci.cancel();
    }
}
