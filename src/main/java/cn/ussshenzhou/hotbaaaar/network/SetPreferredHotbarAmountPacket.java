package cn.ussshenzhou.hotbaaaar.network;

import cn.ussshenzhou.hotbaaaar.Hotbaaaar;
import cn.ussshenzhou.hotbaaaar.HotbaaaarConfig;
import cn.ussshenzhou.hotbaaaar.util.HotbarServerManager;
import cn.ussshenzhou.t88.config.ConfigHelper;
import cn.ussshenzhou.t88.network.annotation.Decoder;
import cn.ussshenzhou.t88.network.annotation.Encoder;
import cn.ussshenzhou.t88.network.annotation.NetPacket;
import cn.ussshenzhou.t88.network.annotation.ServerHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * @author USS_Shenzhou
 */
@NetPacket(modid = Hotbaaaar.MOD_ID)
public class SetPreferredHotbarAmountPacket {
    private final int amount;

    public SetPreferredHotbarAmountPacket() {
        this.amount = ConfigHelper.getConfigRead(HotbaaaarConfig.class).actualHotbarAmount;
    }

    @Decoder
    public SetPreferredHotbarAmountPacket(FriendlyByteBuf buf) {
        this.amount = buf.readVarInt();
    }

    @Encoder
    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(amount);
    }

    @ServerHandler
    public void serverHandler(IPayloadContext context) {
        HotbarServerManager.setHotbaaaarAmount(context.player().getUUID(), amount);
    }
}
