package net.abraxator.moresnifferflowers.entities;

import net.abraxator.moresnifferflowers.init.ModEntityTypes;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.advancements.critereon.EffectsChangedTrigger;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import org.joml.Vector3f;

public class DragonflyProjectile extends ThrowableItemProjectile {
    public DragonflyProjectile(EntityType<? extends DragonflyProjectile> entityType, Level pLevel) {
        super(entityType, pLevel);
    }

    public DragonflyProjectile(Level pLevel, Player player) {
        super(ModEntityTypes.DRAGONFLY.get(), player, pLevel);
        this.setOwner(player);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.DRAGONFLY.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if(pResult.getEntity() instanceof LivingEntity entity) {
            entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 140, 2));
            entity.hurt(this.damageSources().thrown(this, this.getOwner()), 0);
        }
        
        discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        if(level() instanceof ServerLevel serverLevel) {
            var particle = new ItemParticleOption(ParticleTypes.ITEM, ModItems.DRAGONFLY.get().getDefaultInstance());
            serverLevel.sendParticles(particle, getX(), getY(), getZ(), 10, Mth.nextDouble(random, 0, 0.3), Mth.nextDouble(random, 0, 0.3), Mth.nextDouble(random, 0, 0.3), 0);
        }
        super.onHitBlock(pResult);
        discard();
    }
}
