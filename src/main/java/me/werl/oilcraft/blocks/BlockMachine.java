package me.werl.oilcraft.blocks;

import me.werl.oilcraft.OilCraft;
import me.werl.oilcraft.data.EnumMachines;
import me.werl.oilcraft.data.GuiData;
import me.werl.oilcraft.tileentity.TileHeatGenerator;
import me.werl.oilcraft.tileentity.interfaces.IActivatableTile;
import me.werl.oilcraft.tileentity.interfaces.IFacingTile;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockMachine extends BlockContainer {

    public static final IProperty<EnumMachines> MACHINE_TYPE = PropertyEnum.create("machine_type", EnumMachines.class);
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public BlockMachine() {
        super(Material.IRON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));
        this.setCreativeTab(OilCraft.creativeTab);

        this.setRegistryName("block_machine");
        this.setUnlocalizedName(this.getRegistryName().toString());
    }

    public static void setState(boolean active, EnumFacing facing, World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        TileEntity tile = world.getTileEntity(pos);

        world.setBlockState(pos, state.withProperty(FACING, facing).withProperty(ACTIVE, active), 3);

        if(tile != null) {
            tile.validate();
            world.setTileEntity(pos, tile);
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.setDefaultFacing(worldIn, pos, state);
    }

    private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote)
        {
            IBlockState iblockstate = worldIn.getBlockState(pos.north());
            IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
            IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
            IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
            EnumFacing enumfacing = state.getValue(FACING);

            if (enumfacing == EnumFacing.NORTH && iblockstate.isFullBlock() && !iblockstate1.isFullBlock())
            {
                enumfacing = EnumFacing.SOUTH;
            }
            else if (enumfacing == EnumFacing.SOUTH && iblockstate1.isFullBlock() && !iblockstate.isFullBlock())
            {
                enumfacing = EnumFacing.NORTH;
            }
            else if (enumfacing == EnumFacing.WEST && iblockstate2.isFullBlock() && !iblockstate3.isFullBlock())
            {
                enumfacing = EnumFacing.EAST;
            }
            else if (enumfacing == EnumFacing.EAST && iblockstate3.isFullBlock() && !iblockstate2.isFullBlock())
            {
                enumfacing = EnumFacing.WEST;
            }

            TileEntity tile = worldIn.getTileEntity(pos);
            if(tile instanceof IFacingTile)
                ((IFacingTile)tile).setFacing(enumfacing);

            worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing).withProperty(ACTIVE, false), 2);

        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing facing = placer.getHorizontalFacing().getOpposite();
        TileEntity tile = worldIn.getTileEntity(pos);

        worldIn.setBlockState(pos, state.withProperty(FACING, facing).withProperty(ACTIVE, false), 3);
        setTileFacing(facing, worldIn, pos);
        if(tile instanceof IActivatableTile)
            ((IActivatableTile)tile).setActive(false);
    }

    public EnumFacing getTileFacing(IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof IFacingTile)
            return ((IFacingTile)tile).getFacing();
        return null;
    }

    public void setTileFacing(EnumFacing facing, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);

        if(tile instanceof IFacingTile)
            ((IFacingTile)tile).setFacing(facing);
    }

    public boolean getTileActive(IBlockAccess world, BlockPos pos) {
        final TileEntity tile = world.getTileEntity(pos);
        return tile instanceof IActivatableTile && ((IActivatableTile) tile).isActive();
    }

    public void setTileActive(boolean isActive, IBlockAccess world, BlockPos pos) {
        final TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof IActivatableTile)
            ((IActivatableTile)tile).setActive(isActive);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        switch(meta) {
            case 0:
                return new TileHeatGenerator();
            default:
                return null;
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tile = worldIn.getTileEntity(pos);

        if(tile instanceof IInventory) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tile);
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand,
                                    @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote)
            return true;
        else {
            TileEntity tile = worldIn.getTileEntity(pos);
            if(tile instanceof TileHeatGenerator) {
                playerIn.openGui(OilCraft.instance, GuiData.HEAT_GENERATOR_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
            return true;
        }
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        IBlockState state = world.getBlockState(pos);
        for (IProperty<?> prop : state.getProperties().keySet())
        {
            if (prop.getName().equals("facing") || prop.getName().equals("rotation"))
            {
                world.setBlockState(pos, state.cycleProperty(prop));
                return true;
            }
        }
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        EnumFacing facing = getTileFacing(worldIn, pos);
        boolean isActive = getTileActive(worldIn, pos);

        return state.withProperty(FACING, facing).withProperty(ACTIVE, isActive);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        EnumFacing facing = getTileFacing(world, pos);
        boolean isActive = getTileActive(world, pos);
        if(facing == null)
            facing = EnumFacing.NORTH;


        return state.withProperty(FACING, facing).withProperty(ACTIVE, isActive);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(MACHINE_TYPE).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, MACHINE_TYPE, FACING, ACTIVE);
    }
}
