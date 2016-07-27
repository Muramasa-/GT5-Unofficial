package gregtech.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class GT_Block_Casings6 extends GT_Block_Casings_Abstract {
    public GT_Block_Casings6() {
        super(GT_Item_Casings6.class, "gt.blockcasings6", GT_Material_Casings.INSTANCE);
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            Textures.BlockIcons.CASING_BLOCKS[(i + 80)] = new GT_CopiedBlockTexture(this, 6, i);
        }
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Ceramic Pipe Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Coiled Copper Tubing Block");

        ItemList.Casing_Pipe_Ceramic.set(new ItemStack(this, 1, 0));
        ItemList.Casing_Coiled_Tubing_Copper.set(new ItemStack(this, 1, 1));
    }
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int aSide, int aMeta) {
        switch (aMeta) {
            case 0:
                return Textures.BlockIcons.MACHINE_CASING_PIPE_CERAMIC.getIcon();
            case 1:
                return Textures.BlockIcons.MACHINE_COIL_CUPRONICKEL.getIcon();
        }
        return Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
    }
}
