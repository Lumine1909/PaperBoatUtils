package io.github.lumine1909;

public enum Modes {
    BROKEN_SLIME_RALLY,//0
    BROKEN_SLIME_RALLY_BLUE,//1
    BROKEN_SLIME_BA_NOFD,//2
    BROKEN_SLIME_PARKOUR,//3
    BROKEN_SLIME_BA_BLUE_NOFD,//4
    BROKEN_SLIME_PARKOUR_BLUE,//5
    BROKEN_SLIME_BA,//6
    BROKEN_SLIME_BA_BLUE,//7
    RALLY,//8
    RALLY_BLUE,//9
    BA_NOFD,//10
    PARKOUR,//11
    BA_BLUE_NOFD,//12
    PARKOUR_BLUE,//13
    BA,//14
    BA_BLUE,//15
    JUMP_BLOCKS,//16
    BOOSTER_BLOCKS,//17
    DEFAULT_ICE,//18
    DEFAULT_BLUE_ICE,//19
    ;

    public static void setMode(Modes mode) {
        switch (mode) {
            case RALLY:
                PaperBoatUtils.setAllBlocksSlipperiness(0.98f);
                PaperBoatUtils.setFallDamage(false);
                PaperBoatUtils.setAirControl(true);
                PaperBoatUtils.setStepSize(1.25f);
                return;
            case RALLY_BLUE:
                PaperBoatUtils.setAllBlocksSlipperiness(0.989f);
                PaperBoatUtils.setFallDamage(false);
                PaperBoatUtils.setAirControl(true);
                PaperBoatUtils.setStepSize(1.25f);
                return;
            case BA_NOFD:
                PaperBoatUtils.setFallDamage(false);
                PaperBoatUtils.setAirControl(true);
                PaperBoatUtils.setBlockSlipperiness("minecraft:air", 0.98f);
                PaperBoatUtils.setStepSize(1.25f);
                PaperBoatUtils.setWaterElevation(true);
                return;
            case PARKOUR:
                PaperBoatUtils.setAllBlocksSlipperiness(0.98f);
                PaperBoatUtils.setFallDamage(false);
                PaperBoatUtils.setAirControl(true);
                PaperBoatUtils.setJumpForce(0.36f);
                PaperBoatUtils.setStepSize(0.5f);
                return;
            case BA_BLUE_NOFD:
                PaperBoatUtils.setFallDamage(false);
                PaperBoatUtils.setAirControl(true);
                PaperBoatUtils.setBlockSlipperiness("minecraft:air", 0.989f);
                PaperBoatUtils.setStepSize(1.25f);
                PaperBoatUtils.setWaterElevation(true);
                return;
            case PARKOUR_BLUE:
                PaperBoatUtils.setAllBlocksSlipperiness(0.989f);
                PaperBoatUtils.setFallDamage(false);
                PaperBoatUtils.setAirControl(true);
                PaperBoatUtils.setJumpForce(0.36f);
                PaperBoatUtils.setStepSize(0.5f);
                return;
            case BA:
                PaperBoatUtils.setAirControl(true);
                PaperBoatUtils.setBlockSlipperiness("minecraft:air", 0.98f);
                PaperBoatUtils.setStepSize(1.25f);
                PaperBoatUtils.setWaterElevation(true);
                return;
            case BA_BLUE:
                PaperBoatUtils.setAirControl(true);
                PaperBoatUtils.setBlockSlipperiness("minecraft:air", 0.989f);
                PaperBoatUtils.setStepSize(1.25f);
                PaperBoatUtils.setWaterElevation(true);
                return;
            case BROKEN_SLIME_RALLY:
                PaperBoatUtils.setAllBlocksSlipperiness(0.98f);
                PaperBoatUtils.setFallDamage(false);
                PaperBoatUtils.setAirControl(true);
                PaperBoatUtils.setStepSize(1.25f);
                PaperBoatUtils.breakSlimePlease();
                return;
            case BROKEN_SLIME_RALLY_BLUE:
                PaperBoatUtils.setAllBlocksSlipperiness(0.989f);
                PaperBoatUtils.setFallDamage(false);
                PaperBoatUtils.setAirControl(true);
                PaperBoatUtils.setStepSize(1.25f);
                PaperBoatUtils.breakSlimePlease();
                return;
            case BROKEN_SLIME_BA_NOFD:
                PaperBoatUtils.setFallDamage(false);
                PaperBoatUtils.setAirControl(true);
                PaperBoatUtils.setBlockSlipperiness("minecraft:air", 0.98f);
                PaperBoatUtils.setStepSize(1.25f);
                PaperBoatUtils.setWaterElevation(true);
                PaperBoatUtils.breakSlimePlease();
                return;
            case BROKEN_SLIME_PARKOUR:
                PaperBoatUtils.setAllBlocksSlipperiness(0.98f);
                PaperBoatUtils.setFallDamage(false);
                PaperBoatUtils.setAirControl(true);
                PaperBoatUtils.setJumpForce(0.36f);
                PaperBoatUtils.setStepSize(0.5f);
                PaperBoatUtils.breakSlimePlease();
                return;
            case BROKEN_SLIME_BA_BLUE_NOFD:
                PaperBoatUtils.setFallDamage(false);
                PaperBoatUtils.setAirControl(true);
                PaperBoatUtils.setBlockSlipperiness("minecraft:air", 0.989f);
                PaperBoatUtils.setStepSize(1.25f);
                PaperBoatUtils.setWaterElevation(true);
                PaperBoatUtils.breakSlimePlease();
                return;
            case BROKEN_SLIME_PARKOUR_BLUE:
                PaperBoatUtils.setAllBlocksSlipperiness(0.989f);
                PaperBoatUtils.setFallDamage(false);
                PaperBoatUtils.setAirControl(true);
                PaperBoatUtils.setJumpForce(0.36f);
                PaperBoatUtils.setStepSize(0.5f);
                PaperBoatUtils.breakSlimePlease();
                return;
            case BROKEN_SLIME_BA:
                PaperBoatUtils.setAirControl(true);
                PaperBoatUtils.setBlockSlipperiness("minecraft:air", 0.98f);
                PaperBoatUtils.setStepSize(1.25f);
                PaperBoatUtils.setWaterElevation(true);
                PaperBoatUtils.breakSlimePlease();
                return;
            case BROKEN_SLIME_BA_BLUE:
                PaperBoatUtils.setAirControl(true);
                PaperBoatUtils.setBlockSlipperiness("minecraft:air", 0.989f);
                PaperBoatUtils.setStepSize(1.25f);
                PaperBoatUtils.setWaterElevation(true);
                PaperBoatUtils.breakSlimePlease();
                return;
            case JUMP_BLOCKS:
                PaperBoatUtils.setBlockSetting(PaperBoatUtils.PerBlockSettingType.jumpForce, "minecraft:orange_concrete", 0.36f);// ~1 block
                PaperBoatUtils.setBlockSetting(PaperBoatUtils.PerBlockSettingType.jumpForce, "minecraft:black_concrete", 0.0f);// no jump
                PaperBoatUtils.setBlockSetting(PaperBoatUtils.PerBlockSettingType.jumpForce, "minecraft:green_concrete", 0.5f);// ~2-3 block
                PaperBoatUtils.setBlockSetting(PaperBoatUtils.PerBlockSettingType.jumpForce, "minecraft:yellow_concrete", 0.18f);// ~0.5 blocks
                return;
            case BOOSTER_BLOCKS:
                PaperBoatUtils.setBlockSetting(PaperBoatUtils.PerBlockSettingType.forwardsAccel, "minecraft:magenta_glazed_terracotta", 0.08f);// double accel
                PaperBoatUtils.setBlockSetting(PaperBoatUtils.PerBlockSettingType.yawAccel, "minecraft:light_gray_glazed_terracotta", 0.08f);// double yaw accel
                return;
            case DEFAULT_ICE:
                PaperBoatUtils.setAllBlocksSlipperiness(0.98f);
                return;
            case DEFAULT_BLUE_ICE:
                PaperBoatUtils.setAllBlocksSlipperiness(0.985f);
                return;
        }
    }
}