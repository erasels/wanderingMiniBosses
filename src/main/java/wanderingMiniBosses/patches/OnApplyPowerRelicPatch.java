package wanderingMiniBosses.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;
import wanderingMiniBosses.util.OnApplyPowerRelic;

@SpirePatch(clz = ApplyPowerAction.class, method = "update")
public class OnApplyPowerRelicPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void Insert(ApplyPowerAction __instance) {
        if(__instance.source == AbstractDungeon.player) {
            AbstractPower pow = (AbstractPower) ReflectionHacks.getPrivate(__instance, ApplyPowerAction.class, "powerToApply");
            for(final AbstractRelic relic : AbstractDungeon.player.relics) {
                if(relic instanceof OnApplyPowerRelic) {
                    relic.onTrigger();
                }
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
