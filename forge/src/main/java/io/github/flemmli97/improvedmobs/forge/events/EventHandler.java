package io.github.flemmli97.improvedmobs.forge.events;

import io.github.flemmli97.improvedmobs.ImprovedMobs;
import io.github.flemmli97.improvedmobs.commands.IMCommand;
import io.github.flemmli97.improvedmobs.events.EventCalls;
import io.github.flemmli97.improvedmobs.forge.capability.TileCap;
import io.github.flemmli97.improvedmobs.forge.config.ConfigLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    public static final ResourceLocation tileCap = new ResourceLocation(ImprovedMobs.MODID, "opened_flag");

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<BlockEntity> event) {
        if (event.getObject() instanceof Container)
            event.addCapability(tileCap, new TileCap());
    }

    /**
     * Move the init of default config to {@link WorldEvent.Load} cause {@link ServerStartingEvent} is too late.
     * Entities are already loaded at that point
     */
    @SubscribeEvent
    public void worldLoad(WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerLevel && ((ServerLevel) event.getWorld()).dimension() == Level.OVERWORLD) {
            ConfigLoader.serverInit((ServerLevel) event.getWorld());
        }
    }

    @SubscribeEvent
    public void commands(RegisterCommandsEvent event) {
        IMCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onEntityLoad(EntityJoinWorldEvent e) {
        if (e.getEntity() instanceof Mob mob) {
            EventCalls.onEntityLoad(mob);
        }
    }

    @SubscribeEvent
    public void hurtEvent(LivingHurtEvent e) {
        e.setAmount(EventCalls.hurtEvent(e.getEntityLiving(), e.getSource(), e.getAmount()));
    }

    @SubscribeEvent
    public void removeBoats(LivingEvent.LivingUpdateEvent event) {
        EventCalls.entityTick(event.getEntityLiving());
    }

    @SubscribeEvent
    public void attackEvent(LivingAttackEvent e) {
        if (EventCalls.onAttackEvent(e.getEntityLiving(), e.getSource()))
            e.setCanceled(true);
    }

    @SubscribeEvent
    public void openTile(PlayerInteractEvent.RightClickBlock e) {
        EventCalls.openTile(e.getPlayer(), e.getPos());
    }

    @SubscribeEvent
    public void equipPet(PlayerInteractEvent.EntityInteract e) {
        if (EventCalls.equipPet(e.getPlayer(), e.getHand(), e.getTarget()))
            e.setCanceled(true);
    }

    @SubscribeEvent
    public void projectileImpact(ProjectileImpactEvent e) {
        if (e.getEntity() instanceof Projectile proj && EventCalls.projectileImpact(proj, e.getRayTraceResult())) {
            e.setCanceled(true);
        }
    }

    //Note: Sodium-Forge breaks this since they modify explosion but dont call the forge event
    @SubscribeEvent
    public void explosion(ExplosionEvent.Detonate event) {
        EventCalls.explosion(event.getExplosion(), event.getExplosion().getExploder(), event.getAffectedEntities());
    }
}
