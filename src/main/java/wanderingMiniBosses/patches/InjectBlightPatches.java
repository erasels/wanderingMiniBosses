package wanderingMiniBosses.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.helpers.BlightHelper;

@SpirePatch(clz = BlightHelper.class, method = "getBlight")
public class InjectBlightPatches {
    @SpirePrefixPatch
    public static SpireReturn<AbstractBlight> returnBlight(String ID) {
        if (ID.equals()) {
            return SpireReturn.Return(new );
        }

        return SpireReturn.Continue();
    }
}