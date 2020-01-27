package wanderingMiniBosses.vfx.general;

import com.megacrit.cardcrawl.vfx.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;


//Heavily based on The-Evil-Pickle ReplayTheSpire's ColorSmokeBombEffect
//original version can be found at
//https://github.com/The-Evil-Pickle/Replay-the-Spire/tree/master/src/main/java/com/megacrit/cardcrawl/mod/replay/vfx/combat
public class ColorSmokeBombEffect extends AbstractGameEffect
{
    private float x;
    private float y;
    private Color color;
    private boolean whiff_sound_played;
    
    public ColorSmokeBombEffect(final float x, final float y, final Color color,
    		final float duration) {
    	this(x, y, color);
    	this.duration = duration;
    }
    
    public ColorSmokeBombEffect(final float x, final float y, final Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.duration = 0.2f;
        whiff_sound_played = false;
    }

    @Override
    public void update() {
        if (!whiff_sound_played) {
            CardCrawlGame.sound.play("ATTACK_WHIFF_2");
            for (int i = 0; i < 90; ++i) {
                AbstractDungeon.effectsQueue.add(new ColorSmokeBlur(this.x, this.y, this.color));
            }
            whiff_sound_played = true;
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0f) {
            CardCrawlGame.sound.play("APPEAR");
            this.isDone = true;
        }
    }
    
    @Override
    public void render(final SpriteBatch sb) {
    }
    
    @Override
    public void dispose() {
    }
}