package wanderingMiniBosses.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.util.OnApplyPowerRelic;
import wanderingMiniBosses.util.TextureLoader;

import static wanderingMiniBosses.WanderingminibossesMod.makeRelicOutlinePath;
import static wanderingMiniBosses.WanderingminibossesMod.makeRelicPath;

public class OtherGremlinHorn extends CustomRelic implements OnApplyPowerRelic {

    public static final String ID = WanderingminibossesMod.makeID("OtherGremlinHorn");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("OtherGremlinHorn.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("OtherGremlinHorn.png"));

    private boolean cardPlayed;
    private boolean triggered;

    public OtherGremlinHorn() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.HEAVY);
        this.energyBased = true;
    }

    @Override
    public void atTurnStart() {
        this.triggered = false;
        this.cardPlayed = false;
    }
    @Override
    public void onPlayerEndTurn() {
        this.cardPlayed = false;
    }
    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        this.cardPlayed = true;
    }

    @Override
    public void onTrigger() {
        if(!this.triggered && this.cardPlayed) {
            this.triggered = true;
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(1));
        }
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
