package wanderingMiniBosses.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.OnAnyPowerAppliedRelic;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.util.TextureLoader;

import static wanderingMiniBosses.WanderingminibossesMod.makeRelicOutlinePath;
import static wanderingMiniBosses.WanderingminibossesMod.makeRelicPath;

public class OtherGremlinHorn extends CustomRelic implements OnAnyPowerAppliedRelic {

    public static final String ID = WanderingminibossesMod.makeID("OtherGremlinHorn");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("OtherGremlinHorn.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("OtherGremlinHorn.png"));

    private boolean triggered;

    public OtherGremlinHorn() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.HEAVY);
        this.energyBased = true;
    }

    @Override
    public void atTurnStart() {
        this.triggered = false;
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public boolean onAnyPowerApply(AbstractPower pow, AbstractCreature target, AbstractCreature source) {
        if(!triggered && (pow instanceof WeakPower || pow instanceof VulnerablePower) && source instanceof AbstractPlayer) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(1));
        }

        return true;
    }


}
