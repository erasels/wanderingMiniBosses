package wanderingMiniBosses.powers.eternalPrincessPowers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.util.TextureLoader;

import static wanderingMiniBosses.WanderingminibossesMod.makePowerPath;


public class BountyOfJustice extends AbstractPower {

    public static final String POWER_ID = WanderingminibossesMod.makeID("BountyOfJustice");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final int BOUNTY = 60;
    private boolean failed = false;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Coin84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Coin32.png"));

    public BountyOfJustice(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = BOUNTY;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void onAfterCardPlayed(AbstractCard card) {
        if (card.type == AbstractCard.CardType.SKILL) {
            failed = true;
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!failed) {
            this.flash();
            AbstractDungeon.player.gainGold(BOUNTY);
        }
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, ID));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new BountyOfMercy(this.owner)));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
