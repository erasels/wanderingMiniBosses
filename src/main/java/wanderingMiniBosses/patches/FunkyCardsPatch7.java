package wanderingMiniBosses.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import wanderingMiniBosses.WanderingminibossesMod;

@SpirePatch(
        clz = AbstractCard.class,
        method = "renderCardTip"
)
public class FunkyCardsPatch7 {
    public static SpireReturn Prefix(AbstractCard __instance, SpriteBatch sb) {
        if (WanderingminibossesMod.inkedCardsList.contains(__instance)) {
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }

}

