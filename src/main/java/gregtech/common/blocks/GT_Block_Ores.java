package gregtech.common.blocks;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.objects.GT_OreGenHandler;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class GT_Block_Ores extends GT_Block_Ores_Abstract {
    public GT_Block_Ores() {
        super("gt.blockores", 7, false, Material.rock);
        GT_OreGenHandler.tBlockList.add(new GT_OreGenHandler(Blocks.stone, 0, this, (short) 0));
        GT_OreGenHandler.tBlockList.add(new GT_OreGenHandler(Blocks.netherrack, 0, this, (short) 1000));
        GT_OreGenHandler.tBlockList.add(new GT_OreGenHandler(Blocks.end_stone, 0, this, (short) 2000));
        GT_OreGenHandler.tBlockList.add(new GT_OreGenHandler(GregTech_API.sBlockGranites, 0, this, (short) 3000));
        GT_OreGenHandler.tBlockList.add(new GT_OreGenHandler(GregTech_API.sBlockGranites, 8, this, (short) 4000));
        GT_OreGenHandler.tBlockList.add(new GT_OreGenHandler(GregTech_API.sBlockStones, 0, this, (short) 5000));
        GT_OreGenHandler.tBlockList.add(new GT_OreGenHandler(GregTech_API.sBlockStones, 8, this, (short) 6000));
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
}