package wanderingMiniBosses.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.Ectoplasm;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.util.TextureLoader;

public class FilledWithCoinsPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = WanderingminibossesMod.makeID("FilledWithCoinsPower");
    private static final Texture tex84 = TextureLoader.getTexture("wanderingMiniBossesResources/images/powers/FilledWithCoins_84.png");
    private static final Texture tex32 = TextureLoader.getTexture("wanderingMiniBossesResources/images/powers/FilledWithCoins_32.png");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public FilledWithCoinsPower(final AbstractCreature owner) {
        name = powerStrings.NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = -1;

        type = PowerType.DEBUFF;
        isTurnBased = false;
        canGoNegative = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner == AbstractDungeon.player && damageAmount > 0) {
            if (!AbstractDungeon.player.hasRelic(Ectoplasm.ID))
                for (int i = 0; i < damageAmount; ++i) {// 117
                    AbstractDungeon.effectList.add(new GainPennyEffect(this.owner, this.owner.hb.cX, this.owner.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, true));// 121
                }
            AbstractDungeon.player.gainGold(damageAmount);
        }
        return damageAmount;
    }

    @Override
    public AbstractPower makeCopy() {
        return new FilledWithCoinsPower(owner);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}