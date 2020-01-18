package wanderingMiniBosses.actions;

import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CustomSpawnMonsterAction extends SpawnMonsterAction {
    private AbstractMonster mon;
    private boolean calledPreBattle;

    public CustomSpawnMonsterAction(AbstractMonster m, boolean isMinion) {
        super(m, isMinion);
        mon = m;
        calledPreBattle = false;
    }

    @Override
    public void update() {
        super.update();
        if(isDone && !calledPreBattle) {
            mon.usePreBattleAction();
            calledPreBattle = true;
        }
    }
}
