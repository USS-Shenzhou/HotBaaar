package cn.ussshenzhou.hotbaaaar.network;

import cn.ussshenzhou.hotbaaaar.HotBaaaar;
import cn.ussshenzhou.hotbaaaar.util.HotBaaaarServerManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * @author USS_Shenzhou
 */
public class DisplayResolutionPacket implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<DisplayResolutionPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(HotBaaaar.MOD_ID, "display_resolution"));
    public static final StreamCodec<ByteBuf, DisplayResolutionPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            o -> o.width,
            DisplayResolutionPacket::new
    );

    public int width;

    public DisplayResolutionPacket(int width) {
        this.width = width;
    }

    public void handle(IPayloadContext context) {
        HotBaaaarServerManager.HOTBAR_AMOUNT.put(context.player().getUUID(), this.width);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
