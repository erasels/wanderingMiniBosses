package wanderingMiniBosses.actions;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BackAttackPower;
import com.megacrit.cardcrawl.powers.SurroundedPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import wanderingMiniBosses.monsters.AbstractRotatableBoss;

public class RammingEntranceAction extends AbstractGameAction {
    private final float speed = 400F * Settings.scale;
    private boolean collided;

    public RammingEntranceAction(AbstractCreature target, AbstractCreature source) {
        super();
        this.target = target;
        this.source = source;
        this.collided = false;
    }

    @Override
    public void update() {
        if(!collided) {
            if(source.drawX < target.drawX - target.hb.width / 2) {
                source.drawX += speed * 5 * Gdx.graphics.getDeltaTime();
            } else {
                collided = true;
                source.useHopAnimation();
                target.useHopAnimation();
                this.duration = (float) ReflectionHacks.getPrivate(source, AbstractCreature.class, "animationTimer");
                //AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(source, source, new BackAttackPower(target)));
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, source, new SurroundedPower(target)));
                AbstractDungeon.actionManager.addToTop(new VFXAction(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AttackEffect.BLUNT_HEAVY)));
            }
        } else {
            this.tickDuration();
            target.drawX += speed * Gdx.graphics.getDeltaTime();
            source.drawX -= speed / 2F * Gdx.graphics.getDeltaTime();
        }
    }
}
