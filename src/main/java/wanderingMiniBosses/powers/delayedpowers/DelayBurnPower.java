package wanderingMiniBosses.powers.delayedpowers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.actions.SetPlayerBurnAction;


public class DelayBurnPower extends AbstractDelayedPower {
    public static final String POWER_ID = WanderingminibossesMod.makeID("DelayBurnPower");

    public DelayBurnPower(AbstractCreature owner) {
        super(POWER_ID, owner);
    }

    @Override
    public void atEndOfRound() {
        super.atEndOfRound();
        addToBot(new SetPlayerBurnAction());
    }
}
