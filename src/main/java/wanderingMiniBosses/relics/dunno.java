package wanderingMiniBosses.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.util.TextureLoader;

import static wanderingMiniBosses.WanderingminibossesMod.makeRelicOutlinePath;
import static wanderingMiniBosses.WanderingminibossesMod.makeRelicPath;

public class dunno extends CustomRelic {
    public static final String ID = WanderingminibossesMod.makeID("???");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Blackblade.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Blackblade.png"));


    public dunno() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
