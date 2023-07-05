package wanderingMiniBosses.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class FinaleOfPromiseAction extends AbstractGameAction {
    public static final String[] TEXT;
    private AbstractPlayer player;
    private int playAmt;
    private int threshold;
    private int numCards;

    public FinaleOfPromiseAction(int playAmt, int threshold, int numCards) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.playAmt = playAmt;
        this.threshold = threshold;
        this.numCards = numCards;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.player.discardPile.isEmpty()) {
                this.isDone = true;
            } else {
                CardGroup temp = new CardGroup(CardGroupType.UNSPECIFIED);
                for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                    if (card.costForTurn <= threshold) {
                        temp.addToTop(card);
                    }
                }

                if (temp.isEmpty()) {
                    this.isDone = true;
                    return;
                }

                if (temp.size() <= numCards) {
                    for (AbstractCard c : temp.group) {
                        for(int i = 0; i < this.playAmt; ++i) {
                            AbstractCard tmp = c.makeStatEquivalentCopy();
                            tmp.purgeOnUse = true;
                            this.addToBot(new NewQueueCardAction(tmp, true, false, true));
                        }
                    }
                    this.isDone = true;
                    return;
                }

                temp.sortAlphabetically(true);
                temp.sortByRarityPlusStatusCardType(false);
                AbstractDungeon.gridSelectScreen.open(temp, numCards, TEXT[0] + numCards + TEXT[1], false);
                this.tickDuration();
            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {

                for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    for (int i = 0; i < this.playAmt; ++i) {
                        AbstractCard tmp = c.makeStatEquivalentCopy();
                        tmp.purgeOnUse = true;
                        this.addToBot(new NewQueueCardAction(tmp, true, false, true));
                    }
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }

            this.tickDuration();
        }
    }

    static {
        TEXT = CardCrawlGame.languagePack.getUIString("wanderingMiniBosses:FinaleOfPromiseAction").TEXT;
    }
}
