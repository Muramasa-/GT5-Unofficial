package gregtech.common.blocks;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class GT_Block_Ores extends GT_Block_Ores_Abstract {
    public GT_Block_Ores() {
        super("gt.blockores", 7, false, Material.rock);
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
    public boolean shouldRegisterOre(short aMeta) {
        switch (aMeta) {
            case 0:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                return GT_Worldgen_GT_Ore_Layer.sOverworldOres.contains(aMeta);
            case 1:
                return GT_Worldgen_GT_Ore_Layer.sNetherOres.contains(aMeta);
            case 2:
                return GT_Worldgen_GT_Ore_Layer.sEndOres.contains(aMeta);
            default: return false;
        }
    }

    @Override
    public int getBaseBlockHarvestLevel(int aMeta) {
        switch (aMeta) {
            case 3:
            case 4:return 3;
            case 0:
            case 1:
            case 2:
            case 5:
            case 6:
            default:return 0;
        }
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
}