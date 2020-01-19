package wanderingMiniBosses.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import wanderingMiniBosses.cards.BossBurn;
import wanderingMiniBosses.powers.BurnPower;

public class SetPlayerBurnAction extends AbstractGameAction {
    private static int exist = 0;

    private int count;

    public SetPlayerBurnAction() {
        this(0);
    }
    public SetPlayerBurnAction(int add) {
        exist++;

        this.count = add;
    }

    @Override
    public void update() {
        if(--exist == 0) {
            int playercount = AbstractDungeon.player.hasPower(BurnPower.POWER_ID) ? AbstractDungeon.player.getPower(BurnPower.POWER_ID).amount : 0;
            CardGroup[] cg = new CardGroup[]{AbstractDungeon.player.drawPile, AbstractDungeon.player.hand, AbstractDungeon.player.discardPile};
            for(final CardGroup group : cg) {
                for (final AbstractCard card : group.group) {
                    if(card.cardID == BossBurn.ID) {
                        count += card.timesUpgraded + 1;
                    }
                }
            }
            if(playercount < count) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.getCurrRoom().monsters.monsters.get(0),
                        new BurnPower(AbstractDungeon.player, count - playercount), count - playercount));
            } else if(count < playercount) {
                if(count == 0) {
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.getCurrRoom().monsters.monsters.get(0),
                            BurnPower.POWER_ID));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.getCurrRoom().monsters.monsters.get(0),
                            BurnPower.POWER_ID, playercount - count));
                }
                CardCrawlGame.sound.play("HEAL_" + MathUtils.random(1, 3));
            }
        }

        this.isDone = true;
    }

    public static void addToBottom(int add) {
        AbstractDungeon.actionManager.addToBottom(new SetPlayerBurnAction(add));
    }
    public static void addToBottom() {
        addToBottom(0);
    }
}
