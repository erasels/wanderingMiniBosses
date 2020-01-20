package wanderingMiniBosses.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.helpers.BlightHelper;
import wanderingMiniBosses.blights.FullOfOpenings;
import wanderingMiniBosses.blights.NotAskedDonationsToTheBloodFund;
import wanderingMiniBosses.blights.TooCautious;

@SpirePatch(clz = BlightHelper.class, method = "getBlight")
public class InjectBlightPatches {
    @SpirePrefixPatch
    public static SpireReturn<AbstractBlight> returnBlight(String ID) {
        if (ID.equals(FullOfOpenings.ID)) {
            return SpireReturn.Return(new FullOfOpenings());
        } else if (ID.equals(TooCautious.ID)) {
            return SpireReturn.Return(new TooCautious());
        } else if (ID.equals(NotAskedDonationsToTheBloodFund.ID)) {
            return SpireReturn.Return(new NotAskedDonationsToTheBloodFund());
        }

        return SpireReturn.Continue();
    }
}