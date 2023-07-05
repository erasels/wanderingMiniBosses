package wanderingMiniBosses.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.util.MiscFunctions;
import wanderingMiniBosses.util.TextureLoader;

import static wanderingMiniBosses.WanderingminibossesMod.makeRelicOutlinePath;
import static wanderingMiniBosses.WanderingminibossesMod.makeRelicPath;

public class Blackblade extends CustomRelic {

    public static final String ID = WanderingminibossesMod.makeID("Blackblade");
    public static final int STRENGTH = 1;

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Blackblade.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Blackblade.png"));

    public Blackblade() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        MiscFunctions.applyToSelf(new StrengthPower(AbstractDungeon.player, STRENGTH));
        MiscFunctions.applyToSelf(new LoseStrengthPower(AbstractDungeon.player, STRENGTH));
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + STRENGTH + DESCRIPTIONS[1];
    }

}
