package wanderingMiniBosses.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.util.TextureLoader;

public class InterruptMePower2 extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = WanderingminibossesMod.makeID("InterruptMePower2");
    private static final Texture tex84 = TextureLoader.getTexture("wanderingMiniBossesResources/images/powers/InterruptMe_84.png");
    private static final Texture tex32 = TextureLoader.getTexture("wanderingMiniBossesResources/images/powers/InterruptMe_32.png");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(InterruptMePower.POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public InterruptMePower2(final AbstractCreature owner, final int amount) {
        name = powerStrings.NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.DEBUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        this.flashWithoutSound();
        amount -= damageAmount;
        if (amount <= 0) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
            AbstractDungeon.actionManager.addToBottom(new StunMonsterAction((AbstractMonster) owner, owner));
        }
        return damageAmount;
    }

    @Override
    public AbstractPower makeCopy() {
        return new InterruptMePower2(owner, amount);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}