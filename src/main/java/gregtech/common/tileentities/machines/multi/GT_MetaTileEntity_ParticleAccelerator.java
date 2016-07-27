package gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GT_MetaTileEntity_ParticleAccelerator extends GT_MetaTileEntity_MultiBlockBase {
    public GT_MetaTileEntity_ParticleAccelerator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }
    public GT_MetaTileEntity_ParticleAccelerator(String aName) {
        super(aName);
    }

    public String[] getDescription() {
        return new String[]{
                "Controller Block for the Particle Accelerator"};
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[60], new GT_RenderedTexture(aActive ? Textures.BlockIcons.MACHINE_CASING_PA_ACTIVE : Textures.BlockIcons.MACHINE_CASING_PA)};
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
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return false;
    }

    public Block getCasingBlock() {
        return GregTech_API.sBlockCasings4;
    }

    public byte getCasingMeta() {
        return 0;
    }

    public byte getCasingTextureIndex() {
        return 60;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_ParticleAccelerator(this.mName);
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
