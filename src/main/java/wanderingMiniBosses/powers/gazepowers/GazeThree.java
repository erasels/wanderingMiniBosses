package wanderingMiniBosses.powers.gazepowers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.Defend_Blue;
import com.megacrit.cardcrawl.cards.blue.Strike_Blue;
import com.megacrit.cardcrawl.cards.green.Defend_Green;
import com.megacrit.cardcrawl.cards.green.Strike_Green;
import com.megacrit.cardcrawl.cards.red.Defend_Red;
import com.megacrit.cardcrawl.cards.red.Strike_Red;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import wanderingMiniBosses.WanderingminibossesMod;

import java.util.ArrayList;

public class GazeThree extends AbstractGazePower {
    public static final String POWER_ID = WanderingminibossesMod.makeID("GazeThree");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    private int strength;

    private ArrayList<AbstractCard> basics = new ArrayList<>();
    private ArrayList<AbstractCard> strikes = new ArrayList<>();

    public GazeThree(AbstractCreature owner) {
        super(owner, Color.RED, 3);
        this.ID = POWER_ID;
        this.name = NAME;
        this.isTurnBased = false;

        this.strength = AbstractDungeon.ascensionLevel >= 19 ? 2 : 1;

        this.updateDescription();
    }


    @Override
    protected void trigger(AbstractCard card) {
        ArrayList<String> cards = AbstractDungeon.player.getStartingDeck();
        if(cards.isEmpty()) {
            cards.add(Strike_Blue.ID);
            cards.add(Strike_Green.ID);
            cards.add(Strike_Red.ID);
            cards.add(Defend_Red.ID);
            cards.add(Defend_Green.ID);
            cards.add(Defend_Blue.ID);
        }
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(CardLibrary.getCard(cards.get(AbstractDungeon.monsterRng.random(cards.size() - 1))).makeCopy(), 1));
        if(AbstractDungeon.ascensionLevel >= 4) {
            for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if(!mo.isDeadOrEscaped()) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, this.owner, new StrengthPower(mo, this.strength), this.strength));
                }
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
        if(AbstractDungeon.ascensionLevel >= 4) {
            this.description += DESCRIPTIONS[1] + this.strength + DESCRIPTIONS[2];
        }
        this.description += DESCRIPTIONS[3];
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
