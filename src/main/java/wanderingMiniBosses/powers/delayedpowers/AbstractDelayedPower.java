package wanderingMiniBosses.powers.delayedpowers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class AbstractDelayedPower extends AbstractPower implements InvisiblePower {

    public AbstractDelayedPower(String POWER_ID, AbstractCreature owner) {
        this(POWER_ID, owner, -1);
    }

    public AbstractDelayedPower(String POWER_ID, AbstractCreature owner, int amount) {
        this.owner = owner;
        this.amount = amount;

        this.isTurnBased = false;

        this.name = "";
        this.ID = POWER_ID;
        this.type = PowerType.BUFF;

        this.description = "";

        this.loadRegion("time");
    }

    @Override
    public void updateDescription() {
    }

    @Override
    public void atEndOfRound() {
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }
}