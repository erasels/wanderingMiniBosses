package wanderingMiniBosses.actions;

import basemod.devcommands.blight.BlightRemove;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PowerExpireTextEffect;
import wanderingMiniBosses.util.MiscFunctions;

import java.util.Iterator;

public class RemoveSpecificBlightAction extends AbstractGameAction  {

    private AbstractBlight blightInstance;
    private static final float DURATION = 0.1F;

    public RemoveSpecificBlightAction(AbstractBlight blightInstance) {
        this.actionType = ActionType.SPECIAL;
        this.duration = 0.1F;
        this.blightInstance = blightInstance;
    }

    public void update() {
        if (this.duration <= 0.1F)
        {
            AbstractBlight removeMe = blightInstance;

            if (removeMe != null) {
                BlightRemove blight_remove_command = new BlightRemove();
                blight_remove_command.execute(new String[] {"blah", "blah", removeMe.blightID}, 0);
            }

            this.isDone = true;

        }

        this.tickDuration();
    }

}
