package wanderingMiniBosses.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.vfx.general.StraightFireParticle;

import java.util.Random;

public class InnerFlamePower extends AbstractWBPower implements CloneablePowerInterface {
    public static final String POWER_ID = WanderingminibossesMod.makeID("InnerFlame");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static Random rng = new Random();
    public static Color tintCol = Color.ORANGE.cpy().add(0.1f, 0.3f, 0.3f, 1.0f);
    private static final float INTERVAL = 0.05f;
    private float particleTimer;

    public InnerFlamePower(AbstractCreature owner, int num1, int num2) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        loadRegion("explosive");
        amount = num1;
        amount2 = num2;
        this.type = AbstractPower.PowerType.BUFF;
        priority = 0;
        isTurnBased = false;
        updateDescription();
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + amount2 + DESCRIPTIONS[2];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if(!isPlayer) {
            addToBot(new DamageAction(owner, new DamageInfo(owner, amount, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.FIRE));
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount2), amount2));
        }
    }

    @Override
    public void updateParticles() {
        this.particleTimer -= Gdx.graphics.getRawDeltaTime();
        if (this.particleTimer < 0.0F) {
            float xOff = ((owner.hb_w) * (float) rng.nextGaussian())*0.25f;
            if(MathUtils.randomBoolean()) {
                xOff = -xOff;
            }
            AbstractDungeon.effectList.add(new StraightFireParticle(owner.drawX + xOff, owner.drawY + MathUtils.random(owner.hb_h/2f), 75f));
            this.particleTimer = INTERVAL;
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "render")
    public static class ChangeColorOfAffectedMonsterPls {
        @SpirePrefixPatch
        public static void patch(AbstractMonster __instance, SpriteBatch sb) {
            if(__instance.hasPower(InnerFlamePower.POWER_ID)) {
                __instance.tint.color.mul(InnerFlamePower.tintCol);
            }
        }
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        particleTimer = 0.4f;
    }

    @Override
    public AbstractPower makeCopy() {
        return new InnerFlamePower(owner, amount, amount2);
    }
}