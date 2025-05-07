package io.github.lumine1909;

import io.github.lumine1909.command.ModCommands;
import io.github.lumine1909.command.PluginCommands;
import io.github.lumine1909.messageutil.api.MessageUtil;
import io.github.lumine1909.network.PacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.SpigotConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaperBoatUtils extends JavaPlugin {

    public static final int VERSION = 13;
    public static final String MOD_ID = "openboatutils:settings";
    private static final double wronglyBackup = SpigotConfig.movedWronglyThreshold;
    private static final double tooQuicklyBackup = SpigotConfig.movedTooQuicklyMultiplier;
    public static boolean enabled = false;
    public static boolean fallDamage = true;
    public static boolean waterElevation = false;
    public static boolean airControl = false;
    public static float defaultSlipperiness = 0.6f;
    public static float jumpForce = 0f;
    public static float stepSize = 0f;
    public static double gravityForce = -0.03999999910593033;// funny rounding
    public static float yawAcceleration = 1.0f;
    public static float forwardsAcceleration = 0.04f;
    public static float backwardsAcceleration = 0.005f;
    public static float turningForwardsAcceleration = 0.005f;
    public static boolean allowAccelStacking = false;
    public static boolean underwaterControl = false;
    public static boolean surfaceWaterControl = false;
    public static int coyoteTime = 0;
    public static int coyoteTimer = 0;// timer decrements per tick, is reset to time when grounded
    public static boolean waterJumping = false;
    public static float swimForce = 0.0f;
    public static CollisionMode collision = CollisionMode.VANILLA;
    public static boolean canStepWhileFalling = false; // Setting to true fixes "boatutils jank"
    public static HashMap<String, Float> vanillaSlipperinessMap;
    public static HashMap<String, Float> slipperinessMap;
    public static HashMap<Integer, HashMap<String, Float>> perBlockSettings;
    public static PaperBoatUtils plugin;
    private final MessageUtil messageUtil = new MessageUtil(this);
    private final PacketHandler packetHandler = new PacketHandler();

    public static HashMap<String, Float> getVanillaSlipperinessMap() {
        if (vanillaSlipperinessMap == null) {
            vanillaSlipperinessMap = new HashMap<>();
            for (Block b : BuiltInRegistries.BLOCK.stream().toList()) {
                if (b.getFriction() != 0.6f) {
                    vanillaSlipperinessMap.put(String.valueOf(BuiltInRegistries.BLOCK.getId(b)), b.getFriction());
                }
            }
        }
        return vanillaSlipperinessMap;
    }

    public static boolean settingHasPerBlock(PerBlockSettingType setting) {
        return perBlockSettings != null && perBlockSettings.containsKey(setting.ordinal());
    }

    public static float getPerBlockForBlock(PerBlockSettingType setting, String blockid) {
        return settingHasPerBlock(setting) && perBlockSettings.get(setting.ordinal()).containsKey(blockid) ? perBlockSettings.get(setting.ordinal()).get(blockid) : defaultPerBlock(setting);
    }

    public static float getNearbySetting(Boat instance, PerBlockSettingType setting) {
        AABB box = instance.getBoundingBox();
        AABB box2 = new AABB(box.minX, box.minY - 0.001, box.minZ, box.maxX, box.minY, box.maxZ);
        int i = Mth.floor(box2.minX) - 1;
        int j = Mth.ceil(box2.maxX) + 1;
        int k = Mth.floor(box2.minY) - 1;
        int l = Mth.ceil(box2.maxY) + 1;
        int m = Mth.floor(box2.minZ) - 1;
        int n = Mth.ceil(box2.maxZ) + 1;
        VoxelShape voxelShape = Shapes.create(box2);
        float f = 0.0f;
        int o = 0;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int p = i; p < j; ++p) {
            for (int q = m; q < n; ++q) {
                int r = (p == i || p == j - 1 ? 1 : 0) + (q == m || q == n - 1 ? 1 : 0);
                if (r == 2) continue;
                for (int s = k; s < l; ++s) {
                    if (r > 0 && (s == k || s == l - 1)) continue;
                    mutable.set(p, s, q);
                    BlockState blockState = instance.level().getBlockState(mutable);
                    if (blockState.getBlock() instanceof WaterlilyBlock || !Shapes.joinIsNotEmpty(blockState.getCollisionShape(instance.level(), mutable).move(p, s, q), voxelShape, BooleanOp.AND))
                        continue;
                    f += getPerBlockForBlock(setting, String.valueOf(BuiltInRegistries.BLOCK.getId(blockState.getBlock())));
                    ++o;
                }
            }
        }
        if (o == 0) return getPerBlockForBlock(setting, "minecraft:air");
        return f / (float) o;
    }

    public static float defaultPerBlock(PerBlockSettingType setting) {
        switch (setting) {
            case yawAccel -> {
                return yawAcceleration;
            }
            case jumpForce -> {
                return jumpForce;
            }
            case forwardsAccel -> {
                return forwardsAcceleration;
            }
            case backwardsAccel -> {
                return backwardsAcceleration;
            }
            case turnForwardsAccel -> {
                return turningForwardsAcceleration;
            }
        }
        return 0;// unreachable but java compiler hates me (personally)
    }

    public static HashMap<String, Float> getSlipperinessMap() {
        if (slipperinessMap == null) {
            slipperinessMap = new HashMap<>(getVanillaSlipperinessMap());
        }
        return slipperinessMap;
    }

    public static void resetSettings() {
        enabled = false;
        stepSize = 0f;
        fallDamage = true;
        waterElevation = false;
        defaultSlipperiness = 0.6f;
        airControl = false;
        jumpForce = 0f;
        gravityForce = -0.03999999910593033;
        yawAcceleration = 1.0f;
        forwardsAcceleration = 0.04f;
        backwardsAcceleration = 0.005f;
        turningForwardsAcceleration = 0.005f;
        allowAccelStacking = false;
        underwaterControl = false;
        surfaceWaterControl = false;
        coyoteTime = 0;
        waterJumping = false;
        swimForce = 0.0f;
        slipperinessMap = new HashMap<>(getVanillaSlipperinessMap());
        perBlockSettings = new HashMap<>();
        collision = CollisionMode.VANILLA;
        canStepWhileFalling = false;
    }

    public static void setBlocksSlipperiness(List<String> blocks, float slipperiness) {
        enabled = true;
        for (String block : blocks) {
            setBlockSlipperiness(block, slipperiness);
        }
    }

    public static void setAllBlocksSlipperiness(float slipperiness) {
        enabled = true;
        defaultSlipperiness = slipperiness;
    }

    static void setBlockSlipperiness(String block, float slipperiness) {
        getSlipperinessMap().put(block, slipperiness);
    }

    public static float getBlockSlipperiness(String block) {
        if (getSlipperinessMap().containsKey(block)) return getSlipperinessMap().get(block);
        return defaultSlipperiness;
    }

    public static float getStepSize() {
        return stepSize;
    }

    public static void setStepSize(float stepsize) {
        enabled = true;
        stepSize = stepsize;
    }

    public static void setFallDamage(boolean newValue) {
        enabled = true;
        fallDamage = newValue;
    }

    public static void setWaterElevation(boolean newValue) {
        enabled = true;
        waterElevation = newValue;
    }

    //? >=1.21 {
    /*public record BytePayload(ByteBuf data) implements CustomPayload {
        public static final PacketCodec<PacketByteBuf, BytePayload> CODEC = CustomPayload.codecOf(BytePayload::write, BytePayload::new);
        public static final Id<BytePayload> ID = new Id<>(settingsChannel);

        public BytePayload(PacketByteBuf buf) {
            this(buf.copy());
            buf.readerIndex(buf.writerIndex());// so mc doesn't complain we haven't read all the bytes
        }

        void write(PacketByteBuf buf) {
            buf.writeBytes(data);
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    *///?}

    public static void setAirControl(boolean newValue) {
        enabled = true;
        airControl = newValue;
    }

    public static void setJumpForce(float newValue) {
        enabled = true;
        jumpForce = newValue;
    }

    public static void setGravityForce(double g) {
        enabled = true;
        gravityForce = g;
    }

    public static void setYawAcceleration(float accel) {
        enabled = true;
        yawAcceleration = accel;
    }

    public static void setForwardsAcceleration(float accel) {
        enabled = true;
        forwardsAcceleration = accel;
    }

    public static void setBackwardsAcceleration(float accel) {
        enabled = true;
        backwardsAcceleration = accel;
    }

    public static void setTurningForwardsAcceleration(float accel) {
        enabled = true;
        turningForwardsAcceleration = accel;
    }

    public static void setAllowAccelStacking(boolean value) {
        enabled = true;
        allowAccelStacking = value;
    }

    public static void setUnderwaterControl(boolean value) {
        enabled = true;
        underwaterControl = value;
    }

    public static void setSurfaceWaterControl(boolean value) {
        enabled = true;
        surfaceWaterControl = value;
    }

    public static void setCoyoteTime(int t) {
        enabled = true;
        coyoteTime = t;
    }

    public static void setWaterJumping(boolean value) {
        enabled = true;
        waterJumping = value;
    }

    public static void setSwimForce(float value) {
        enabled = true;
        swimForce = value;
    }

    public static void breakSlimePlease() {
        enabled = true;
        getSlipperinessMap().remove("minecraft:slime_block");
    }

    public static void removeBlockSlipperiness(String block) {
        enabled = true;
        getSlipperinessMap().remove(block);
    }

    public static void removeBlocksSlipperiness(List<String> blocks) {
        enabled = true;
        for (String block : blocks) {
            removeBlockSlipperiness(block);
        }
    }

    public static void clearSlipperinessMap() {
        enabled = true;
        slipperinessMap = new HashMap<>();
    }

    public static float GetJumpForce(Boat boat) {
        if (!settingHasPerBlock(PerBlockSettingType.jumpForce)) return jumpForce;
        else return getNearbySetting(boat, PerBlockSettingType.jumpForce);
    }

    public static float GetYawAccel(Boat boat) {
        if (!settingHasPerBlock(PerBlockSettingType.yawAccel)) return yawAcceleration;
        else return getNearbySetting(boat, PerBlockSettingType.yawAccel);
    }

    public static float GetForwardAccel(Boat boat) {
        if (!settingHasPerBlock(PerBlockSettingType.forwardsAccel)) return forwardsAcceleration;
        else return getNearbySetting(boat, PerBlockSettingType.forwardsAccel);
    }

    public static float GetBackwardAccel(Boat boat) {
        if (!settingHasPerBlock(PerBlockSettingType.backwardsAccel)) return backwardsAcceleration;
        else return getNearbySetting(boat, PerBlockSettingType.backwardsAccel);
    }

    public static float GetTurnForwardAccel(Boat boat) {
        if (!settingHasPerBlock(PerBlockSettingType.turnForwardsAccel)) return turningForwardsAcceleration;
        else return getNearbySetting(boat, PerBlockSettingType.turnForwardsAccel);
    }

    public static void setBlocksSetting(PerBlockSettingType setting, List<String> blocks, float value) {
        enabled = true;
        if (!settingHasPerBlock(setting)) perBlockSettings.put(setting.ordinal(), new HashMap<>());
        HashMap<String, Float> map = perBlockSettings.get(setting.ordinal());
        for (String block : blocks) {
            map.put(block, value);
        }
    }

    public static void setBlockSetting(PerBlockSettingType setting, String block, float value) {
        ArrayList<String> blocks = new ArrayList<>();
        blocks.add(block);
        setBlocksSetting(setting, blocks, value);
    }

    public static CollisionMode getCollisionMode() {
        return collision;
    }

    public static void setCollisionMode(CollisionMode mode) {
        enabled = true;
        collision = mode;
    }

    public static boolean canStepWhileFalling() {
        return canStepWhileFalling;
    }

    public static void setCanStepWhileFalling(boolean canStepWhileFalling) {
        PaperBoatUtils.canStepWhileFalling = canStepWhileFalling;
    }

    @Override
    public void onEnable() {
        plugin = this;
        messageUtil.getMessengerManager().register(packetHandler);
        messageUtil.enable();
        //SpigotConfig.movedWronglyThreshold = Double.MAX_VALUE;
        //SpigotConfig.movedTooQuicklyMultiplier = Double.MAX_VALUE;
        Bukkit.getPluginManager().registerEvents(new PacketHandler.PlayerListener(), plugin);
        ModCommands.registerCommands();
        PluginCommands.registerCommands();
    }

    @Override
    public void onDisable() {
        SpigotConfig.movedTooQuicklyMultiplier = tooQuicklyBackup;
        SpigotConfig.movedWronglyThreshold = wronglyBackup;
        messageUtil.disable();
    }

    public enum PerBlockSettingType {
        jumpForce,
        forwardsAccel,
        backwardsAccel,
        yawAccel,
        turnForwardsAccel,
    }
}