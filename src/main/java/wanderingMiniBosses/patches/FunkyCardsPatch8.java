package wanderingMiniBosses.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.powers.InkPower;
import wanderingMiniBosses.util.TextureLoader;

@SpirePatch(
        clz = TopPanel.class,
        method = "renderDeckIcon"
)
public class FunkyCardsPatch8 {
    static Texture bruh = TextureLoader.getTexture(WanderingminibossesMod.makeUIPath("inkyDeck.png"));
    static float bruh1 = 0;
    static float bruh2 = 0;
    static float bruh3 = 0;

    public static void Postfix(TopPanel __instance, SpriteBatch sb) {
        if (AbstractDungeon.player.hasPower(InkPower.POWER_ID)) {
            if (bruh1 == 0) {
                bruh1 = ((float) ReflectionHacks.getPrivate(__instance, TopPanel.class, "DECK_X"));
            }
            if (bruh2 == 0) {
                bruh2 = ((float) ReflectionHacks.getPrivate(__instance, TopPanel.class, "ICON_Y"));
            }
            if (bruh3 == 0) {
                bruh3 = ((float) ReflectionHacks.getPrivate(__instance, TopPanel.class, "deckAngle"));
            }
            sb.draw(bruh, bruh1 - 32.0F + 32.0F * Settings.scale, bruh2 - 32.0F + 32.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, bruh3, 0, 0, 64, 64, false, false);// 1866
        }
    }
}
