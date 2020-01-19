package wanderingMiniBosses.powers.delayedpowers;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import wanderingMiniBosses.WanderingminibossesMod;

public class StrengthenAllPower extends AbstractDelayedPower {
    public static final String POWER_ID = WanderingminibossesMod.makeID("StrengthenAllPower");

    public StrengthenAllPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public void atEndOfRound() {
        super.atEndOfRound();

        addToBot(new VFXAction(new InflameEffect(AbstractDungeon.player)));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, this.owner, new StrengthPower(AbstractDungeon.player, this.amount), this.amount));
        addToBot(new GainEnergyAction(1));
        for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if(mo != this.owner && !mo.isDeadOrEscaped()) {
                if(mo.id == Byrd.ID) {
                    addToBot(new GainBlockAction(mo, this.owner, AbstractDungeon.actNum * 5));
                } else {
                    addToBot(new VFXAction(new InflameEffect(mo)));
                    addToBot(new ApplyPowerAction(mo, this.owner, new StrengthPower(mo, this.amount), this.amount));
                }
            }
        }
    }
}
