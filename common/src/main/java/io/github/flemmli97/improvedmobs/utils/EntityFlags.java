package io.github.flemmli97.improvedmobs.utils;

import io.github.flemmli97.improvedmobs.mixinhelper.IEntityData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class EntityFlags {

    public static final String TAG_ID = "IMFlags";
    public static final String SERVER_ENTITY_TAG_ID = "ServerSideEntityID";

    public boolean ladderClimber;

    public static EntityFlags get(Entity entity) {
        return ((IEntityData) entity).getFlags();
    }

    public boolean modifyArmor, modifyHeldItems, modifyAttributes, enchantGear;

    public boolean isThrownEntity;

    public boolean isVariedSize;

    public FlagType canBreakBlocks = FlagType.UNDEF;
    public FlagType canFly = FlagType.UNDEF;

    private int shieldCooldown;

    public static float magicRes, projMult = 1, explosionMult = 1;

    public ResourceLocation serverSideEntityID;

    public void disableShield() {
        this.shieldCooldown = 120;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("CanBreakBlocks", this.canBreakBlocks.ordinal());
        tag.putInt("CanFly", this.canFly.ordinal());
        tag.putBoolean("ModifiedArmor", this.modifyArmor);
        tag.putBoolean("ModifiedHeld", this.modifyHeldItems);
        tag.putBoolean("ModifiedAttributes", this.modifyAttributes);
        tag.putBoolean("GearEnchanted", this.enchantGear);
        tag.putBoolean("IsThrown", this.isThrownEntity);
        tag.putBoolean("IsVariedSize", this.isVariedSize);
        tag.putFloat("MagicRes", this.magicRes);
        tag.putFloat("ProjBoost", this.projMult);
        tag.putFloat("explosionMult", this.explosionMult);
        return tag;
    }

    public void load(CompoundTag nbt) {
        this.canBreakBlocks = FlagType.values()[nbt.getInt("CanBreakBlocks")];
        this.canFly = FlagType.values()[nbt.getInt("CanFly")];
        this.modifyArmor = nbt.getBoolean("ModifiedArmor");
        this.modifyHeldItems = nbt.getBoolean("ModifiedHeld");
        this.modifyAttributes = nbt.getBoolean("ModifiedAttributes");
        this.enchantGear = nbt.getBoolean("GearEnchanted");
        this.isThrownEntity = nbt.getBoolean("IsThrown");
        this.isVariedSize = nbt.getBoolean("IsVariedSize");
        this.magicRes = nbt.getFloat("MagicRes");
        this.projMult = nbt.contains("ProjBoost") ? nbt.getFloat("ProjBoost") : 1;
        this.explosionMult = nbt.contains("ExplosionBoost") ? nbt.getFloat("ExplosionBoost") : 1;
        if (nbt.contains(SERVER_ENTITY_TAG_ID))
            this.serverSideEntityID = ResourceLocation.parse(nbt.getString(SERVER_ENTITY_TAG_ID));
    }

    public boolean isShieldDisabled() {
        return --this.shieldCooldown > 0;
    }

    public enum FlagType {
        UNDEF,
        TRUE,
        FALSE,
    }
}
