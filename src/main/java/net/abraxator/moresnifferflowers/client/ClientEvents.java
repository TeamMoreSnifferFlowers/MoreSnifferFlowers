package net.abraxator.moresnifferflowers.client;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.model.entity.BoblingModel;
import net.abraxator.moresnifferflowers.client.particle.FlyParticle;
import net.abraxator.moresnifferflowers.client.renderer.block.AmbushBlockEntityRenderer;
import net.abraxator.moresnifferflowers.client.renderer.entity.BoblingRenderer;
import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.abraxator.moresnifferflowers.init.ModEntityTypes;
import net.abraxator.moresnifferflowers.init.ModParticles;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.locating.IModFile;

@Mod.EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onEntityRenderersRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BoblingModel.LAYER_LOCATION, BoblingModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.BOBLING.get(), BoblingRenderer::new);
    }

    @SubscribeEvent
    public static void blockRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.AMBUSH.get(), AmbushBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.FLY.get(), FlyParticle.Provider::new);
    }

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if(event.getPackType() == PackType.CLIENT_RESOURCES) {
            IModFileInfo iModFileInfo = ModList.get().getModFileById(MoreSnifferFlowers.MOD_ID);
            if(iModFileInfo == null) {
                MoreSnifferFlowers.LOGGER.error("Could not find More Sniffer Flowers mod file info; built-in resource packs will be missing!");
            }

            IModFile modFile = iModFileInfo.getFile();
            event.addRepositorySource(pOnLoad -> {
                Pack pack = Pack.readMetaAndCreate(
                        MoreSnifferFlowers.loc("rtx_moresnifferflowers").toString(),
                        Component.literal("RTX More Sniffer Flowers"),
                        false,
                        pId -> new PathPackResources(pId, modFile.findResource("resourcepacks/rtx_moresnifferflowers"), true),
                        PackType.CLIENT_RESOURCES,
                        Pack.Position.TOP,
                        PackSource.BUILT_IN);
                if(pack != null) {
                    pOnLoad.accept(pack);
                }
            });
        }
    }
}
