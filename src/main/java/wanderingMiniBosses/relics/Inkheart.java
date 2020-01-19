package wanderingMiniBosses.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.util.TextureLoader;

import static wanderingMiniBosses.WanderingminibossesMod.makeRelicOutlinePath;
import static wanderingMiniBosses.WanderingminibossesMod.makeRelicPath;

public class Inkheart extends CustomRelic {

    public static final String ID = WanderingminibossesMod.makeID("Inkheart");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Inkheart.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Inkheart.png"));

    public Inkheart() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }



    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
