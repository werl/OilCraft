package me.werl.oilcraft.client.model;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MeshDefinitionFix implements ItemMeshDefinition {

    private ModelResourceLocation location;

    public MeshDefinitionFix(ModelResourceLocation location) {
        this.location = location;
    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack) {
        return location;
    }
}
