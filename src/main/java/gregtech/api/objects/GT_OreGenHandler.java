package gregtech.api.objects;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.ArrayList;

public class GT_OreGenHandler {
    public static ArrayList<GT_OreGenHandler> tBlockList = new ArrayList<GT_OreGenHandler>();
    public Block tTargetBlock;
    public int tTargetMeta;
    public Block tDroppedBlock;
    public short tMetadataOffset;

    public GT_OreGenHandler(Block aTargetBlock, int aTargetMeta, Block aDroppedBlock, short aMetaDataOffset) {
        tTargetBlock = aTargetBlock;
        tTargetMeta = aTargetMeta;
        tDroppedBlock = aDroppedBlock;
        tMetadataOffset = aMetaDataOffset;
        tBlockList.add(this);
    }

    public static GT_OreGenHandler getHandler(World aWorld, int aX, int aY, int aZ){
        Block aTargetBlock = aWorld.getBlock(aX, aY, aZ);
        int aTargetMeta = aWorld.getBlockMetadata(aX, aY, aZ);
        for (GT_OreGenHandler aOreGenHandler : tBlockList) {
            if ((aOreGenHandler.tTargetBlock == aTargetBlock) && (aOreGenHandler.tTargetMeta == aTargetMeta)) {
                return aOreGenHandler;
            }
        }
        return null;
    }
}
