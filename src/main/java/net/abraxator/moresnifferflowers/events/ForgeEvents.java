package net.abraxator.moresnifferflowers.events;

import cpw.mods.jarhandling.impl.Jar;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.GiantCropBlockEntity;
import net.abraxator.moresnifferflowers.init.ModAdvancementCritters;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModParticles;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.abraxator.moresnifferflowers.items.JarOfBonmeelItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PermissionsChangedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeEvents {
    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        var item = event.getEntity().getItemInHand(event.getHand()).getItem();
        var isItem = item instanceof JarOfBonmeelItem;
        var isBlock = event.getLevel().getBlockState(event.getPos()).is(ModTags.ModBlockTags.CROPS_FERTIABLE_BY_FBM);
        
        if(isItem && isBlock) {
            var context = new UseOnContext(event.getLevel(), event.getEntity(), event.getHand(), event.getItemStack(), event.getHitVec());
            
            event.setCanceled(true);
            ((JarOfBonmeelItem) item).useOn(context);
        }
    }
    
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if(event.getState().is(ModBlocks.AMBER.get()) && event.getLevel() instanceof ServerLevel serverLevel) {
            fireFlyLogic(event.getState(), serverLevel, event.getPos(), event.getPlayer(), event);
        }

        if(event.getLevel().getBlockEntity(event.getPos()) instanceof GiantCropBlockEntity entity) {
            BlockPos.betweenClosed(entity.pos1, entity.pos2).forEach(blockPos -> {
                event.getLevel().destroyBlock(blockPos, true);
            });
        }
    }

    @SubscribeEvent
    public static void onGetAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        if(event.getAdvancement().getId().equals(new ResourceLocation("minecraft", "husbandry/obtain_sniffer_egg")) && event.getEntity() instanceof ServerPlayer serverPlayer) {
            ModAdvancementCritters.EARN_SNIFFER_ADVANCEMENT.trigger(serverPlayer);
        }
    }

    private static void fireFlyLogic(BlockState state, ServerLevel serverLevel, BlockPos pos, Player player, Event event) {
        List<ItemStack> list = Block.getDrops(state, serverLevel, pos, serverLevel.getBlockEntity(pos), player, player.getMainHandItem());
        RandomSource randomSource = serverLevel.getRandom();
        if ((randomSource.nextInt(list.size())) == list.size()) {
            event.setCanceled(true);
            serverLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            serverLevel.sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK, state),
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                   10, 0, 0, 0, 0);
            Vec3 vecPos = pos.getCenter();
            serverLevel.sendParticles(
                    ModParticles.FLY.get(),
                    vecPos.x() + (randomSource.nextInt(10) - 5) / 10,
                    vecPos.y() + (randomSource.nextInt(10) - 5) / 10,
                    vecPos.z() + (randomSource.nextInt(10) - 5) / 10,
                    0, 0, 0, 0, 0);
        }
    }
}
