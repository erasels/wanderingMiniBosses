package wanderingMiniBosses.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CustomSpawnMonsterAction extends SpawnMonsterAction {
    private AbstractMonster mon;

    public CustomSpawnMonsterAction(AbstractMonster m, boolean isMinion) {
        super(m, isMinion);
        mon = m;
    }

    @Override
    public void update() {
        super.update();
        if(isDone) {
            mon.usePreBattleAction();
            AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    mon.createIntent();
                    isDone = true;
                }
            });
            AbstractDungeon.actionManager.addToTop(new RollMoveAction(mon));
        }
    }
}
