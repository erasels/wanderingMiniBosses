package wanderingMiniBosses.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.util.TextureLoader;

public class InkPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = WanderingminibossesMod.makeID("InkPower");
    private static final Texture tex84 = TextureLoader.getTexture("wanderingMiniBossesResources/images/powers/Ink_84.png");
    private static final Texture tex32 = TextureLoader.getTexture("wanderingMiniBossesResources/images/powers/Ink_32.png");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public AbstractCreature source;

    int cardsDrawnThisTurn = 0;

    public InkPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = powerStrings.NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.DEBUFF;
        isTurnBased = false;
        canGoNegative = false;


        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atEndOfRound() {
        super.atEndOfRound();
        cardsDrawnThisTurn = 0;
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        super.onCardDraw(card);
        if (cardsDrawnThisTurn < this.amount) {
            WanderingminibossesMod.inkedCardsList.add(card);
            card.target = AbstractCard.CardTarget.SELF_AND_ENEMY;
            cardsDrawnThisTurn++;
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new InkPower(owner, source, amount);
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0];
        } else {
            description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }
    }
}