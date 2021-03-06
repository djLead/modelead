package com.djlead.leadmod.blocks;

import com.djlead.leadmod.Reference;
import com.djlead.leadmod.sys.MyBlocks;
import com.djlead.leadmod.sys.MyTab;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Lead on 3-10-2015.
 */
public class LeavesBT extends BlockLeavesBase implements IShearable {
    int[] field_150128_a;
    @SideOnly(Side.CLIENT)
    protected int field_150127_b;
    protected IIcon[][] field_150129_M = new IIcon[2][];
    private static final String __OBFID = "CL_00000263";

    public LeavesBT() {
        super(Material.leaves, false);
        this.setTickRandomly(true);
        this.setCreativeTab(MyTab.CreaTab);
        this.setHardness(0.2F);
        this.setLightOpacity(1);
        this.setStepSound(soundTypeGrass);
        this.setBlockName("leavesbt");

    }

    @SideOnly(Side.CLIENT)
    public int getBlockColor() {
        double d0 = 0.5D;
        double d1 = 1.0D;
        return ColorizerFoliage.getFoliageColor(d0, d1);
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int p_149741_1_) {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_) {
        int l = 0;
        int i1 = 0;
        int j1 = 0;

        for (int k1 = -1; k1 <= 1; ++k1) {
            for (int l1 = -1; l1 <= 1; ++l1) {
                int i2 = p_149720_1_.getBiomeGenForCoords(p_149720_2_ + l1, p_149720_4_ + k1).getBiomeFoliageColor(p_149720_2_ + l1, p_149720_3_, p_149720_4_ + k1);
                l += (i2 & 16711680) >> 16;
                i1 += (i2 & 65280) >> 8;
                j1 += i2 & 255;
            }
        }

        return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
    }

    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
        byte b0 = 1;
        int i1 = b0 + 1;

        if (p_149749_1_.checkChunksExist(p_149749_2_ - i1, p_149749_3_ - i1, p_149749_4_ - i1, p_149749_2_ + i1, p_149749_3_ + i1, p_149749_4_ + i1)) {
            for (int j1 = -b0; j1 <= b0; ++j1) {
                for (int k1 = -b0; k1 <= b0; ++k1) {
                    for (int l1 = -b0; l1 <= b0; ++l1) {
                        Block block = p_149749_1_.getBlock(p_149749_2_ + j1, p_149749_3_ + k1, p_149749_4_ + l1);
                        if (block.isLeaves(p_149749_1_, p_149749_2_ + j1, p_149749_3_ + k1, p_149749_4_ + l1)) {
                            block.beginLeavesDecay(p_149749_1_, p_149749_2_ + j1, p_149749_3_ + k1, p_149749_4_ + l1);
                        }
                    }
                }
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int posX, int posY, int posZ, Random random) {
        if (!world.isRemote) {
            int l = world.getBlockMetadata(posX, posY, posZ);

            if ((l & 8) != 0 && (l & 4) == 0) {
                byte b0 = 4;
                int i1 = b0 + 1;
                byte b1 = 32;
                int j1 = b1 * b1;
                int k1 = b1 / 2;

                if (this.field_150128_a == null) {
                    this.field_150128_a = new int[b1 * b1 * b1];
                }

                int l1;

                if (world.checkChunksExist(posX - i1, posY - i1, posZ - i1, posX + i1, posY + i1, posZ + i1)) {
                    int i2;
                    int j2;

                    for (l1 = -b0; l1 <= b0; ++l1) {
                        for (i2 = -b0; i2 <= b0; ++i2) {
                            for (j2 = -b0; j2 <= b0; ++j2) {
                                Block block = world.getBlock(posX + l1, posY + i2, posZ + j2);

                                if (!block.canSustainLeaves(world, posX + l1, posY + i2, posZ + j2)) {
                                    if (block.isLeaves(world, posX + l1, posY + i2, posZ + j2)) {
                                        this.field_150128_a[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -2;
                                    } else {
                                        this.field_150128_a[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -1;
                                    }
                                } else {
                                    this.field_150128_a[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = 0;
                                }
                            }
                        }
                    }

                    for (l1 = 1; l1 <= 4; ++l1) {
                        for (i2 = -b0; i2 <= b0; ++i2) {
                            for (j2 = -b0; j2 <= b0; ++j2) {
                                for (int k2 = -b0; k2 <= b0; ++k2) {
                                    if (this.field_150128_a[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1] == l1 - 1) {
                                        if (this.field_150128_a[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2) {
                                            this.field_150128_a[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.field_150128_a[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2) {
                                            this.field_150128_a[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.field_150128_a[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + k2 + k1] == -2) {
                                            this.field_150128_a[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.field_150128_a[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + k2 + k1] == -2) {
                                            this.field_150128_a[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.field_150128_a[(i2 + k1) * j1 + (j2 + k1) * b1 + (k2 + k1 - 1)] == -2) {
                                            this.field_150128_a[(i2 + k1) * j1 + (j2 + k1) * b1 + (k2 + k1 - 1)] = l1;
                                        }

                                        if (this.field_150128_a[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1 + 1] == -2) {
                                            this.field_150128_a[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1 + 1] = l1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                l1 = this.field_150128_a[k1 * j1 + k1 * b1 + k1];

                if (l1 >= 0) {
                    world.setBlockMetadataWithNotify(posX, posY, posZ, l & -9, 4);
                } else {
                    this.removeLeaves(world, posX, posY, posZ);
                }
            }
        }
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int posX, int posY, int posZ, Random random) {
        if (world.canLightningStrikeAt(posX, posY + 1, posZ) && !World.doesBlockHaveSolidTopSurface(world, posX, posY - 1, posZ) && random.nextInt(15) == 1) {
            double d0 = (double) ((float) posX + random.nextFloat());
            double d1 = (double) posY - 0.05D;
            double d2 = (double) ((float) posZ + random.nextFloat());
            world.spawnParticle("dripWater", d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }

    private void removeLeaves(World world, int posX, int posY, int posZ) {
        this.dropBlockAsItem(world, posX, posY, posZ, world.getBlockMetadata(posX, posY, posZ), 0);
        world.setBlockToAir(posX, posY, posZ);
    }

    /**
     * Returns the quantity of items to drop on block destruction. Dropchance of Saplings
     */
    public int quantityDropped(Random random) {
        return random.nextInt(60) == 0 ? 1 : 0;
    }

    public Item getItemDropped(int p_149650_1_, Random random, int p_149650_3_) {
        return Item.getItemFromBlock(MyBlocks.sapplingBT);
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World random, int p_149690_2_, int p_149690_3_, int p_149690_4_, int p_149690_5_, float p_149690_6_, int p_149690_7_) {
        super.dropBlockAsItemWithChance(random, p_149690_2_, p_149690_3_, p_149690_4_, p_149690_5_, 1.0f, p_149690_7_);
    }

    protected void func_150124_c(World p_150124_1_, int p_150124_2_, int p_150124_3_, int p_150124_4_, int p_150124_5_, int p_150124_6_) {
    }

    protected int func_150123_b(int p_150123_1_) {
        return 20;
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World world, EntityPlayer player, int p_149636_3_, int p_149636_4_, int p_149636_5_, int p_149636_6_) {
        {
            super.harvestBlock(world, player, p_149636_3_, p_149636_4_, p_149636_5_, p_149636_6_);
        }
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int p_149692_1_) {
        return p_149692_1_ & 3;
    }

    // opaque weirdness
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube() {
        return false;

    }

    // opaque weirdness
    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockaccess, int posX, int posY, int posZ, int side){
        return true;
    }

    /**
     * Gets the block's texture. Args: side, meta
     */

    @Override
    public String getUnlocalizedName() {
        return String.format("tile.%s", getUnWrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(String.format("%s",  Reference.MODID.toLowerCase() + ":" + getUnWrappedUnlocalizedName(this.getUnlocalizedName())));
    }

    protected String getUnWrappedUnlocalizedName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }
    /**
     * Pass true to draw this block using fancy graphics, or false for fast graphics.
     */
    @SideOnly(Side.CLIENT)
    public void setGraphicsLevel(boolean p_150122_1_) {
        this.field_150121_P = p_150122_1_;
        this.field_150127_b = p_150122_1_ ? 0 : 1;
    }

    /**
     * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage
     * and is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
     */
    protected ItemStack createStackedBlock(int p_149644_1_) {
        return new ItemStack(Item.getItemFromBlock(this), 1, p_149644_1_ & 3);
    }

    @Override
    public boolean isShearable(ItemStack item, IBlockAccess world, int posX, int posY, int posZ) {
        return true;
    }

    @Override
    public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int posX, int posY, int posZ, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(this, 1, world.getBlockMetadata(posX, posY, posZ) & 3));
        return ret;
    }

    @Override
    public void beginLeavesDecay(World world, int posX, int posY, int posZ) {

        int i2 = world.getBlockMetadata(posX, posY, posZ);

        if ((i2 & 8) == 0) {
            world.setBlockMetadataWithNotify(posX, posY, posZ, i2 | 8, 4);
        }
        world.setBlockMetadataWithNotify(posX, posY, posZ, world.getBlockMetadata(posX, posY, posZ) | 8, 4);
    }

    @Override
    public boolean isLeaves(IBlockAccess world, int posX, int posY, int posZ) {
        return true;
    }


    @Override
    public ArrayList<ItemStack> getDrops(World world, int posX, int posY, int posZ, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        if (world.rand.nextFloat() < 0.025F)
            ret.add(new ItemStack(Items.diamond));
       // return ret;

        int chance = this.func_150123_b(metadata);

        if (fortune > 0) {
            chance -= 2 << fortune;
            if (chance < 10) chance = 10;
        }

        if (world.rand.nextInt(225) == 0)
            ret.add(new ItemStack(this.getItemDropped(metadata, world.rand, 225), 1, this.damageDropped(metadata)));

        chance = 200;
        if (fortune > 0) {
            chance -= 10 << fortune;
            if (chance < 40) chance = 40;
        }

        this.captureDrops(true);
        this.func_150124_c(world, posX, posY, posZ, metadata, chance); // Dammet mojang
        ret.addAll(this.captureDrops(false));
        return ret;
    }
}