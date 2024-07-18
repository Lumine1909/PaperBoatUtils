package io.github.lumine1909;

import io.github.lumine1909.network.ClientboundPackets;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Util {
    public static Object getSuperPrivateField(Object object, String fieldName) {
        try {
            final Field f = object.getClass().getSuperclass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setSuperPrivateField(Object object, String fieldName, Object value) {
        try {
            final Field f = object.getClass().getSuperclass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object invokeSuperPrivateMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        try {
            final Method m = object.getClass().getSuperclass().getDeclaredMethod(methodName, parameterTypes);
            m.setAccessible(true);
            return m.invoke(object, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addEntityToWorld(Entity nmsEntity, World world) {
        ServerLevel level = ((CraftWorld) world).getHandle();
        level.addFreshEntity(nmsEntity);
    }

    public static void sendSettings(Channel channel) {

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_STEP_HEIGHT.ordinal());
        buf.writeFloat(PaperBoatUtils.stepSize);
        channel.writeAndFlush(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_DEFAULT_SLIPPERINESS.ordinal());
        buf.writeFloat(PaperBoatUtils.defaultSlipperiness);
        channel.writeAndFlush(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_AIR_CONTROL.ordinal());
        buf.writeBoolean(PaperBoatUtils.airControl);
        channel.writeAndFlush(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_BOAT_WATER_ELEVATION.ordinal());
        buf.writeBoolean(PaperBoatUtils.waterElevation);
        channel.writeAndFlush(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_BOAT_FALL_DAMAGE.ordinal());
        buf.writeBoolean(PaperBoatUtils.fallDamage);
        channel.writeAndFlush(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_BOAT_JUMP_FORCE.ordinal());
        buf.writeFloat(PaperBoatUtils.jumpForce);
        channel.writeAndFlush(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_GRAVITY.ordinal());
        buf.writeDouble(PaperBoatUtils.gravityForce);
        channel.writeAndFlush(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_YAW_ACCEL.ordinal());
        buf.writeFloat(PaperBoatUtils.yawAcceleration);
        channel.writeAndFlush(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_FORWARD_ACCEL.ordinal());
        buf.writeFloat(PaperBoatUtils.forwardsAcceleration);
        channel.writeAndFlush(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_BACKWARD_ACCEL.ordinal());
        buf.writeFloat(PaperBoatUtils.backwardsAcceleration);
        channel.writeAndFlush(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_TURN_ACCEL.ordinal());
        buf.writeFloat(PaperBoatUtils.turningForwardsAcceleration);
        channel.writeAndFlush(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.ALLOW_ACCEL_STACKING.ordinal());
        buf.writeBoolean(PaperBoatUtils.allowAccelStacking);
        channel.writeAndFlush(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_SURFACE_WATER_CONTROL.ordinal());
        buf.writeBoolean(PaperBoatUtils.surfaceWaterControl);
        channel.writeAndFlush(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_UNDERWATER_CONTROL.ordinal());
        buf.writeBoolean(PaperBoatUtils.underwaterControl);
        channel.writeAndFlush(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_COYOTE_TIME.ordinal());
        buf.writeInt(PaperBoatUtils.coyoteTime);
        channel.writeAndFlush(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_WATER_JUMPING.ordinal());
        buf.writeBoolean(PaperBoatUtils.waterJumping);
        channel.writeAndFlush(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_SWIM_FORCE.ordinal());
        buf.writeFloat(PaperBoatUtils.swimForce);
        channel.writeAndFlush(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));

    }
}