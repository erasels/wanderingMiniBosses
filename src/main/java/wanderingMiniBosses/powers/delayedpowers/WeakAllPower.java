package wanderingMiniBosses.powers.delayedpowers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import wanderingMiniBosses.WanderingminibossesMod;


public class WeakAllPower extends AbstractDelayedPower {
    public static final String POWER_ID = WanderingminibossesMod.makeID("WeakAllPower");

    public WeakAllPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public void atEndOfRound() {
        super.atEndOfRound();

        addToBot(new ApplyPowerAction(AbstractDungeon.player, this.owner, new WeakPower(AbstractDungeon.player, amount, false), amount));
        for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if(mo != this.owner && !mo.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(mo, this.owner, new WeakPower(mo, amount, false), amount));
            }
        }
    }
}
