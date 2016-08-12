package gregtech.common;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Log;
import gregtech.api.world.GT_Worldgen;
import gregtech.common.blocks.GT_TileEntity_Ores;
import gregtech.loaders.misc.GT_Achievements;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Random;

public class GT_Worldgen_GT_Ore_Layer
        extends GT_Worldgen {
    public static ArrayList<GT_Worldgen_GT_Ore_Layer> sList = new ArrayList();
    public static int sWeight = 0;
    public final short mMinY;
    public final short mMaxY;
    public final short mWeight;
    public final short mDensity;
    public final short mSize;
    public short mPrimaryMeta = -1;
    public short mSecondaryMeta = -1;
    public short mBetweenMeta = -1;
    public short mSporadicMeta = -1;
    public final String mRestrictBiome;
    public final boolean mOverworld;
    public final boolean mNether;
    public final boolean mEnd;
    public final boolean mEndAsteroid;
    public final boolean mMoon;
    public final boolean mMars;
    public final boolean mAsteroid;

    public String mPrimaryString;
    public String mSecondaryString;
    public String mBetweenString;
    public String mSporadicString;

    public Block mPrimaryBlock;
    public Block mSecondaryBlock;
    public Block mBetweenBlock;
    public Block mSporadicBlock;

    public int mPrimaryDamage; //Make final?
    public int mSecondaryDamage;
    public int mBetweenDamage;
    public int mSporadicDamage;

    public boolean mIsPrimaryGT = true;
    public boolean mIsSecondaryGT = true;
    public boolean mIsBetweenGT = true;
    public boolean misSporadicGT = true;

    public GT_Worldgen_GT_Ore_Layer(String aName, boolean aDefault, int aMinY, int aMaxY, int aWeight, int aDensity, int aSize, boolean aOverworld, boolean aNether, boolean aEnd, boolean aMoon, boolean aMars, boolean aAsteroid, Materials aPrimary, Materials aSecondary, Materials aBetween, Materials aSporadic) {
        super(aName, sList, aDefault);
        this.mOverworld = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Overworld", aOverworld);
        this.mNether = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Nether", aNether);
        this.mEnd = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "TheEnd", aEnd);
        this.mEndAsteroid = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "EndAsteroid", aEnd);
        this.mMoon = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Moon", aMoon);
        this.mMars = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Mars", aMars);
        this.mAsteroid = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Asteroid", aAsteroid);
        this.mMinY = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "MinHeight", aMinY));
        this.mMaxY = ((short) Math.max(this.mMinY + 5, GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "MaxHeight", aMaxY)));
        this.mWeight = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "RandomWeight", aWeight));
        this.mDensity = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Density", aDensity));
        this.mSize = ((short) Math.max(1, GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Size", aSize)));
        this.mRestrictBiome = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "RestrictToBiomeName", "None");

        this.mPrimaryString = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "OrePrimaryLayer", String.valueOf(aPrimary.mMetaItemSubID));
        this.mSecondaryString = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "OreSecondaryLayer",String.valueOf(aSecondary.mMetaItemSubID));
        this.mBetweenString = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "OreSporadiclyInbetween", String.valueOf(aBetween.mMetaItemSubID));
        this.mSporadicString = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "OreSporaticlyAround", String.valueOf(aSporadic.mMetaItemSubID));

        if (!NumberUtils.isNumber(this.mPrimaryString)) { //Could be a block id/meta string
            Block aBlock = isForeignBlockValid(this.mPrimaryString.split(":")); //Find block with supplied data
            if (aBlock != null) { //Was it found?
                this.mPrimaryBlock = aBlock;
                this.mPrimaryDamage = Integer.parseInt(this.mPrimaryString.split(":")[2]);
                this.mIsPrimaryGT = false; //No longer spawning gt ore for this layer
            } else  { //Block not found, use normal ore metadata
                this.mPrimaryMeta = (short) aPrimary.mMetaItemSubID;
                GT_Log.err.println("Block ID/Meta for the primary layer in the " + this.mWorldGenName + " ore mix was invalid or the block doesn't exist");
            }
        } else this.mPrimaryMeta = Short.parseShort(this.mPrimaryString); //Is a number, must be standard ore metadata

        if (!NumberUtils.isNumber(this.mSecondaryString)) {
            Block aBlock = isForeignBlockValid(this.mSecondaryString.split(":"));
            if (aBlock != null) {
                this.mSecondaryBlock = aBlock;
                this.mSecondaryDamage = Integer.parseInt(this.mSecondaryString.split(":")[2]);
                this.mIsSecondaryGT = false;
            } else {
                this.mSecondaryMeta = (short) aSecondary.mMetaItemSubID;
                GT_Log.err.println("Block ID/Meta for the secondary layer in the " + this.mWorldGenName + " ore mix was invalid or the block doesn't exist");
            }
        } else this.mSecondaryMeta = Short.parseShort(this.mSecondaryString);

        if (!NumberUtils.isNumber(this.mBetweenString)) {
            Block aBlock = isForeignBlockValid(this.mBetweenString.split(":"));
            if (aBlock != null) {
                this.mBetweenBlock = aBlock;
                this.mBetweenDamage = Integer.parseInt(this.mBetweenString.split(":")[2]);
                this.mIsBetweenGT = false;
            } else {
                this.mBetweenMeta = (short) aBetween.mMetaItemSubID;
                GT_Log.err.println("Block ID/Meta for the between layer in the " + this.mWorldGenName + " ore mix was invalid or the block doesn't exist");
            }
        } else this.mBetweenMeta = Short.parseShort(this.mBetweenString);

        if (!NumberUtils.isNumber(this.mSporadicString)) {
            Block aBlock = isForeignBlockValid(this.mSporadicString.split(":"));
            if (aBlock != null) {
                this.mSporadicBlock = aBlock;
                this.mSporadicDamage = Integer.parseInt(this.mSporadicString.split(":")[2]);
                this.misSporadicGT = false;
            } else {
                this.mSporadicMeta = (short) aSporadic.mMetaItemSubID;
                GT_Log.err.println("Block ID/Meta for the sporadic layer in the " + this.mWorldGenName + " ore mix was invalid or the block doesn't exist");
            }
        } else this.mSporadicMeta = Short.parseShort(this.mSporadicString);

        if (this.mEnabled) {
            if (this.mIsPrimaryGT) GT_Achievements.registerOre(GregTech_API.sGeneratedMaterials[(mPrimaryMeta % 1000)], aMinY, aMaxY, aWeight, aOverworld, aNether, aEnd);
            if (this.mIsSecondaryGT) GT_Achievements.registerOre(GregTech_API.sGeneratedMaterials[(mSecondaryMeta % 1000)], aMinY, aMaxY, aWeight, aOverworld, aNether, aEnd);
            if (this.mIsBetweenGT) GT_Achievements.registerOre(GregTech_API.sGeneratedMaterials[(mBetweenMeta % 1000)], aMinY, aMaxY, aWeight, aOverworld, aNether, aEnd);
            if (this.misSporadicGT) GT_Achievements.registerOre(GregTech_API.sGeneratedMaterials[(mSporadicMeta % 1000)], aMinY, aMaxY, aWeight, aOverworld, aNether, aEnd);
            sWeight += this.mWeight;
        }
    }

    public boolean executeWorldgen(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX, int aChunkZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
        if (!this.mRestrictBiome.equals("None") && !(this.mRestrictBiome.equals(aBiome))) {
            return false; //Not the correct biome for ore mix
        }
        if (!isGenerationAllowed(aWorld, aDimensionType, ((aDimensionType == -1) && (this.mNether)) || ((aDimensionType == 0) && (this.mOverworld)) || ((aDimensionType == 1) && (this.mEnd)) || ((aWorld.provider.getDimensionName().equals("Moon")) && (this.mMoon)) || ((aWorld.provider.getDimensionName().equals("Mars")) && (this.mMars)) ? aDimensionType : aDimensionType ^ 0xFFFFFFFF)) {
            return false;
        }
        int tMinY = this.mMinY + aRandom.nextInt(this.mMaxY - this.mMinY - 5);

        int cX = aChunkX - aRandom.nextInt(this.mSize);
        int eX = aChunkX + 16 + aRandom.nextInt(this.mSize);
        for (int tX = cX; tX <= eX; tX++) {
            int cZ = aChunkZ - aRandom.nextInt(this.mSize);
            int eZ = aChunkZ + 16 + aRandom.nextInt(this.mSize);
            for (int tZ = cZ; tZ <= eZ; tZ++) {
                //Secondary
                for (int i = tMinY - 1; i < tMinY + 2; i++) {
                    if ((aRandom.nextInt(Math.max(1, Math.max(Math.abs(cZ - tZ), Math.abs(eZ - tZ)) / this.mDensity)) == 0) || (aRandom.nextInt(Math.max(1, Math.max(Math.abs(cX - tX), Math.abs(eX - tX)) / this.mDensity)) == 0)) {
                        if (this.mIsSecondaryGT) { //Are we using a GT meta ore?
                            if (this.mSecondaryMeta > 0) GT_TileEntity_Ores.setOreBlock(aWorld, tX, i, tZ, this.mSecondaryMeta, false);
                        } else { //Must of found a matching block
                            if (this.mSecondaryBlock != null) GT_TileEntity_Ores.setOreBlock(aWorld, tX, i, tZ, this.mSecondaryBlock, this.mSecondaryDamage, false);
                        }
                    }
                }
                //Between
                if (((aRandom.nextInt(Math.max(1, Math.max(Math.abs(cZ - tZ), Math.abs(eZ - tZ)) / this.mDensity)) == 0) || (aRandom.nextInt(Math.max(1, Math.max(Math.abs(cX - tX), Math.abs(eX - tX)) / this.mDensity)) == 0))) {
                    if (this.mIsBetweenGT) {
                        if (this.mBetweenMeta > 0) GT_TileEntity_Ores.setOreBlock(aWorld, tX, tMinY + 2 + aRandom.nextInt(2), tZ, this.mBetweenMeta, false);
                    } else {
                        if (this.mBetweenBlock != null) GT_TileEntity_Ores.setOreBlock(aWorld, tX, tMinY + 2 + aRandom.nextInt(2), tZ, this.mBetweenBlock, this.mBetweenDamage, false);
                    }
                }

                //Primary
                for (int i = tMinY + 3; i < tMinY + 6; i++) {
                    if ((aRandom.nextInt(Math.max(1, Math.max(Math.abs(cZ - tZ), Math.abs(eZ - tZ)) / this.mDensity)) == 0) || (aRandom.nextInt(Math.max(1, Math.max(Math.abs(cX - tX), Math.abs(eX - tX)) / this.mDensity)) == 0)) {
                        if (this.mIsPrimaryGT) {
                            if (this.mPrimaryMeta > 0) GT_TileEntity_Ores.setOreBlock(aWorld, tX, i, tZ, this.mPrimaryMeta, false);
                        } else {
                            if (this.mPrimaryBlock != null) GT_TileEntity_Ores.setOreBlock(aWorld, tX, i, tZ, this.mPrimaryBlock, this.mPrimaryDamage, false);
                        }
                    }
                }
                //Sporadic
                if (((aRandom.nextInt(Math.max(1, Math.max(Math.abs(cZ - tZ), Math.abs(eZ - tZ)) / this.mDensity)) == 0) || (aRandom.nextInt(Math.max(1, Math.max(Math.abs(cX - tX), Math.abs(eX - tX)) / this.mDensity)) == 0))) {
                    if (this.misSporadicGT) {
                        if (this.mSporadicMeta > 0) GT_TileEntity_Ores.setOreBlock(aWorld, tX, tMinY - 1 + aRandom.nextInt(7), tZ, this.mSporadicMeta, false);
                    } else {
                        if (this.mSporadicBlock != null) GT_TileEntity_Ores.setOreBlock(aWorld, tX, tMinY - 1 + aRandom.nextInt(7), tZ, this.mSporadicBlock, this.mSporadicDamage, false);
                    }
                }
            }
        }
        if (GT_Values.D1) {
            System.out.println("Generated Orevein: " + this.mWorldGenName);
        }
        return true;
    }

    public static Block isForeignBlockValid(String[] aMetaParts) {
        if (aMetaParts.length == 3 && NumberUtils.isNumber(aMetaParts[2])) {
            return GameRegistry.findBlock(aMetaParts[0], aMetaParts[1]);
        }
        return null;
    }
}
