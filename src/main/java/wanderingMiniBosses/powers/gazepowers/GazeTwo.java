package wanderingMiniBosses.powers.gazepowers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.PowerStrings;
import wanderingMiniBosses.WanderingminibossesMod;

public class GazeTwo extends AbstractGazePower {
    public static final String POWER_ID = WanderingminibossesMod.makeID("GazeTwo");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public GazeTwo(AbstractCreature owner) {
        super(owner, Color.BLUE, 2);
        this.ID = POWER_ID;
        this.name = NAME;
        this.isTurnBased = false;

        this.updateDescription();
    }


    @Override
    protected void trigger(AbstractCard card) {
        if(card.type != AbstractCard.CardType.POWER && !card.exhaust) {
            AbstractCard tmpcard = null;
            if(AbstractDungeon.ascensionLevel >= 19) {
                tmpcard = CardLibrary.getCurse(CardLibrary.getCard(Dazed.ID), AbstractDungeon.monsterRng).makeCopy();
            } else if(AbstractDungeon.ascensionLevel >= 9) {
                tmpcard = new Slimed();
            }

            if(tmpcard != null) {
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(tmpcard, 1));
            }
        }
        card.purgeOnUse = true;
    }


    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
        if(AbstractDungeon.ascensionLevel >= 19) {
            this.description += DESCRIPTIONS[3];
        } else if(AbstractDungeon.ascensionLevel >= 9) {
            this.description += DESCRIPTIONS[2];
        } else {
            this.description += DESCRIPTIONS[1];
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
