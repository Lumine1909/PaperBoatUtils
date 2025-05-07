package io.github.lumine1909;

import io.github.lumine1909.messageutil.util.ProtocolUtil;
import io.github.lumine1909.network.ClientboundPackets;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static io.github.lumine1909.PaperBoatUtils.MOD_ID;

public class Util {

    public static Object getSuperPrivateField(Object object, String fieldName) {
        try {
            final Field f = object.getClass().getSuperclass().getSuperclass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setSuperPrivateField(Object object, String fieldName, Object value) {
        try {
            final Field f = object.getClass().getSuperclass().getSuperclass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object invokeSuperPrivateMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        try {
            final Method m = object.getClass().getSuperclass().getSuperclass().getDeclaredMethod(methodName, parameterTypes);
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

    public static void sendSettings(String playerName) {

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_STEP_HEIGHT.ordinal());
        buf.writeFloat(PaperBoatUtils.stepSize);
        ProtocolUtil.send(playerName, MOD_ID, buf);
        ProtocolUtil.send(playerName, MOD_ID, buf);

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_DEFAULT_SLIPPERINESS.ordinal());
        buf.writeFloat(PaperBoatUtils.defaultSlipperiness);
        ProtocolUtil.send(playerName, MOD_ID, buf);

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_AIR_CONTROL.ordinal());
        buf.writeBoolean(PaperBoatUtils.airControl);
        ProtocolUtil.send(playerName, MOD_ID, buf);

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_BOAT_WATER_ELEVATION.ordinal());
        buf.writeBoolean(PaperBoatUtils.waterElevation);
        ProtocolUtil.send(playerName, MOD_ID, buf);

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_BOAT_FALL_DAMAGE.ordinal());
        buf.writeBoolean(PaperBoatUtils.fallDamage);
        ProtocolUtil.send(playerName, MOD_ID, buf);

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_BOAT_JUMP_FORCE.ordinal());
        buf.writeFloat(PaperBoatUtils.jumpForce);
        ProtocolUtil.send(playerName, MOD_ID, buf);

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_GRAVITY.ordinal());
        buf.writeDouble(PaperBoatUtils.gravityForce);
        ProtocolUtil.send(playerName, MOD_ID, buf);

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_YAW_ACCEL.ordinal());
        buf.writeFloat(PaperBoatUtils.yawAcceleration);
        ProtocolUtil.send(playerName, MOD_ID, buf);

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_FORWARD_ACCEL.ordinal());
        buf.writeFloat(PaperBoatUtils.forwardsAcceleration);
        ProtocolUtil.send(playerName, MOD_ID, buf);

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_BACKWARD_ACCEL.ordinal());
        buf.writeFloat(PaperBoatUtils.backwardsAcceleration);
        ProtocolUtil.send(playerName, MOD_ID, buf);

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_TURN_ACCEL.ordinal());
        buf.writeFloat(PaperBoatUtils.turningForwardsAcceleration);
        ProtocolUtil.send(playerName, MOD_ID, buf);

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.ALLOW_ACCEL_STACKING.ordinal());
        buf.writeBoolean(PaperBoatUtils.allowAccelStacking);
        ProtocolUtil.send(playerName, MOD_ID, buf);

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_SURFACE_WATER_CONTROL.ordinal());
        buf.writeBoolean(PaperBoatUtils.surfaceWaterControl);
        ProtocolUtil.send(playerName, MOD_ID, buf);

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_UNDERWATER_CONTROL.ordinal());
        buf.writeBoolean(PaperBoatUtils.underwaterControl);
        ProtocolUtil.send(playerName, MOD_ID, buf);

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_COYOTE_TIME.ordinal());
        buf.writeInt(PaperBoatUtils.coyoteTime);
        ProtocolUtil.send(playerName, MOD_ID, buf);

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_WATER_JUMPING.ordinal());
        buf.writeBoolean(PaperBoatUtils.waterJumping);
        ProtocolUtil.send(playerName, MOD_ID, buf);

        buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeShort(ClientboundPackets.SET_SWIM_FORCE.ordinal());
        buf.writeFloat(PaperBoatUtils.swimForce);
        ProtocolUtil.send(playerName, MOD_ID, buf);
    }
}