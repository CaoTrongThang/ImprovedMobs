package io.github.flemmli97.improvedmobs.fabric.mixin;

import io.github.flemmli97.improvedmobs.events.EventCalls;
import io.github.flemmli97.improvedmobs.utils.EntityFlags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntity.class})
public abstract class LivingEntityMixin {

    @ModifyVariable(method = "actuallyHurt", at = @At(value = "HEAD"), argsOnly = true)
    private float onDamage(float amount, DamageSource source) {
        return EventCalls.hurtEvent((LivingEntity) (Object) this, source, amount);
    }

    @Inject(method = "hurt", at = @At(value = "HEAD"), cancellable = true)
    private void hurtCheck(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        if (EventCalls.onAttackEvent((LivingEntity) (Object) this, source)) {
            info.setReturnValue(false);
            info.cancel();
        }
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void onDamage(DamageSource source, float damageAmount, CallbackInfoReturnable<Boolean> cir) {

        LivingEntity entity = (LivingEntity) (Object) this;

        if (source.getSource() instanceof ArrowEntity) {
            //Increase damage dealt by Arrows
            entity.damage(entity.getWorld().getDamageSources().generic(), damageAmount * EntityFlags.projMult);
        }

        if ("explosion.player".equals(source.getName())) {
            //Increase damage dealt by Explosions
            entity.damage(entity.getWorld().getDamageSources().generic(), damageAmount * EntityFlags.explosionMult);
        }
    }
}
