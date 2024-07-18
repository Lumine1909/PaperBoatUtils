package io.github.lumine1909.command;

import com.mojang.brigadier.arguments.*;
import io.github.lumine1909.Modes;
import io.github.lumine1909.PaperBoatUtils;
import io.github.lumine1909.network.ClientboundPackets;
import io.github.lumine1909.network.PacketHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Arrays;

import static net.minecraft.commands.Commands.*;

public class ModCommands {
    private static final com.mojang.brigadier.CommandDispatcher<CommandSourceStack> dispatcher = MinecraftServer.getServer().getCommands().getDispatcher();

    public static void registerCommands() {
        dispatcher.register(
                literal("stepsize").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(
                        argument("size", FloatArgumentType.floatArg()).executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayer();
                            if (player == null) return 0;
                            float size = FloatArgumentType.getFloat(ctx, "size");
                            PaperBoatUtils.setStepSize(size);
                            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                            buf.writeShort(ClientboundPackets.SET_STEP_HEIGHT.ordinal());
                            buf.writeFloat(size);
                            PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                            return 1;
                        })
                )
        );

        dispatcher.register(
                literal("reset").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    if (player == null) return 0;
                    PaperBoatUtils.resetSettings();
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeShort(ClientboundPackets.RESET.ordinal());
                    PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                    return 1;
                })
        );

        dispatcher.register(
                literal("defaultslipperiness").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("slipperiness", FloatArgumentType.floatArg()).executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayer();
                            if (player == null) return 0;
                            PaperBoatUtils.defaultSlipperiness = FloatArgumentType.getFloat(ctx, "slipperiness");
                            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                            buf.writeShort(ClientboundPackets.SET_DEFAULT_SLIPPERINESS.ordinal());
                            buf.writeFloat(PaperBoatUtils.defaultSlipperiness);
                            PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                            return 1;
                        })
                )
        );

        dispatcher.register(
                literal("blockslipperiness").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("slipperiness", FloatArgumentType.floatArg()).then(argument("blocks", StringArgumentType.greedyString()).executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    if (player == null) return 0;
                    float slipperiness = FloatArgumentType.getFloat(ctx, "slipperiness");
                    String blocks = StringArgumentType.getString(ctx, "blocks").trim();
                    String[] blockArray = blocks.split(",");
                    PaperBoatUtils.setBlocksSlipperiness(Arrays.asList(blockArray), slipperiness);
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeShort(ClientboundPackets.SET_BLOCKS_SLIPPERINESS.ordinal());
                    buf.writeFloat(slipperiness);
                    buf.writeUtf(blocks);
                    PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                    return 1;
                })))
        );

        dispatcher.register(
                literal("aircontrol").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("enabled", BoolArgumentType.bool()).executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    if (player == null) return 0;
                    boolean enabled = BoolArgumentType.getBool(ctx, "enabled");
                    PaperBoatUtils.setAirControl(enabled);
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeShort(ClientboundPackets.SET_AIR_CONTROL.ordinal());
                    buf.writeBoolean(enabled);
                    PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                    return 1;
                }))
        );

        dispatcher.register(
                literal("waterelevation").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("enabled", BoolArgumentType.bool()).executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    if (player == null) return 0;
                    boolean enabled = BoolArgumentType.getBool(ctx, "enabled");
                    PaperBoatUtils.setWaterElevation(enabled);
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeShort(ClientboundPackets.SET_BOAT_WATER_ELEVATION.ordinal());
                    buf.writeBoolean(enabled);
                    PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                    return 1;
                }))
        );

        dispatcher.register(
                literal("falldamage").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("enabled", BoolArgumentType.bool()).executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    if (player == null) return 0;
                    boolean enabled = BoolArgumentType.getBool(ctx, "enabled");
                    PaperBoatUtils.setFallDamage(enabled);
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeShort(ClientboundPackets.SET_BOAT_FALL_DAMAGE.ordinal());
                    buf.writeBoolean(enabled);
                    PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                    return 1;
                }))
        );

        dispatcher.register(
                literal("jumpforce").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("force", FloatArgumentType.floatArg()).executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayer();
                            if (player == null) return 0;
                            float force = FloatArgumentType.getFloat(ctx, "force");
                            PaperBoatUtils.setJumpForce(force);
                            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                            buf.writeShort(ClientboundPackets.SET_BOAT_JUMP_FORCE.ordinal());
                            buf.writeFloat(force);
                            PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                            return 1;
                        })
                )
        );

        dispatcher.register(
                literal("boatmode").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("mode", StringArgumentType.string()).executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    if (player == null) return 0;
                    Modes mode;
                    try {
                        mode = Modes.valueOf(StringArgumentType.getString(ctx, "mode"));
                    } catch (Exception e) {
                        StringBuilder valid_modes = new StringBuilder();
                        for (Modes m : Modes.values()) {
                            valid_modes.append(m.toString()).append(" ");
                        }
                        ctx.getSource().sendSystemMessage(Component.literal("Invalid mode! Valid modes are: " + valid_modes));
                        return 0;
                    }
                    Modes.setMode(mode);
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeShort(ClientboundPackets.SET_MODE.ordinal());
                    buf.writeShort(mode.ordinal());
                    PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                    return 1;
                }))
        );

        dispatcher.register(
                literal("boatgravity").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("gravity", DoubleArgumentType.doubleArg()).executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    if (player == null) return 0;
                    double gravity = DoubleArgumentType.getDouble(ctx, "gravity");
                    PaperBoatUtils.setGravityForce(gravity);
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeShort(ClientboundPackets.SET_GRAVITY.ordinal());
                    buf.writeDouble(gravity);
                    PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                    return 1;
                }))
        );

        dispatcher.register(
                literal("setyawaccel").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("accel", FloatArgumentType.floatArg()).executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayer();
                            if (player == null) return 0;
                            float accel = FloatArgumentType.getFloat(ctx, "accel");
                            PaperBoatUtils.setYawAcceleration(accel);
                            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                            buf.writeShort(ClientboundPackets.SET_YAW_ACCEL.ordinal());
                            buf.writeFloat(accel);
                            PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                            return 1;
                        })
                )
        );

        dispatcher.register(
                literal("setforwardaccel").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("accel", FloatArgumentType.floatArg()).executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayer();
                            if (player == null) return 0;
                            float accel = FloatArgumentType.getFloat(ctx, "accel");
                            PaperBoatUtils.setForwardsAcceleration(accel);
                            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                            buf.writeShort(ClientboundPackets.SET_FORWARD_ACCEL.ordinal());
                            buf.writeFloat(accel);
                            PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                            return 1;
                        })
                )
        );

        dispatcher.register(
                literal("setbackwardaccel").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("accel", FloatArgumentType.floatArg()).executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayer();
                            if (player == null) return 0;
                            float accel = FloatArgumentType.getFloat(ctx, "accel");
                            PaperBoatUtils.setBackwardsAcceleration(accel);
                            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                            buf.writeShort(ClientboundPackets.SET_BACKWARD_ACCEL.ordinal());
                            buf.writeFloat(accel);
                            PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                            return 1;
                        })
                )
        );

        dispatcher.register(
                literal("setturnforwardaccel").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("accel", FloatArgumentType.floatArg()).executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayer();
                            if (player == null) return 0;
                            float accel = FloatArgumentType.getFloat(ctx, "accel");
                            PaperBoatUtils.setTurningForwardsAcceleration(accel);
                            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                            buf.writeShort(ClientboundPackets.SET_TURN_ACCEL.ordinal());
                            buf.writeFloat(accel);
                            PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                            return 1;
                        })
                )
        );

        dispatcher.register(
                literal("allowaccelstacking").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("allow", BoolArgumentType.bool()).executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    if (player == null) return 0;
                    boolean allow = BoolArgumentType.getBool(ctx, "allow");
                    PaperBoatUtils.setAllowAccelStacking(allow);
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeShort(ClientboundPackets.ALLOW_ACCEL_STACKING.ordinal());
                    buf.writeBoolean(allow);
                    PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                    return 1;
                }))
        );

        dispatcher.register(literal("sendversionpacket").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).executes(ctx -> {
            ServerPlayer player = ctx.getSource().getPlayer();
            if (player == null) return 0;
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeShort(ClientboundPackets.RESEND_VERSION.ordinal());
            PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
            return 1;
        }));

        dispatcher.register(
                literal("underwatercontrol").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("enabled", BoolArgumentType.bool()).executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    if (player == null) return 0;
                    boolean enabled = BoolArgumentType.getBool(ctx, "enabled");
                    PaperBoatUtils.setUnderwaterControl(enabled);
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeShort(ClientboundPackets.SET_UNDERWATER_CONTROL.ordinal());
                    buf.writeBoolean(enabled);
                    PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                    return 1;
                }))
        );

        dispatcher.register(
                literal("surfacewatercontrol").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("enabled", BoolArgumentType.bool()).executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    if (player == null) return 0;
                    boolean enabled = BoolArgumentType.getBool(ctx, "enabled");
                    PaperBoatUtils.setSurfaceWaterControl(enabled);
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeShort(ClientboundPackets.SET_SURFACE_WATER_CONTROL.ordinal());
                    buf.writeBoolean(enabled);
                    PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                    return 1;
                }))
        );

        dispatcher.register(
                literal("exclusiveboatmode").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("mode", StringArgumentType.string()).executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    if (player == null) return 0;
                    Modes mode;
                    try {
                        mode = Modes.valueOf(StringArgumentType.getString(ctx, "mode"));
                    } catch (Exception e) {
                        StringBuilder valid_modes = new StringBuilder();
                        for (Modes m : Modes.values()) {
                            valid_modes.append(m.toString()).append(" ");
                        }
                        ctx.getSource().sendSystemMessage(Component.literal("Invalid mode! Valid modes are: " + valid_modes));
                        return 0;
                    }
                    PaperBoatUtils.resetSettings();
                    Modes.setMode(mode);
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeShort(ClientboundPackets.SET_EXCLUSIVE_MODE.ordinal());
                    buf.writeShort(mode.ordinal());
                    PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                    return 1;
                }))
        );

        dispatcher.register(
                literal("coyotetime").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("ticks", IntegerArgumentType.integer()).executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    if (player == null) return 0;
                    int time = IntegerArgumentType.getInteger(ctx, "ticks");
                    PaperBoatUtils.setCoyoteTime(time);
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeShort(ClientboundPackets.SET_COYOTE_TIME.ordinal());
                    buf.writeInt(time);
                    PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                    return 1;
                }))
        );

        dispatcher.register(
                literal("waterjumping").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("enabled", BoolArgumentType.bool()).executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    if (player == null) return 0;
                    boolean enabled = BoolArgumentType.getBool(ctx, "enabled");
                    PaperBoatUtils.setWaterJumping(enabled);
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeShort(ClientboundPackets.SET_WATER_JUMPING.ordinal());
                    buf.writeBoolean(enabled);
                    PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                    return 1;
                }))
        );

        dispatcher.register(
                literal("swimforce").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("force", FloatArgumentType.floatArg()).executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayer();
                            if (player == null) return 0;
                            float force = FloatArgumentType.getFloat(ctx, "force");
                            PaperBoatUtils.setSwimForce(force);
                            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                            buf.writeShort(ClientboundPackets.SET_SWIM_FORCE.ordinal());
                            buf.writeFloat(force);
                            PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                            return 1;
                        })
                )
        );

        dispatcher.register(
                literal("removeblockslipperiness").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("blocks", StringArgumentType.greedyString()).executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    if (player == null) return 0;
                    String blocks = StringArgumentType.getString(ctx, "blocks").trim();
                    String[] blockArray = blocks.split(",");
                    PaperBoatUtils.removeBlocksSlipperiness(Arrays.asList(blockArray));
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeShort(ClientboundPackets.REMOVE_BLOCKS_SLIPPERINESS.ordinal());
                    buf.writeUtf(blocks);
                    PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                    return 1;
                }))
        );

        dispatcher.register(
                literal("clearslipperiness").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    if (player == null) return 0;
                    PaperBoatUtils.clearSlipperinessMap();
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeShort(ClientboundPackets.CLEAR_SLIPPERINESS.ordinal());
                    PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                    return 1;
                })
        );

        dispatcher.register(
                literal("modeseries").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("modes", StringArgumentType.greedyString()).executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    if (player == null) return 0;
                    Modes mode;
                    String[] strs = StringArgumentType.getString(ctx, "modes").split(",");
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeShort(ClientboundPackets.MODE_SERIES.ordinal());
                    buf.writeShort(strs.length);
                    for (String modeStr : strs) {
                        try {
                            mode = Modes.valueOf(modeStr.trim());
                        } catch (Exception e) {
                            StringBuilder valid_modes = new StringBuilder();
                            for (Modes m : Modes.values()) {
                                valid_modes.append(m.toString()).append(" ");
                            }
                            ctx.getSource().sendSystemMessage(Component.literal("Invalid mode! Valid modes are: " + valid_modes));
                            return 0;
                        }
                        Modes.setMode(mode);
                        buf.writeShort(mode.ordinal());
                    }
                    PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                    return 1;
                }))
        );

        dispatcher.register(
                literal("exclusivemodeseries").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("modes", StringArgumentType.greedyString()).executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    if (player == null) return 0;
                    PaperBoatUtils.resetSettings();
                    Modes mode;
                    String[] strs = StringArgumentType.getString(ctx, "modes").split(",");
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeShort(ClientboundPackets.EXCLUSIVE_MODE_SERIES.ordinal());
                    buf.writeShort(strs.length);
                    for (String modeStr : strs) {
                        try {
                            mode = Modes.valueOf(modeStr.trim());
                        } catch (Exception e) {
                            StringBuilder valid_modes = new StringBuilder();
                            for (Modes m : Modes.values()) {
                                valid_modes.append(m.toString()).append(" ");
                            }
                            ctx.getSource().sendSystemMessage(Component.literal("Invalid mode! Valid modes are: " + valid_modes));
                            return 0;
                        }
                        Modes.setMode(mode);
                        buf.writeShort(mode.ordinal());
                    }
                    PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                    return 1;
                }))
        );

        dispatcher.register(
                literal("setblocksetting").requires(ctx -> ctx.getBukkitSender().hasPermission("paperboatutils.admin")).then(argument("setting", StringArgumentType.string()).then(argument("value", FloatArgumentType.floatArg()).then(argument("blocks", StringArgumentType.greedyString()).executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    if (player == null) return 0;
                    PaperBoatUtils.PerBlockSettingType setting;
                    try {
                        setting = PaperBoatUtils.PerBlockSettingType.valueOf(StringArgumentType.getString(ctx, "setting"));
                    } catch (Exception e) {
                        StringBuilder valid_settings = new StringBuilder();
                        for (PaperBoatUtils.PerBlockSettingType s : PaperBoatUtils.PerBlockSettingType.values()) {
                            valid_settings.append(s.toString()).append(" ");
                        }
                        ctx.getSource().sendSystemMessage(Component.literal("Invalid setting! Valid settings are: " + valid_settings));
                        return 0;
                    }
                    float value = FloatArgumentType.getFloat(ctx, "value");
                    String blocks = StringArgumentType.getString(ctx, "blocks");
                    PaperBoatUtils.setBlocksSetting(setting, Arrays.asList(blocks.split(",")), value);
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeShort(ClientboundPackets.SET_PER_BLOCK.ordinal());
                    buf.writeShort(setting.ordinal());
                    buf.writeFloat(value);
                    buf.writeUtf(blocks);
                    PacketHandler.syncPacket(new ClientboundCustomPayloadPacket(PaperBoatUtils.modKey, buf));
                    return 1;
                }))))
        );
    }
}