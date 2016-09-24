package me.werl.oilcraft.client.gui;

import me.werl.oilcraft.data.ModData;
import me.werl.oilcraft.inventory.ContainerSBRefinery;
import me.werl.oilcraft.tileentity.TileHeatGenerator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSBRefinery extends GuiContainer {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModData.RESOURCE_PREFIX +
            "textures/gui/container/sb_refinery.png");
    private final IInventory generator;

    public GuiSBRefinery(InventoryPlayer player, IInventory generator) {
        super(new ContainerSBRefinery(player, generator));

        this.generator = generator;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        if (TileHeatGenerator.isBurning(this.generator)) {
            int k = this.getBurnLeftScaled(14);
            this.drawTexturedModalRect(i + 14, j + 59 + 12 - k, 176, 12 - k, 14, k + 1);
        }

        int l = getHeatScaled(56);
        this.drawTexturedModalRect(i + 6, j + 15 + 56 - l, 176, 70 - l, 4, l);
    }

    private int getBurnLeftScaled(int pixels) {
        int i = generator.getField(1);

        if(i == 0)
            i = 200;
        return this.generator.getField(0) * pixels / i;
    }

    private int getHeatScaled(int pixles) {
        int i = generator.getField(3);

        if(i == 0){
            return 0;
        }
        return generator.getField(2) * pixles / i;
    }

}
