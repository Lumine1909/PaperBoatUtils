package io.github.lumine1909.network;

import net.minecraft.network.FriendlyByteBuf;

public enum ServerboundPackets {
    VERSION;
    public static int handleVersionPacket(FriendlyByteBuf buf) {
        try {
            short packetID = buf.readShort();
            if (packetID == 0) {
                return buf.readInt();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }
}
