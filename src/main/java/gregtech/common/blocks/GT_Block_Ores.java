package gregtech.common.blocks;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class GT_Block_Ores extends GT_Block_Ores_Abstract {
    public GT_Block_Ores() {
        super("gt.blockores", 7, false, Material.rock);
        tBlockReplacementList.put(Blocks.stone, this);
        tBlockReplacementList.put(Blocks.netherrack, this);
        tBlockReplacementList.put(Blocks.end_stone, this);
        tBlockReplacementList.put(GregTech_API.sBlockGranites, this);
        tBlockReplacementList.put(GregTech_API.sBlockStones, this);
    }

    @Override
    public String getUnlocalizedName() {
        return "gt.blockores";
    }

    @Override
    public OrePrefixes[] getProcessingPrefix() { //Must have 8 entries; an entry can be null to disable automatic recipes.
        return new OrePrefixes[]{OrePrefixes.ore, OrePrefixes.oreNetherrack, OrePrefixes.oreEndstone, OrePrefixes.oreBlackgranite, OrePrefixes.oreRedgranite, OrePrefixes.oreMarble, OrePrefixes.oreBasalt, null};
    }

    @Override
    public Block getDroppedBlock() {
        return GregTech_API.sBlockOres1;
    }

    @Override
    public Materials[] getDroppedDusts() { //Must have 8 entries; can be null.
        return new Materials[]{Materials.Stone, Materials.Netherrack, Materials.Endstone, Materials.GraniteBlack, Materials.GraniteRed, Materials.Marble, Materials.Basalt, Materials.Stone};
    }

    @Override
    public ITexture[] getTextureSet() { //Must have 16 entries.
        return new ITexture[]{new GT_CopiedBlockTexture(Blocks.stone, 0, 0), new GT_CopiedBlockTexture(Blocks.netherrack, 0, 0), new GT_CopiedBlockTexture(Blocks.end_stone, 0, 0), new GT_RenderedTexture(Textures.BlockIcons.GRANITE_BLACK_STONE), new GT_RenderedTexture(Textures.BlockIcons.GRANITE_RED_STONE), new GT_RenderedTexture(Textures.BlockIcons.MARBLE_STONE), new GT_RenderedTexture(Textures.BlockIcons.BASALT_STONE), new GT_CopiedBlockTexture(Blocks.stone, 0, 0), new GT_CopiedBlockTexture(Blocks.stone, 0, 0), new GT_CopiedBlockTexture(Blocks.stone, 0, 0), new GT_CopiedBlockTexture(Blocks.stone, 0, 0), new GT_CopiedBlockTexture(Blocks.stone, 0, 0), new GT_CopiedBlockTexture(Blocks.stone, 0, 0), new GT_CopiedBlockTexture(Blocks.stone, 0, 0), new GT_CopiedBlockTexture(Blocks.stone, 0, 0), new GT_CopiedBlockTexture(Blocks.stone, 0, 0)};
    }

    @Override
    public boolean isValidBlock(Block aBlockKey, int aMetaData, boolean isSmallOre, World aWorld, int aX, int aY, int aZ) {
        if (aBlockKey == Blocks.netherrack) {
            tMetaData = aMetaData + 1000; return true;
        } else if (aBlockKey == Blocks.end_stone) {
            tMetaData = aMetaData + 2000; return true;
        } else if (aBlockKey == GregTech_API.sBlockGranites) {
            if (aWorld.getBlockMetadata(aX, aY, aZ) < 8)
                tMetaData = aMetaData + 3000;
            else
                tMetaData = aMetaData + 4000;
            return true;
        } else if (aBlockKey == GregTech_API.sBlockStones) {
            if (aWorld.getBlockMetadata(aX, aY, aZ) < 8)
                tMetaData = aMetaData + 5000;
            else
                tMetaData = aMetaData + 6000;
            return true;
        }
        return false;
    }
}