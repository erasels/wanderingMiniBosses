package wanderingMiniBosses.vfx.general;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.SpotlightEffect;

public class GaudyEntranceVFX extends SpotlightEffect {
    private AbstractCreature target;
    private final float pixels;
    private final float startingDuration;

    public GaudyEntranceVFX(AbstractCreature target) {
        super();
        this.target = target;
        this.startingDuration = this.duration - 1.5F;
        pixels = Settings.HEIGHT - target.drawY;
        target.drawY += pixels;
        this.color = Color.PURPLE.cpy();
        this.color.a = 0.5F;
    }

    @Override
    public void update() {
        super.update();

        if (this.duration > 1.5F) {
            target.drawY -= Gdx.graphics.getDeltaTime() * pixels / this.startingDuration;
        }
    }
}
