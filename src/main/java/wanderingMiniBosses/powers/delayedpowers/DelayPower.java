package wanderingMiniBosses.powers.delayedpowers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DelayPower extends AbstractPower implements NonStackablePower {

    private AbstractPower pow;

    public DelayPower(AbstractPower pow) {
        this.pow = pow;
        this.region48 = pow.region48;
        this.region128 = pow.region128;
        this.owner = pow.owner;
        this.name = pow.name;
        this.description = pow.description;
        this.type = this.pow.type;
    }


    @Override
    public void atEndOfRound() {
        if(this.pow.owner instanceof AbstractMonster) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            addToBot(new ApplyPowerAction(this.owner, this.owner, this.pow));
        }
    }
    @Override
    public void atStartOfTurn() {
        if(this.pow.owner instanceof AbstractPlayer) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            addToBot(new ApplyPowerAction(this.owner, this.owner, this.pow));
        }
    }

    public static void addToBottom(AbstractPower pow) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(pow.owner, pow.owner, pow));
    }
    public static void addToTop(AbstractPower pow) {
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(pow.owner, pow.owner, pow));
    }
}
