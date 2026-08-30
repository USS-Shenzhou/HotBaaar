package cn.ussshenzhou.hotbaaaar.mixin;

import cn.ussshenzhou.hotbaaaar.util.HotBaaaarHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

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

    @Unique
    private static final StackWalker WALKER = StackWalker.getInstance();

    @ModifyConstant(method = "isHotbarSlot", constant = @Constant(intValue = 9))
    private static int hotBaaaarAllSelectable(int constant) {
        return 36;
    }

    /**
     * @author USS_Shenzhou
     * @reason Inject at HEAD would do the same, but overwrite is cheaper and more convenient.
     */
    @Overwrite
    public static int getSelectionSize() {
        if (WALKER.walk(s -> s.anyMatch(f -> f.getClassName().startsWith("mekanism") || f.getClassName().startsWith("appeng.menu")))) {
            return 9;
        }
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            return 9 * HotBaaaarHelper.getHotBaaaarAmount(null);
        } else {
            return 36;
        }
    }

    /**
     * @author myworldzycpc
     * @reason Inject at HEAD would do the same, but overwrite is cheaper and more convenient.
     */
    @Overwrite
    public void swapPaint(double d) {
        int i = (int)Math.signum(d);
        this.selected -= i;

        while (this.selected < 0) {
            this.selected += getSelectionSize();
        }

        while (this.selected >= getSelectionSize()) {
            this.selected -= getSelectionSize();
        }
    }

    /**
     * @author USS_Shenzhou
     * @reason Inject at HEAD would do the same, but overwrite is cheaper and more convenient.
     */
    @Overwrite
    public int getSuitableHotbarSlot() {
        int max = HotBaaaarHelper.getHotBaaaarAmount(this.player.getUUID()) * 9;
        for (int i = 0; i < max; ++i) {
            int j = (this.selected + i) % max;
            if (this.items.get(j).isEmpty()) {
                return j;
            }
        }
        for (int k = 0; k < max; ++k) {
            int l = (this.selected + k) % max;
            if (!this.items.get(l).isEnchanted()) {
                return l;
            }
        }
        return this.selected;
    }
}