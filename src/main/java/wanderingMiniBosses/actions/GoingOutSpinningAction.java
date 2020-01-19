package wanderingMiniBosses.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import wanderingMiniBosses.monsters.AbstractRotatableBoss;

public class GoingOutSpinningAction extends AbstractGameAction {
    private AbstractRotatableBoss target;
    private float vRot, rotation;
    private float vScale, scale;

    public GoingOutSpinningAction(AbstractRotatableBoss target) {
        super();
        this.target = target;
        this.scale = target.getScale();
        this.rotation = target.getRotation();
        vRot = 0;
        vScale = 0;
    }

    @Override
    public void update() {
        vRot += Gdx.graphics.getDeltaTime() * 60;
        vScale += Gdx.graphics.getDeltaTime() / 30 * Settings.scale;

        scale -= vScale;
        rotation += vRot;

        if(scale <= 0F) {
            this.isDone = true;
            target.escape();
            target.escapeTimer = -1F;
        } else {
            target.setRotation(rotation);
            target.setScale(scale);
        }
    }
}
