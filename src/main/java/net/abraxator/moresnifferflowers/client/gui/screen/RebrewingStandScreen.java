package net.abraxator.moresnifferflowers.client.gui.screen;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.gui.menu.RebrewingStandMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class RebrewingStandScreen extends AbstractContainerScreen<RebrewingStandMenu> {
    public static final ResourceLocation TEXTURE = MoreSnifferFlowers.loc("textures/gui/container/rebrewing_stand.png");
    private static final int[] BUBBLELENGTHS = new int[]{27, 22, 17, 12, 8, 4, 0};
    
    public RebrewingStandScreen(RebrewingStandMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void render(GuiGraphics p_283479_, int p_283661_, int p_281248_, float p_281886_) {
        this.renderBackground(p_283479_);
        super.render(p_283479_, p_283661_, p_281248_, p_281886_);
        this.renderTooltip(p_283479_, p_281248_, p_281248_);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float p_97788_, int p_97789_, int p_97790_) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        int fuel = menu.getFuel();
        int progress = menu.getBrewingTicks();
        int renderFuel;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        
        if(fuel > 0) {
            renderFuel = fuel == 1 ? 2 : fuel == 16 ? 18 : fuel + 1;
            guiGraphics.blit(TEXTURE, x + 55, y + 39, 176, 29, renderFuel, 4);
        }
        
        if(progress > 0) {
            var bubbleFactor = BUBBLELENGTHS[progress / 2 % 7];
            System.out.println(bubbleFactor);
            guiGraphics.blit(TEXTURE, x + 59, y + 72 + 27 - bubbleFactor, 186, 27 - bubbleFactor, 11, bubbleFactor);   
        }
    }
}
