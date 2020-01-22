package wanderingMiniBosses.actions;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import wanderingMiniBosses.monsters.gremlinknight.GremlinKnight;

public class OneMoreKickAction extends AbstractGameAction {
    private final float speed = 400F * Settings.scale;
    private boolean collided;

    public OneMoreKickAction(AbstractCreature target, AbstractCreature source) {
        super();
        this.target = target;
        this.source = source;
        this.collided = false;
        this.source.flipHorizontal = !this.source.flipHorizontal;
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
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AttackEffect.BLUNT_HEAVY));
            }
        } else if(this.duration >= 0F) {
            this.duration -= Gdx.graphics.getDeltaTime();
            RammingEntranceAction.moveOrbs(speed * Gdx.graphics.getDeltaTime());
            source.drawX -= speed / 2F * Gdx.graphics.getDeltaTime();
            if(this.duration < 0F) {
                source.flipHorizontal = !source.flipHorizontal;
            }
        } else {
            source.drawX -= speed * 5 * Gdx.graphics.getDeltaTime();
            if(source.drawX < -source.hb.width) {
                AbstractDungeon.getCurrRoom().monsters.monsters.remove(source);
                GremlinKnight.checkBattleEnd();
                this.isDone = true;
            }
        }
    }
}
