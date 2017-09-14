package gregtech.common.blocks;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.MaterialFlags;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.items.GT_Generic_Block;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.render.GT_Renderer_Block;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class GT_Block_Ores_Abstract extends GT_Generic_Block implements ITileEntityProvider {
    public static ThreadLocal<GT_TileEntity_Ores> mTemporaryTileEntity = new ThreadLocal<>();
    public static boolean FUCKING_LOCK = false;
    public static boolean tHideOres;
    private final String aTextName = ".name";

    protected GT_Block_Ores_Abstract(String aUnlocalizedName, int aOreMetaCount, boolean aHideFirstMeta, Material aBlockMaterial) {
        super(GT_Item_Ores.class, aUnlocalizedName, aBlockMaterial);
        this.isBlockContainer = true;
        setStepSound(soundTypeStone);
        setCreativeTab(GregTech_API.TAB_GREGTECH_ORES);
        tHideOres = Loader.isModLoaded("NotEnoughItems") && GT_Mod.gregtechproxy.mHideUnusedOres;
        if(aOreMetaCount > 8 || aOreMetaCount < 0) aOreMetaCount = 8;

        for (int i = 1; i < Materials.MATERIALS_ORE.length; i++) {
            Materials aMaterial = Materials.MATERIALS_ORE[i];
            if (aMaterial != null) {
                for (int aCurrMeta = 0; aCurrMeta < aOreMetaCount; aCurrMeta++) {
                    if (!this.getEnabledMetas()[aCurrMeta]) continue;
                    GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." + (aMaterial.mMetaItemSubID + (aCurrMeta * 1000)) + aTextName, getLocalizedName(aMaterial));
                    if (aMaterial.hasFlag(MaterialFlags.SORE)) {
                        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." + ((aMaterial.mMetaItemSubID + 16000) + (aCurrMeta * 1000)) + aTextName, "Small " + getLocalizedName(aMaterial));
                    }
                    GT_OreDictUnificator.registerOre(this.getProcessingPrefix()[aCurrMeta] != null ? this.getProcessingPrefix()[aCurrMeta].get(aMaterial) : "", new ItemStack(this, 1, aMaterial.mMetaItemSubID + (aCurrMeta * 1000)));
                    if (tHideOres) {
                        if (!(aCurrMeta == 0 && !aHideFirstMeta)) {
                            codechicken.nei.api.API.hideItem(new ItemStack(this, 1, aMaterial.mMetaItemSubID + (aCurrMeta * 1000)));
                        }
                        if (aMaterial.hasFlag(MaterialFlags.SORE)) {
                            codechicken.nei.api.API.hideItem(new ItemStack(this, 1, (aMaterial.mMetaItemSubID + 16000) + (aCurrMeta * 1000)));
                        }
                    }
                }
            }
        }
    }

    public int getBaseBlockHarvestLevel(int aMeta) {
        return 0;
    }

    public void onNeighborChange(IBlockAccess aWorld, int aX, int aY, int aZ, int aTileX, int aTileY, int aTileZ) {
        if (!FUCKING_LOCK) {
            FUCKING_LOCK = true;
            TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
            if ((tTileEntity instanceof GT_TileEntity_Ores)) {
                ((GT_TileEntity_Ores) tTileEntity).onUpdated();
            }
        }
        FUCKING_LOCK = false;
    }

    public void onNeighborBlockChange(World aWorld, int aX, int aY, int aZ, Block aBlock) {
        if (!FUCKING_LOCK) {
            FUCKING_LOCK = true;
            TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
            if ((tTileEntity instanceof GT_TileEntity_Ores)) {
                ((GT_TileEntity_Ores) tTileEntity).onUpdated();
            }
        }
        FUCKING_LOCK = false;
    }

    public String getLocalizedName(Materials aMaterial) {
        switch (aMaterial.mName) {
            case "InfusedAir":
            case "InfusedEarth":
            case "InfusedEntropy":
            case "InfusedFire":
            case "InfusedOrder":
            case "InfusedWater":
                return aMaterial.mDefaultLocalName + " Infused Stone";
            case "Bentonite":
            case "Talc":
            case "BasalticMineralSand":
            case "GraniticMineralSand":
            case "GlauconiteSand":
            case "CassiteriteSand":
            case "Pitchblende":
                return aMaterial.mDefaultLocalName;
            default:
                return aMaterial.mDefaultLocalName + OrePrefixes.ore.mLocalizedMaterialPost;
        }
    }

    public boolean onBlockEventReceived(World world, int x, int y, int z, int p_149696_5_, int p_149696_6_) {
        super.onBlockEventReceived(world, x, y, z, p_149696_5_, p_149696_6_);
        TileEntity tileentity = world.getTileEntity(x, y, z);
        return tileentity != null && tileentity.receiveClientEvent(p_149696_5_, p_149696_6_);
    }

    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        return (!(entity instanceof EntityDragon)) && (super.canEntityDestroy(world, x, y, z, entity));
    }

    public String getHarvestTool(int aMeta) {
        return aMeta < 8 ? "pickaxe" : "shovel";
    }

    public int getHarvestLevel(int aMeta) {
        return aMeta == 5 || aMeta == 6 ? 2 : aMeta % 8;
    }

    public float getBlockHardness(World aWorld, int aX, int aY, int aZ) {
        return 1.0F + getHarvestLevel(aWorld.getBlockMetadata(aX, aY, aZ)) * 1.0F;
    }

    public float getExplosionResistance(Entity par1Entity, World aWorld, int aX, int aY, int aZ, double explosionX, double explosionY, double explosionZ) {
        return 1.0F + getHarvestLevel(aWorld.getBlockMetadata(aX, aY, aZ)) * 1.0F;
    }

    protected boolean canSilkHarvest() {
        return false;
    }

    public abstract String getUnlocalizedName();

    public String getLocalizedName() {
        return StatCollector.translateToLocal(getUnlocalizedName() + aTextName);
    }

    public int getRenderType() {
        if (GT_Renderer_Block.INSTANCE == null) {
            return super.getRenderType();
        }
        return GT_Renderer_Block.INSTANCE.mRenderID;
    }

    public boolean canBeReplacedByLeaves(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return false;
    }

    public boolean isNormalCube(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return true;
    }

    public boolean hasTileEntity(int aMeta) {
        return true;
    }

    public boolean renderAsNormalBlock() {
        return true;
    }

    public boolean isOpaqueCube() {
        return true;
    }

    public TileEntity createNewTileEntity(World aWorld, int aMeta) {
        return createTileEntity(aWorld, aMeta);
    }

    public IIcon getIcon(IBlockAccess aIBlockAccess, int aX, int aY, int aZ, int aSide) {
        return Blocks.stone.getIcon(0, 0);
    }

    public IIcon getIcon(int aSide, int aMeta) {
        return Blocks.stone.getIcon(0, 0);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister aIconRegister) {
    }

    public int getDamageValue(World aWorld, int aX, int aY, int aZ) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (((tTileEntity instanceof GT_TileEntity_Ores))) {
            return ((GT_TileEntity_Ores) tTileEntity).getMetaData();
        }
        return 0;
    }

    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block par5, int par6) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if ((tTileEntity instanceof GT_TileEntity_Ores)) {
            mTemporaryTileEntity.set((GT_TileEntity_Ores) tTileEntity);
        }
        super.breakBlock(aWorld, aX, aY, aZ, par5, par6);
        aWorld.removeTileEntity(aX, aY, aZ);
    }

    public abstract OrePrefixes[] getProcessingPrefix(); //Must have 8 entries; an entry can be null to disable automatic recipes.

    public abstract boolean[] getEnabledMetas(); //Must have 8 entries.

    public abstract Block getDroppedBlock();

    public abstract Materials[] getDroppedDusts(); //Must have 8 entries; can be null.

    public ArrayList<ItemStack> getDrops(World aWorld, int aX, int aY, int aZ, int aMeta, int aFortune) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if ((tTileEntity instanceof GT_TileEntity_Ores)) {
            return ((GT_TileEntity_Ores) tTileEntity).getDrops(getDroppedBlock(), aFortune);
        }
        return mTemporaryTileEntity.get() == null ? new ArrayList<ItemStack>() : mTemporaryTileEntity.get().getDrops(getDroppedBlock(), aFortune);
    }

    public TileEntity createTileEntity(World aWorld, int aMeta) {
        return new GT_TileEntity_Ores();
    }

    public abstract ITexture[] getTextureSet(); //Must have 16 entries.

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs aTab, List aList) {
        for (Materials tMaterial : Materials.MATERIALS_ORE) {
            if (tMaterial != null) {
                if (!(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID).getDisplayName().contains(aTextName))) aList.add(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID));
                if (!(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 1000).getDisplayName().contains(aTextName))) aList.add(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 1000));
                if (!(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 2000).getDisplayName().contains(aTextName))) aList.add(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 2000));
                if (!(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 3000).getDisplayName().contains(aTextName))) aList.add(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 3000));
                if (!(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 4000).getDisplayName().contains(aTextName))) aList.add(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 4000));
                if (!(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 5000).getDisplayName().contains(aTextName))) aList.add(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 5000));
                if (!(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 6000).getDisplayName().contains(aTextName))) aList.add(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 6000));
                if (!(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 7000).getDisplayName().contains(aTextName))) aList.add(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 7000));
                if (tMaterial.hasFlag(MaterialFlags.SORE)) {
                    if (!(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 16000).getDisplayName().contains(aTextName))) aList.add(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 16000));
                    if (!(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 17000).getDisplayName().contains(aTextName))) aList.add(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 17000));
                    if (!(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 18000).getDisplayName().contains(aTextName))) aList.add(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 18000));
                    if (!(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 19000).getDisplayName().contains(aTextName))) aList.add(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 19000));
                    if (!(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 20000).getDisplayName().contains(aTextName))) aList.add(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 20000));
                    if (!(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 21000).getDisplayName().contains(aTextName))) aList.add(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 21000));
                    if (!(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 22000).getDisplayName().contains(aTextName))) aList.add(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 22000));
                    if (!(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 23000).getDisplayName().contains(aTextName))) aList.add(new ItemStack(aItem, 1, tMaterial.mMetaItemSubID + 23000));
                }
            }
        }
    }
}