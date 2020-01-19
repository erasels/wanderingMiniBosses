package wanderingMiniBosses.vfx.general;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.util.TextureLoader;

public class SpookyStuff extends AbstractGameEffect {
    private float x;
    private float y;
    private Vector2 speedVector;
    private float speed;
    private boolean isWhite;
    private static Texture[] imgs;

    public SpookyStuff(float x, float y) {
        this.x = x - 15F;
        this.y = y - 15F;
        this.color = Color.WHITE.cpy();
        imgs = new Texture[12];
        imgs[0] = TextureLoader.getTexture(WanderingminibossesMod.makeUIPath("schema1.png"));
        imgs[1] = TextureLoader.getTexture(WanderingminibossesMod.makeUIPath("schema2.png"));
        imgs[2] = TextureLoader.getTexture(WanderingminibossesMod.makeUIPath("schema3.png"));
        imgs[3] = TextureLoader.getTexture(WanderingminibossesMod.makeUIPath("schema4.png"));
        imgs[4] = TextureLoader.getTexture(WanderingminibossesMod.makeUIPath("schema5.png"));
        imgs[5] = TextureLoader.getTexture(WanderingminibossesMod.makeUIPath("schema6.png"));
        imgs[6] = TextureLoader.getTexture(WanderingminibossesMod.makeUIPath("altschema1.png"));
        imgs[7] = TextureLoader.getTexture(WanderingminibossesMod.makeUIPath("altschema2.png"));
        imgs[8] = TextureLoader.getTexture(WanderingminibossesMod.makeUIPath("altschema3.png"));
        imgs[9] = TextureLoader.getTexture(WanderingminibossesMod.makeUIPath("altschema4.png"));
        imgs[10] = TextureLoader.getTexture(WanderingminibossesMod.makeUIPath("altschema5.png"));
        imgs[11] = TextureLoader.getTexture(WanderingminibossesMod.makeUIPath("altschema6.png"));
        this.duration = 0.6F;
        this.startingDuration = 0.6F;
        this.speed = MathUtils.random(20.0F * Settings.scale, 40.0F * Settings.scale);
        this.speedVector = new Vector2(MathUtils.random(-1.0F, 1.0F), MathUtils.random(-1.0F, 1.0F));
        this.speedVector.nor();
        this.speedVector.angle();
        this.rotation = this.speedVector.angle();
        Vector2 var10000 = this.speedVector;
        var10000.x *= this.speed;
        var10000 = this.speedVector;
        var10000.y *= this.speed;
        isWhite = MathUtils.randomBoolean();

    }

    public void update() {
        this.speed -= Gdx.graphics.getDeltaTime() * 60.0F;
        this.speedVector.nor();
        Vector2 var10000 = this.speedVector;
        var10000.x *= this.speed * Gdx.graphics.getDeltaTime() * 60.0F;
        var10000 = this.speedVector;
        var10000.y *= this.speed * Gdx.graphics.getDeltaTime() * 60.0F;
        this.x += this.speedVector.x;
        this.y += this.speedVector.y;
        super.update();
    }

    public void render(SpriteBatch sb) {
        if (!this.isDone) {
            sb.setColor(Color.WHITE.cpy());
            if (this.isWhite) {
                if (this.duration >= 0.5F) {
                    sb.draw(imgs[0], this.x, this.y, (float) imgs[0].getWidth() / 2.0F, (float) imgs[0].getHeight() / 2.0F, (float) imgs[0].getWidth(), (float) imgs[0].getHeight(), this.scale, this.scale, this.rotation, 0, 0, 30, 30, false, false);
                } else if (this.duration < 0.5F && this.duration >= 0.4F) {
                    sb.draw(imgs[1], this.x, this.y, (float) imgs[1].getWidth() / 2.0F, (float) imgs[1].getHeight() / 2.0F, (float) imgs[1].getWidth(), (float) imgs[1].getHeight(), this.scale, this.scale, this.rotation, 0, 0, 30, 30, false, false);
                } else if (this.duration < 0.4F && this.duration >= 0.3F) {
                    sb.draw(imgs[2], this.x, this.y, (float) imgs[2].getWidth() / 2.0F, (float) imgs[2].getHeight() / 2.0F, (float) imgs[2].getWidth(), (float) imgs[2].getHeight(), this.scale, this.scale, this.rotation, 0, 0, 30, 30, false, false);
                } else if (this.duration < 0.3F && this.duration >= 0.2F) {
                    sb.draw(imgs[3], this.x, this.y, (float) imgs[3].getWidth() / 2.0F, (float) imgs[3].getHeight() / 2.0F, (float) imgs[3].getWidth(), (float) imgs[3].getHeight(), this.scale, this.scale, this.rotation, 0, 0, 30, 30, false, false);
                } else if (this.duration < 0.2F && this.duration >= 0.1F) {
                    sb.draw(imgs[4], this.x, this.y, (float) imgs[4].getWidth() / 2.0F, (float) imgs[4].getHeight() / 2.0F, (float) imgs[4].getWidth(), (float) imgs[4].getHeight(), this.scale, this.scale, this.rotation, 0, 0, 30, 30, false, false);
                } else if (this.duration < 0.1F) {
                    sb.draw(imgs[5], this.x, this.y, (float) imgs[5].getWidth() / 2.0F, (float) imgs[5].getHeight() / 2.0F, (float) imgs[5].getWidth(), (float) imgs[5].getHeight(), this.scale, this.scale, this.rotation, 0, 0, 30, 30, false, false);
                }
            } else {
                if (this.duration >= 0.5F) {
                    sb.draw(imgs[6], this.x, this.y, (float) imgs[6].getWidth() / 2.0F, (float) imgs[6].getHeight() / 2.0F, (float) imgs[6].getWidth(), (float) imgs[6].getHeight(), this.scale, this.scale, this.rotation, 0, 0, 30, 30, false, false);
                } else if (this.duration < 0.5F && this.duration >= 0.4F) {
                    sb.draw(imgs[7], this.x, this.y, (float) imgs[7].getWidth() / 2.0F, (float) imgs[7].getHeight() / 2.0F, (float) imgs[7].getWidth(), (float) imgs[7].getHeight(), this.scale, this.scale, this.rotation, 0, 0, 30, 30, false, false);
                } else if (this.duration < 0.4F && this.duration >= 0.3F) {
                    sb.draw(imgs[8], this.x, this.y, (float) imgs[8].getWidth() / 2.0F, (float) imgs[8].getHeight() / 2.0F, (float) imgs[8].getWidth(), (float) imgs[8].getHeight(), this.scale, this.scale, this.rotation, 0, 0, 30, 30, false, false);
                } else if (this.duration < 0.3F && this.duration >= 0.2F) {
                    sb.draw(imgs[9], this.x, this.y, (float) imgs[9].getWidth() / 2.0F, (float) imgs[9].getHeight() / 2.0F, (float) imgs[9].getWidth(), (float) imgs[9].getHeight(), this.scale, this.scale, this.rotation, 0, 0, 30, 30, false, false);
                } else if (this.duration < 0.2F && this.duration >= 0.1F) {
                    sb.draw(imgs[10], this.x, this.y, (float) imgs[10].getWidth() / 2.0F, (float) imgs[10].getHeight() / 2.0F, (float) imgs[10].getWidth(), (float) imgs[10].getHeight(), this.scale, this.scale, this.rotation, 0, 0, 30, 30, false, false);
                } else if (this.duration < 0.1F) {
                    sb.draw(imgs[11], this.x, this.y, (float) imgs[11].getWidth() / 2.0F, (float) imgs[11].getHeight() / 2.0F, (float) imgs[11].getWidth(), (float) imgs[11].getHeight(), this.scale, this.scale, this.rotation, 0, 0, 30, 30, false, false);
                }
            }
        }
    }

    public void dispose() {
    }
}
