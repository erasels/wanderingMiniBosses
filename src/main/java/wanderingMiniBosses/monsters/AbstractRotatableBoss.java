package wanderingMiniBosses.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.RunicDome;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA;
import static com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA;

public abstract class AbstractRotatableBoss extends AbstractWanderingBoss {
    public AbstractRotatableBoss(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
    }

    public AbstractRotatableBoss(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY, boolean ignoreBlights) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY, ignoreBlights);
    }

    public AbstractRotatableBoss(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl);
    }

    protected float rotation = 0F;
    public void setRotation(float rot) {
        this.rotation = rot;
    }
    public float getRotation() {
        return this.rotation;
    }
    protected float scale = Settings.scale;
    public void setScale(float sca) {
        this.scale = sca;
    }
    public float getScale() {
        return this.scale;
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!isDead && !escaped) {
            this.state.update(Gdx.graphics.getDeltaTime());// 62
            this.state.apply(this.skeleton);

            float angle = (float)Math.toRadians(this.rotation);
            this.skeleton.getRootBone().setRotation(this.rotation);
            this.skeleton.getRootBone().setScale(this.scale);
            this.skeleton.updateWorldTransform();
            this.skeleton.setPosition(
                    (float)(Math.cos(angle) * (this.drawX - this.hb.cX) - Math.sin(angle) * (this.drawY - this.hb.cY) + hb.cX),
                    (float)(Math.sin(angle) * (this.drawX - this.hb.cX) + Math.cos(angle) * (this.drawY - this.hb.cY) + hb.cY));
            skeleton.setColor(tint.color);
            skeleton.setFlip(flipHorizontal, flipVertical);
            sb.end();
            CardCrawlGame.psb.begin();
            AbstractMonster.sr.draw(CardCrawlGame.psb, skeleton);
            CardCrawlGame.psb.end();
            sb.begin();
            sb.setBlendFunction(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


            if (!isDying && !isEscaping && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                    && !AbstractDungeon.player.isDead && !AbstractDungeon.player.hasRelic(RunicDome.ID)
                    && intent != Intent.NONE && !Settings.hideCombatElements) {
                renderIntentVfxBehind(sb);
                renderIntent(sb);
                renderIntentVfxAfter(sb);
                renderDamageRange(sb);
            }

            hb.render(sb);
            intentHb.render(sb);
            healthHb.render(sb);
        }
        if (!AbstractDungeon.player.isDead) {
            renderHealth(sb);
            renderName(sb);
        }
    }
}
