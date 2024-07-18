package io.github.lumine1909.entity;

import io.github.lumine1909.PaperBoatUtils;
import io.github.lumine1909.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftLocation;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import java.util.List;

public class OpenBoat extends Boat {
    public OpenBoat(EntityType<? extends Boat> type, Level world) {
        super(type, world);
    }

    public OpenBoat(Level world, double x, double y, double z) {
        super(world, x, y, z);
    }

    private Boat.Status hookGetStatus(Boat instance, boolean is_tick) {
        instance.setMaxUpStep(0f);
        // Mojang name: getStatus
        Boat.Status status1 = (Status) Util.invokeSuperPrivateMethod(this, "y", null, null);
        Boat.Status originalStatus = status1;
        if (!PaperBoatUtils.instance.isEnabled() || !(getFirstPassenger() instanceof ServerPlayer player))
            return status1;

        if (is_tick) oncePerTick(instance, status1);

        instance.setMaxUpStep(PaperBoatUtils.getStepSize());

        if (status1 == Boat.Status.UNDER_WATER || status1 == Boat.Status.UNDER_FLOWING_WATER) {
            if (PaperBoatUtils.waterElevation) {
                // Mojang name: waterLevel
                double waterLevel = (double) Util.getSuperPrivateField(this, "aJ");
                Util.setSuperPrivateField(this, "aJ", waterLevel + 1.0);
                instance.setPos(instance.getX(), waterLevel, instance.getZ());
                Vec3 velocity = instance.getDeltaMovement();
                instance.setDeltaMovement(velocity.x, 0f, velocity.z);// parity with old boatutils, but maybe in the future
                // there should be an implementation with different y velocities here.
                return Boat.Status.IN_WATER;
            }
            return status1;
        }
        // Mojang name: checkInWater
        if ((boolean) Util.invokeSuperPrivateMethod(this, "z", null, null)) {
            if (PaperBoatUtils.waterElevation) {
                Vec3 velocity = instance.getDeltaMovement();
                instance.setDeltaMovement(velocity.x, 0.0, velocity.z);
            }
            status1 = Boat.Status.IN_WATER;
        }

        if (originalStatus == Boat.Status.IN_AIR && PaperBoatUtils.airControl) {
            // Mojang name: landFriction
            Util.setSuperPrivateField(this, "aK", PaperBoatUtils.getBlockSlipperiness("minecraft:air"));
            status1 = Boat.Status.ON_LAND;
        }

        return status1;
    }

    private void oncePerTick(Boat instance, Boat.Status status1) {
        if ((status1 == Status.UNDER_FLOWING_WATER || status1 == Boat.Status.UNDER_WATER) && PaperBoatUtils.swimForce != 0.0f) {
            Vec3 velocity = instance.getDeltaMovement();
            instance.setDeltaMovement(velocity.x, velocity.y + PaperBoatUtils.swimForce, velocity.z);
        }

        if (status1 == Boat.Status.ON_LAND || (PaperBoatUtils.waterJumping && status1 == Boat.Status.IN_WATER)) {
            PaperBoatUtils.coyoteTimer = PaperBoatUtils.coyoteTime;
        } else {
            PaperBoatUtils.coyoteTimer--;
        }

        float jumpForce = PaperBoatUtils.GetJumpForce(this);

        if (PaperBoatUtils.coyoteTimer >= 0 && jumpForce > 0f) {
            Vec3 velocity = instance.getDeltaMovement();
            instance.setDeltaMovement(velocity.x, jumpForce, velocity.z);
            PaperBoatUtils.coyoteTimer = -1;// cant jump again until grounded
        }
    }

    @Override
    public SoundEvent getPaddleSound() {
        return switch (this.hookGetStatus(this, false)) {
            case IN_WATER, UNDER_WATER, UNDER_FLOWING_WATER -> SoundEvents.BOAT_PADDLE_WATER;
            case ON_LAND -> SoundEvents.BOAT_PADDLE_LAND;
            default -> null;
        };
    }

    @Override
    public void tick() {
        // Mojang name: oldStatus
        Util.setSuperPrivateField(this, "aM", status);
        this.status = this.hookGetStatus(this, true);
        // Mojang name: outOfControlTicks
        float f = (float) Util.getSuperPrivateField(this, "q");
        if (this.status != Boat.Status.UNDER_WATER && this.status != Boat.Status.UNDER_FLOWING_WATER) {
            f = 0;
            Util.setSuperPrivateField(this, "q", f);
        } else {
            f++;
            Util.setSuperPrivateField(this, "q", f);
        }

        if (!this.level.isClientSide && f >= 60.0F) {
            this.ejectPassengers();
        }

        if (this.getHurtTime() > 0) {
            this.setHurtTime(this.getHurtTime() - 1);
        }

        if (this.getDamage() > 0.0F) {
            this.setDamage(this.getDamage() - 1.0F);
        }

        baseTick();
        // Mojang name: tickLerp
        Util.invokeSuperPrivateMethod(this, "x", null, null);
        if (this.isControlledByLocalInstance()) {
            if (!(this.getFirstPassenger() instanceof Player)) {
                this.setPaddleState(false, false);
            }

            floatBoat();
            if (this.level.isClientSide) {
                // Mojang name: controlBoat
                Util.invokeSuperPrivateMethod(this, "D", null, null);
                this.level.sendPacketToServer(new ServerboundPaddleBoatPacket(this.getPaddleState(0), this.getPaddleState(1)));
            }

            this.move(MoverType.SELF, this.getDeltaMovement());
        } else {
            this.setDeltaMovement(Vec3.ZERO);
        }

        // CraftBukkit start
        org.bukkit.Server server = this.level.getCraftServer();
        org.bukkit.World bworld = this.level.getWorld();

        Location to = CraftLocation.toBukkit(this.position(), bworld, this.getYRot(), this.getXRot());
        Vehicle vehicle = (Vehicle) this.getBukkitEntity();

        server.getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleUpdateEvent(vehicle));
        final Location lastLoc = (Location) Util.getSuperPrivateField(this, "lastLocation");
        if (lastLoc != null && !lastLoc.equals(to)) {
            VehicleMoveEvent event = new VehicleMoveEvent(vehicle, lastLoc, to);
            server.getPluginManager().callEvent(event);
        }

        Util.setSuperPrivateField(this, "lastLocation", vehicle.getLocation());
        // CraftBukkit end

        // Mojang name: tickBubbleColumn
        Util.invokeSuperPrivateMethod(this, "w", null, null);

        final float[] paddlePos = (float[]) Util.getSuperPrivateField(this, "o");
        for (int i = 0; i <= 1; ++i) {
            if (this.getPaddleState(i)) {
                if (!this.isSilent() && (double) (paddlePos[i] % 6.2831855F) <= 0.7853981852531433D && (double) ((paddlePos[i] + 0.3926991F) % 6.2831855F) >= 0.7853981852531433D) {
                    SoundEvent soundeffect = this.getPaddleSound();

                    if (soundeffect != null) {
                        Vec3 vec3d = this.getViewVector(1.0F);
                        double d0 = i == 1 ? -vec3d.z : vec3d.z;
                        double d1 = i == 1 ? vec3d.x : -vec3d.x;

                        this.level.playSound((Player) null, this.getX() + d0, this.getY(), this.getZ() + d1, soundeffect, this.getSoundSource(), 1.0F, 0.8F + 0.4F * this.random.nextFloat());
                    }
                }

                paddlePos[i] += 0.3926991F;
            } else {
                paddlePos[i] = 0.0F;
            }
        }

        this.checkInsideBlocks();
        List<Entity> list = this.level.getEntities((Entity) this, this.getBoundingBox().inflate(0.20000000298023224D, -0.009999999776482582D, 0.20000000298023224D), EntitySelector.pushableBy(this));

        if (!list.isEmpty()) {
            boolean flag = !this.level.isClientSide && !(this.getControllingPassenger() instanceof Player);

            for (int j = 0; j < list.size(); ++j) {
                Entity entity = (Entity) list.get(j);

                if (!entity.hasPassenger((Entity) this)) {
                    if (flag && this.getPassengers().size() < this.getMaxPassengers() && !entity.isPassenger() && this.hasEnoughSpaceFor(entity) && entity instanceof LivingEntity && !(entity instanceof WaterAnimal) && !(entity instanceof Player)) {
                        entity.startRiding(this);
                    } else {
                        this.push(entity);
                    }
                }
            }
        }

    }

    @Override
    public float getGroundFriction() {
        AABB axisalignedbb = this.getBoundingBox();
        AABB axisalignedbb1 = new AABB(axisalignedbb.minX, axisalignedbb.minY - 0.001D, axisalignedbb.minZ, axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        int i = Mth.floor(axisalignedbb1.minX) - 1;
        int j = Mth.ceil(axisalignedbb1.maxX) + 1;
        int k = Mth.floor(axisalignedbb1.minY) - 1;
        int l = Mth.ceil(axisalignedbb1.maxY) + 1;
        int i1 = Mth.floor(axisalignedbb1.minZ) - 1;
        int j1 = Mth.ceil(axisalignedbb1.maxZ) + 1;
        VoxelShape voxelshape = Shapes.create(axisalignedbb1);
        float f = 0.0F;
        int k1 = 0;
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

        for (int l1 = i; l1 < j; ++l1) {
            for (int i2 = i1; i2 < j1; ++i2) {
                int j2 = (l1 != i && l1 != j - 1 ? 0 : 1) + (i2 != i1 && i2 != j1 - 1 ? 0 : 1);

                if (j2 != 2) {
                    for (int k2 = k; k2 < l; ++k2) {
                        if (j2 <= 0 || k2 != k && k2 != l - 1) {
                            blockposition_mutableblockposition.set(l1, k2, i2);
                            BlockState iblockdata = this.level.getBlockState(blockposition_mutableblockposition);

                            if (!(iblockdata.getBlock() instanceof WaterlilyBlock) && Shapes.joinIsNotEmpty(iblockdata.getCollisionShape(this.level, blockposition_mutableblockposition).move((double) l1, (double) k2, (double) i2), voxelshape, BooleanOp.AND)) {
                                f += PaperBoatUtils.instance.isEnabled() ? PaperBoatUtils.getBlockSlipperiness(BuiltInRegistries.BLOCK.getKey(iblockdata.getBlock()).toString()) : iblockdata.getBlock().getFriction();
                                ++k1;
                            }
                        }
                    }
                }
            }
        }

        return f / (float) k1;
    }

    @Override
    public void checkFallDamage(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
        if (!PaperBoatUtils.fallDamage) {
            return;
        }
        super.checkFallDamage(heightDifference, onGround, state, landedPosition);
    }

    private void floatBoat() {
        double d0 = -0.03999999910593033D;
        double d1 = PaperBoatUtils.instance.isEnabled() ? PaperBoatUtils.gravityForce : (this.isNoGravity() ? 0.0D : -0.03999999910593033D);
        double d2 = 0.0D;

        // Mojang name: invFriction
        Util.setSuperPrivateField(this, "p", 0.05f);

        // Mojang name: oldStatus
        Status status1 = (Status) Util.getSuperPrivateField(this, "aM");


        if (status1 == Boat.Status.IN_AIR && this.status != Boat.Status.IN_AIR && this.status != Boat.Status.ON_LAND) {
            Util.setSuperPrivateField(this, "aJ", this.getY(1.0D));
            this.move(MoverType.SELF, new Vec3(0.0, ((double) (this.getWaterLevelAbove() - this.getBbHeight()) + 0.101D) - this.getY(), 0.0)); // Paper
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D)); // Paper
            // Mojang name: lastYd
            Util.setSuperPrivateField(this, "aN", 0.0);
            this.status = Boat.Status.IN_WATER;
        } else {
            // Mojang name: waterLevel
            double waterLevel = (double) Util.getSuperPrivateField(this, "aJ");
            // Mojang name: invFriction
            float invFriction = (float) Util.getSuperPrivateField(this, "p");
            if (this.status == Boat.Status.IN_WATER) {
                d2 = (waterLevel - this.getY()) / (double) this.getBbHeight();
                // Mojang name: invFriction
                Util.setSuperPrivateField(this, "p", surfacewaterCheck(0.9f));
            } else if (this.status == Boat.Status.UNDER_FLOWING_WATER) {
                d1 = -7.0E-4D;
                // Mojang name: invFriction
                Util.setSuperPrivateField(this, "p", underwaterCheck(0.9f));
            } else if (this.status == Boat.Status.UNDER_WATER) {
                d2 = 0.009999999776482582D;
                // Mojang name: invFriction
                Util.setSuperPrivateField(this, "p", underwaterCheck(0.45f));
            } else if (this.status == Boat.Status.IN_AIR) {
                // Mojang name: invFriction
                Util.setSuperPrivateField(this, "p", 0.9f);
            } else if (this.status == Boat.Status.ON_LAND) {
                // Mojang name: landFriction
                float landFriction = (float) Util.getSuperPrivateField(this, "aK");
                // Mojang name: invFriction
                Util.setSuperPrivateField(this, "p", landFriction);
                if (this.getControllingPassenger() instanceof Player) {
                    // Mojang name: landFriction
                    Util.setSuperPrivateField(this, "aK", landFriction / 2.0);
                }
            }

            Vec3 vec3d = this.getDeltaMovement();

            // Mojang name: invFriction
            invFriction = (float) Util.getSuperPrivateField(this, "p");
            this.setDeltaMovement(vec3d.x * (double) invFriction, vec3d.y + d1, vec3d.z * (double) invFriction);

            // Mojang name: deltaRotation
            float deltaRotation = (float) Util.getSuperPrivateField(this, "r");
            Util.setSuperPrivateField(this, "r", deltaRotation * invFriction);
            if (d2 > 0.0D) {
                Vec3 vec3d1 = this.getDeltaMovement();
                this.setDeltaMovement(vec3d1.x, (vec3d1.y + d2 * 0.06153846016296973D) * 0.75D, vec3d1.z);
            }
        }

    }

    private float underwaterCheck(float ori) {
        if (!PaperBoatUtils.instance.isEnabled() || !PaperBoatUtils.underwaterControl) {
            return ori;
        }
        return PaperBoatUtils.getBlockSlipperiness("minecraft:water");
    }

    private float surfacewaterCheck(float ori) {
        if (!PaperBoatUtils.instance.isEnabled() || !PaperBoatUtils.surfaceWaterControl) {
            return ori;
        }
        return PaperBoatUtils.getBlockSlipperiness("minecraft:water");
    }

    private void controlBoat() {
        if (this.isVehicle()) {
            float f = 0.0F;

            // Mojang name is local var name
            boolean inputLeft = (boolean) Util.getSuperPrivateField(this, "aF");
            boolean inputRight = (boolean) Util.getSuperPrivateField(this, "aG");
            boolean inputUp = (!PaperBoatUtils.instance.isEnabled() || PaperBoatUtils.allowAccelStacking) && (boolean) Util.getSuperPrivateField(this, "aH");
            boolean inputDown = (!PaperBoatUtils.instance.isEnabled() || PaperBoatUtils.allowAccelStacking) && (boolean) Util.getSuperPrivateField(this, "aI");
            float deltaRotation = (float) Util.getSuperPrivateField(this, "r");

            if (inputLeft) {
                deltaRotation--;
                // Mojang name: deltaRotation
                Util.setSuperPrivateField(this, "r", deltaRotation);
            }

            if (inputRight) {
                deltaRotation++;
                // Mojang name: deltaRotation
                Util.setSuperPrivateField(this, "r", deltaRotation);
            }

            if (inputLeft != inputRight && !inputUp && !inputDown) {
                f += PaperBoatUtils.instance.isEnabled() ? PaperBoatUtils.forwardsAcceleration : 0.005F;
            }

            this.setYRot(this.getYRot() + deltaRotation);

            if (inputUp) {
                f += PaperBoatUtils.instance.isEnabled() ? PaperBoatUtils.forwardsAcceleration : 0.04F;
            }

            if (inputDown) {
                f -= PaperBoatUtils.instance.isEnabled() ? PaperBoatUtils.backwardsAcceleration : 0.005F;
            }

            this.setDeltaMovement(this.getDeltaMovement().add((double) (Mth.sin(-this.getYRot() * 0.017453292F) * f), 0.0D, (double) (Mth.cos(this.getYRot() * 0.017453292F) * f)));
            this.setPaddleState(inputRight && !inputLeft || inputUp, inputLeft && !inputRight || inputUp);
        }
    }
}