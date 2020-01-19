package wanderingMiniBosses.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import wanderingMiniBosses.powers.InkPower;

@SpirePatch(
        clz = TopPanel.class,
        method = "updateDeckViewButtonLogic"
)
public class FunkyCardsPatch9 {
    public static SpireReturn Prefix(TopPanel __instance) {
        if (AbstractDungeon.player.hasPower(InkPower.POWER_ID)) {
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}
