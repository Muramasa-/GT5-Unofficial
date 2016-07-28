package gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collection;

public class GT_MetaTileEntity_HighPressureFurnace extends GT_MetaTileEntity_MultiBlockBase {

    public GT_Recipe mLastRecipe;
    public GT_MetaTileEntity_PlasmaHatch_Output mOutputPlasmaHatch;
    public GT_MetaTileEntity_Hatch_Input mInputMaterialHatch;
    public GT_MetaTileEntity_Hatch_Input mInputGas;
    public GT_MetaTileEntity_Hatch_Output mOutputGas;

    public GT_MetaTileEntity_HighPressureFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }
    public GT_MetaTileEntity_HighPressureFurnace(String aName) {
        super(aName);
    }

    public String[] getDescription() {
        return new String[]{
                "Controller Block for the High Pressure Furnace",
                "Size(WxHxD): 3x4x3, Controller (centered, bottom layer)",
                "2x Ceramic Pipe Casing inside the Casing",
                "1x Input Hatch (one of the coils)",
                "1x Output Hatch (one of the coils)",
                "1x Input Hatch (centered, bottom layer)",
                "1x Plasma Output Hatch (centered, top layer)",
                "1x Maintenance Hatch (one of the Casings)",
                "1x Dynamo Hatch (back centered)",
                "Robust Tungstensteel Casings for the rest (13 at least!)"};
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[60], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_HP_FURNACE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_HP_FURNACE)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[60]};
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return getMaxEfficiency(aStack) > 0;
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "HighPressureFurnace.png");
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        Collection<GT_Recipe> tRecipeList = GT_Recipe.GT_Recipe_Map.sHPFurnaceRecipes.mRecipeList;
        if(mInputMaterialHatch.mFluid != null && mInputGas.mFluid != null && tRecipeList != null) {
            FluidStack[] tFluids = new FluidStack[]{mInputMaterialHatch.mFluid, mInputGas.mFluid};
            GT_Recipe aRecipe = GT_Recipe.GT_Recipe_Map.sHPFurnaceRecipes.findRecipe(this.getBaseMetaTileEntity(), mLastRecipe, false, GT_Values.V[8], tFluids);
            if(aRecipe == null) {
                mLastRecipe = null;
                turnCasingActive(false);
                return false;
            } else {
                mLastRecipe = aRecipe;
                turnCasingActive(true);
                mInputMaterialHatch.mFluid.amount--; //TODO Balance
                mInputGas.mFluid.amount--; //TODO Balance
                mOutputPlasmaHatch.fill(aRecipe.getFluidOutput(1), true);
                mOutputGas.fill(aRecipe.getFluidOutput(0), true);
                this.mEUt = 0; //TODO Balance
                this.mProgresstime = 1;
                this.mMaxProgresstime = 1;
                this.mEfficiencyIncrease = 10000;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;

        int tCasingAmount = 0;
        int tCoilAmount = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if ((i != 0) || (j != 0)) { //Everything but the middle column
                    for (int k = 0; k <= 3; k++) {
                        IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, k, zDir + j);
                        if (k == 3) { //Top layer
                            if (aBaseMetaTileEntity.getBlockOffset(xDir + i, k, zDir + j) != getCasingBlock())
                                return false;
                            if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, k, zDir + j) != getCasingMeta())
                                return false;
                            tCasingAmount++;
                        } else if (k == 1 || k == 2) { //Middle two layers
                            if (!addGasInput(tTileEntity, getCoilTextureIndex()) && !addGasOutput(tTileEntity, getCoilTextureIndex())) {
                                if (aBaseMetaTileEntity.getBlockOffset(xDir + i, k, zDir + j) != getCoilBlock())
                                    return false;
                                if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, k, zDir + j) != getCoilMeta())
                                    return false;
                                tCoilAmount++;
                            }
                        } else if ((k == 0) && ((xDir + i != 0) || (zDir + j != 0))) { //Bottom layer & skip controller pos
                            if (!(addEnergyInputToMachineList(tTileEntity, getCasingTextureIndex())) && (!addMaintenanceToMachineList(tTileEntity, getCasingTextureIndex())) && (!addMufflerToMachineList(tTileEntity, getCasingTextureIndex()))) {
                                if (aBaseMetaTileEntity.getBlockOffset(xDir + i, k, zDir + j) != getCasingBlock())
                                    return false;
                                if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, k, zDir + j) != getCasingMeta())
                                    return false;
                                tCasingAmount++;
                            }
                        }
                    }
                } else { //Now check the middle column
                    for (int k = 1; k <= 2; k++) {
                        if (aBaseMetaTileEntity.getBlockOffset(xDir + i, k, zDir + j) != getPipeBlock())
                            return false;
                        if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, k, zDir + j) != getPipeMeta())
                            return false;
                    }
                    if(!(addPlasmaOutput(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 3, zDir + j), getPlasmaPipeCasingTextureIndex())))
                        return false;
                    if(!(addHydrogenInput(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 0, zDir + j), getPipeCasingTextureIndex())))
                        return false;
                }
            }
        }
        return (tCasingAmount >= 12) && (tCoilAmount >= 14);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if(aBaseMetaTileEntity.isServerSide()) turnCasingActive(aBaseMetaTileEntity.isActive());
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    public boolean addPlasmaOutput(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_PlasmaHatch_Output) {
            ((GT_MetaTileEntity_PlasmaHatch_Output) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            mOutputPlasmaHatch = (GT_MetaTileEntity_PlasmaHatch_Output) aMetaTileEntity;
            return true;
        }
        return false;
    }

    public boolean addHydrogenInput(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            mInputMaterialHatch = (GT_MetaTileEntity_Hatch_Input) aMetaTileEntity;
            return true;
        }
        return false;
    }

    public boolean addGasInput(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            mInputGas = (GT_MetaTileEntity_Hatch_Input) aMetaTileEntity;
            return true;
        }
        return false;
    }

    public boolean addGasOutput(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
            ((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            mOutputGas = (GT_MetaTileEntity_Hatch_Output) aMetaTileEntity;
            return true;
        }
        return false;
    }

    public void turnCasingActive(boolean status) {
        if(mOutputPlasmaHatch != null) mOutputPlasmaHatch.mMachineBlock  = status ? (byte) 52 : (byte) 53;
    }

    public Block getCasingBlock() {
        return GregTech_API.sBlockCasings4;
    }

    public byte getCasingMeta() {
        return 0;
    }

    public Block getPipeBlock() {
        return GregTech_API.sBlockCasings6;
    }

    public byte getPipeMeta() {
        return 0;
    }

    public Block getCoilBlock() {
        return GregTech_API.sBlockCasings6;
    }

    public byte getCoilMeta() {
        return 1;
    }

    public byte getCasingTextureIndex() {
        return 60;
    }

    public byte getPipeCasingTextureIndex() {
        return 48;
    }

    public byte getPlasmaPipeCasingTextureIndex() {
        return 53;
    }

    public byte getCoilTextureIndex() {
        return 81;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_HighPressureFurnace(this.mName);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 1;
    }

    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
    }

    @Override
    public int getAmountOfOutputs() {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public String[] getInfoData() {
        return new String[]{
                "High Pressure Furnace",
                "Current Efficiency: "+(mEfficiency/100)+"%"};
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }
}