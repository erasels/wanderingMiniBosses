package wanderingMiniBosses.powers.gazepowers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import wanderingMiniBosses.WanderingminibossesMod;

public class GazeOne extends AbstractGazePower {
    public static final String POWER_ID = WanderingminibossesMod.makeID("GazeOne");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    private int energy;

    public GazeOne(AbstractCreature owner) {
        super(owner, Color.WHITE, 1);
        this.ID = POWER_ID;
        this.name = NAME;
        this.isTurnBased = false;

        if(AbstractDungeon.ascensionLevel >= 9) {
            this.energy = 5;
        } else {
            this.energy = 1;
        }

        this.updateDescription();
    }


    @Override
    protected void trigger(AbstractCard card) {
        for(int i = 0; i < AbstractDungeon.player.hand.size(); i++) {
            if(AbstractDungeon.player.hand.group.get(i) == card) {
                AbstractCard tmp;
                if(i > 0) {
                    tmp = AbstractDungeon.player.hand.group.get(i - 1);
                    tmp.setCostForTurn(tmp.costForTurn + this.energy);
                }
                if(i < AbstractDungeon.player.hand.size() - 1) {
                    tmp = AbstractDungeon.player.hand.group.get(i + 1);
                    tmp.setCostForTurn(tmp.costForTurn + this.energy);
                }

                break;
            }
        }
    }


    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.energy + DESCRIPTIONS[1];
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
