package cn.ussshenzhou.hotbaaaar.network;

import cn.ussshenzhou.hotbaaaar.HotBaaaar;
import cn.ussshenzhou.hotbaaaar.util.HotBaaaarServerManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * @author USS_Shenzhou
 */
public class DisplayResolutionPacket {

    public static final ResourceLocation TYPE = new ResourceLocation(HotBaaaar.MOD_ID, "display_resolution");

    public final int width;

    public DisplayResolutionPacket(int width) {
        this.width = width;
    }

    public static DisplayResolutionPacket read(FriendlyByteBuf buf) {
        return new DisplayResolutionPacket(buf.readVarInt());
    }

    public void handle(ServerPlayer player) {
        HotBaaaarServerManager.HOTBAR_AMOUNT.put(player.getUUID(), this.width);
    }

    public static void send(int width) {
        ClientPlayNetworking.send(TYPE, PacketByteBufs.create().writeVarInt(width));
    }
}