package wanderingMiniBosses.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.util.TextureLoader;

@SpirePatch(
        clz = AbstractCard.class,
        method = "renderDescription"
)
public class FunkyCardsPatch4 {
    static Texture bruh = TextureLoader.getTexture(WanderingminibossesMod.makeUIPath("inkyText.png"));
    static TextureAtlas.AtlasRegion inkyTitle = new TextureAtlas.AtlasRegion(bruh, 0, 0, bruh.getWidth(), bruh.getHeight());

    public static SpireReturn Prefix(AbstractCard __instance, SpriteBatch sb) {
        if (WanderingminibossesMod.inkedCardsList.contains(__instance)) {
            renderHelper(__instance, sb, Color.WHITE.cpy(), inkyTitle, __instance.current_x, __instance.current_y);// 2727
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }

    private static void renderHelper(AbstractCard __instance, SpriteBatch sb, Color color, TextureAtlas.AtlasRegion img, float drawX, float drawY) {
        sb.setColor(color);// 1427
        sb.draw(img, drawX + img.offsetX - (float) img.originalWidth / 2.0F, drawY + img.offsetY - (float) img.originalHeight / 2.0F, (float) img.originalWidth / 2.0F - img.offsetX, (float) img.originalHeight / 2.0F - img.offsetY, (float) img.packedWidth, (float) img.packedHeight, __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle);// 1428
    }
}

