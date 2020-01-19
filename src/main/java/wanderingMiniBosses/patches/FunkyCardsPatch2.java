package wanderingMiniBosses.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wanderingMiniBosses.WanderingminibossesMod;

import java.util.Iterator;

@SpirePatch(
        clz = CardGroup.class,
        method = "glowCheck"
)
public class FunkyCardsPatch2 {
    public static SpireReturn Prefix(CardGroup __instance) {
        if (!(WanderingminibossesMod.inkedCardsList.isEmpty())) {
            AbstractCard c;
            for (Iterator var1 = __instance.group.iterator(); var1.hasNext(); c.triggerOnGlowCheck()) {// 439 445
                c = (AbstractCard) var1.next();
                if (c.canUse(AbstractDungeon.player, (AbstractMonster) null) && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.HAND_SELECT) {// 440
                    c.beginGlowing();// 441
                } else if (!WanderingminibossesMod.inkedCardsList.contains(c)) {
                    c.stopGlowing();// 443
                }
            }
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}

