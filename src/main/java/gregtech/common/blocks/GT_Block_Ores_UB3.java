package gregtech.common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GT_CopiedBlockTexture;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class GT_Block_Ores_UB3 extends GT_Block_Ores_Abstract {
    Block aUBBlock = GameRegistry.findBlock("UndergroundBiomes", "sedimentaryStone");

    public GT_Block_Ores_UB3() {
        super("gt.blockores.ub3", 8, true, Material.rock);
        if (aUBBlock == null) aUBBlock = Blocks.stone;
        tBlockReplacementList.put(aUBBlock, this);
    }

    @Override
    public String getUnlocalizedName() {
        return "gt.blockores.ub3";
    }

    @Override
    public OrePrefixes[] getProcessingPrefix() { //Must have 8 entries; an entry can be null to disable automatic recipes.
        return new OrePrefixes[]{OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore};
    }

    @Override
    public Block getDroppedBlock() {
        return GregTech_API.sBlockOresUb3;
    }

    @Override
    public Materials[] getDroppedDusts() { //Must have 8 entries; can be null.
        return new Materials[]{Materials.Stone, Materials.Stone, Materials.Stone, Materials.Stone, Materials.Stone, Materials.Stone, Materials.Stone, Materials.Stone};
    }

    @Override
    public ITexture[] getTextureSet() { //Must have 16 entries.
        return new ITexture[]{new GT_CopiedBlockTexture(aUBBlock, 0, 0), new GT_CopiedBlockTexture(aUBBlock, 0, 1), new GT_CopiedBlockTexture(aUBBlock, 0, 2), new GT_CopiedBlockTexture(aUBBlock, 0, 3), new GT_CopiedBlockTexture(aUBBlock, 0, 4), new GT_CopiedBlockTexture(aUBBlock, 0, 5), new GT_CopiedBlockTexture(aUBBlock, 0, 6), new GT_CopiedBlockTexture(aUBBlock, 0, 7), new GT_CopiedBlockTexture(aUBBlock, 0, 0), new GT_CopiedBlockTexture(aUBBlock, 0, 1), new GT_CopiedBlockTexture(aUBBlock, 0, 2), new GT_CopiedBlockTexture(aUBBlock, 0, 3), new GT_CopiedBlockTexture(aUBBlock, 0, 4), new GT_CopiedBlockTexture(aUBBlock, 0, 5), new GT_CopiedBlockTexture(aUBBlock, 0, 6), new GT_CopiedBlockTexture(aUBBlock, 0, 7)};
    }

    @Override
    public boolean isValidBlock(Block aBlockKey, int aMetaData, boolean isSmallOre, World aWorld, int aX, int aY, int aZ) {
        int aBlockMeta = aWorld.getBlockMetadata(aX, aY, aZ);
        if (aBlockMeta > 0 && aBlockMeta <= 8) {
            tMetaData = aMetaData + (aBlockMeta * 1000); return true;
        }
        return false;
    }
}