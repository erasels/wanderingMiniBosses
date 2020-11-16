package wanderingMiniBosses.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.actions.SpawnTolerantDamageAllEnemiesAction;
import wanderingMiniBosses.util.TextureLoader;

import static wanderingMiniBosses.WanderingminibossesMod.makeRelicOutlinePath;
import static wanderingMiniBosses.WanderingminibossesMod.makeRelicPath;

public class CarrionFlame extends CustomRelic {
    public static final String ID = WanderingminibossesMod.makeID("CarrionFlame");
    public static final float HPCONVERSION = 0.5f;

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("CarrionFlame.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("CarrionFlame.png"));

    private int timesTriggeredThisTurn;

    public CarrionFlame() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (m.currentHealth == 0) { //idk gremlin horn does it so I will too
            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead() && ++timesTriggeredThisTurn < 10) {
                addToBot(new RelicAboveCreatureAction(m, this));
                addToBot(new VFXAction(new ShockWaveEffect(m.hb.cX, m.hb.cY, Color.ORANGE, ShockWaveEffect.ShockWaveType.CHAOTIC)));
                addToBot(new SpawnTolerantDamageAllEnemiesAction(AbstractDungeon.player, MathUtils.floor((float)m.maxHealth*HPCONVERSION), true, false, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, false));
                this.flash();
            }
        }
    }

    @Override
    public void atTurnStart() {
        timesTriggeredThisTurn = 0;
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + MathUtils.round(HPCONVERSION*100f) + DESCRIPTIONS[1];
    }

}
