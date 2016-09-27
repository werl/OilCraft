package me.werl.oilcraft.client.gui;

import me.werl.oilcraft.data.ModData;
import me.werl.oilcraft.inventory.ContainerSBRefinery;
import me.werl.oilcraft.tileentity.TileHeatGenerator;
import me.werl.oilcraft.tileentity.TileSBRefinery;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.tools.cmd.gen.AnyVals;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSBRefinery extends GuiContainer {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModData.RESOURCE_PREFIX +
            "textures/gui/container/sb_refinery.png");
    private final IInventory refinery;
    private final TileSBRefinery tile;

    public GuiSBRefinery(InventoryPlayer player, IInventory refinery) {
        super(new ContainerSBRefinery(player, refinery));

        this.refinery = refinery;
        tile = (TileSBRefinery)refinery;

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        if (TileHeatGenerator.isBurning(this.refinery)) {
            int k = this.getBurnLeftScaled(14);
            this.drawTexturedModalRect(i + 14, j + 59 + 12 - k, 176, 12 - k, 14, k + 1);
        }

        int l = getHeatScaled(56);
        this.drawTexturedModalRect(i + 6, j + 15 + 56 - l, 176, 70 - l, 4, l);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int offsetX = (this.width - this.xSize) / 2;
        int offsetY = (this.height - this.ySize) / 2;
        drawTextWhenInArea(refinery.getField(2) + "C",mouseX, mouseY, 6, 15, 10, 71, offsetX, offsetY);

        List<String> inputToolTip = new ArrayList<>();
        if(tile.inputTank.getFluid() != null) {
            inputToolTip.add(tile.inputTank.getFluidType().getLocalizedName(tile.inputTank.getFluid()));
            inputToolTip.add(tile.inputTank.getFluidAmount() + "mb");
        } else {
            inputToolTip.add("Empty");
        }
        drawTextWhenInArea(inputToolTip, mouseX, mouseY, 33, 19, 49, 67, offsetX, offsetY);

        List<String> outputToolTip = new ArrayList<>();
        if(tile.outputTank.getFluid() != null) {
            outputToolTip.add(tile.outputTank.getFluidType().getLocalizedName(tile.outputTank.getFluid()));
            outputToolTip.add(tile.outputTank.getFluidAmount() + "mb");
        } else {
            outputToolTip.add("Empty");
        }
        drawTextWhenInArea(outputToolTip, mouseX, mouseY, 127, 19, 143, 67, offsetX, offsetY);

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    private int getBurnLeftScaled(int pixels) {
        int i = refinery.getField(1);

        if(i == 0)
            i = 200;
        return this.refinery.getField(0) * pixels / i;
    }

    private int getHeatScaled(int pixels) {
        int i = refinery.getField(3);

        if(i == 0){
            return 0;
        }
        return refinery.getField(2) * pixels / i;
    }

    protected void drawTextWhenInArea(String text, int mouseX, int mouseY, int startX, int startY, int endX, int endY, int offsetX, int offsetY) {
        List<String> textList = new ArrayList<>();
        textList.add(text);
        drawTextWhenInArea(textList, mouseX, mouseY, startX, startY, endX, endY, offsetX, offsetY);
    }

    protected void drawTextWhenInArea(List<String> text, int mouseX, int mouseY, int startX, int startY, int endX, int endY, int offsetX, int offsetY) {
        int SOX = startX + offsetX;
        int SOY = startY + offsetY;
        int EOX = endX + offsetX;
        int EOY = endY + offsetY;
        int MOX = mouseX - offsetX;
        int MOY = mouseY - offsetY;
        if(mouseX >= SOX && mouseX <= EOX && mouseY >= SOY && mouseY <= EOY) {
            drawHoveringText(text, MOX, MOY);
        }
    }


}
