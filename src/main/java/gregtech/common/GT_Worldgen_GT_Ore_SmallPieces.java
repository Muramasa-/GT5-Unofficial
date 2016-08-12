package gregtech.common;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.world.GT_Worldgen;
import gregtech.common.blocks.GT_TileEntity_Ores;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Random;

public class GT_Worldgen_GT_Ore_SmallPieces extends GT_Worldgen {
    public final short mMinY;
    public final short mMaxY;
    public final short mAmount;
    public short mMeta = -1;
    public final String mMetaString;
    public final boolean mOverworld;
    public final boolean mNether;
    public final boolean mEnd;
    public final boolean mMoon;
    public final boolean mMars;
    public final boolean mAsteroid;
    public final String mRestrictBiome;

    public Block mBlock;
    public int mDamage;
    public boolean mIsGt = true;

    public GT_Worldgen_GT_Ore_SmallPieces(String aName, boolean aDefault, int aMinY, int aMaxY, int aAmount, boolean aOverworld, boolean aNether, boolean aEnd, boolean aMoon, boolean aMars, boolean aAsteroid, Materials aPrimary) {
        super(aName, GregTech_API.sWorldgenList, aDefault);
        this.mOverworld = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Overworld", aOverworld);
        this.mNether = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Nether", aNether);
        this.mEnd = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "TheEnd", aEnd);
        this.mMoon = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Moon", aMoon);
        this.mMars = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Mars", aMars);
        this.mAsteroid = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Asteroid", aAsteroid);
        this.mMinY = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "MinHeight", aMinY));
        this.mMaxY = ((short) Math.max(this.mMinY + 1, GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "MaxHeight", aMaxY)));
        this.mAmount = ((short) Math.max(1, GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Amount", aAmount)));
        this.mRestrictBiome = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "RestrictToBiomeName", "None");
        this.mMetaString = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Ore", String.valueOf(aPrimary.mMetaItemSubID));

        if (!NumberUtils.isNumber(this.mMetaString)) {
            Block aBlock = GT_Worldgen_GT_Ore_Layer.isForeignBlockValid(this.mMetaString.split(":"));
            if (aBlock != null) {
                this.mBlock = aBlock;
                this.mDamage = Integer.parseInt(this.mMetaString.split(":")[2]);
                this.mIsGt = false;
            }
        } else this.mMeta = Short.parseShort(this.mMetaString);
    }

    public boolean executeWorldgen(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX, int aChunkZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
        if (!this.mRestrictBiome.equals("None") && !(this.mRestrictBiome.equals(aBiome))) {
            return false; //Not the correct biome for small ore
        }
        if (!isGenerationAllowed(aWorld, aDimensionType, ((aDimensionType == -1) && (this.mNether)) || ((aDimensionType == 0) && (this.mOverworld)) || ((aDimensionType == 1) && (this.mEnd)) || ((aWorld.provider.getDimensionName().equals("Moon")) && (this.mMoon)) || ((aWorld.provider.getDimensionName().equals("Mars")) && (this.mMars)) ? aDimensionType : aDimensionType ^ 0xFFFFFFFF)) {
            return false;
        }
        if (this.mMeta > 0) {
            int i = 0;
            for (int j = Math.max(1, this.mAmount / 2 + aRandom.nextInt(this.mAmount) / 2); i < j; i++) {
                GT_TileEntity_Ores.setOreBlock(aWorld, aChunkX + aRandom.nextInt(16), this.mMinY + aRandom.nextInt(Math.max(1, this.mMaxY - this.mMinY)), aChunkZ + aRandom.nextInt(16), this.mMeta, true);
            }
        }
        return true;
    }
}
