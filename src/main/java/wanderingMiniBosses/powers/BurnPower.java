package wanderingMiniBosses.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.powers.AbstractPower;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.actions.SetPlayerBurnAction;
import wanderingMiniBosses.util.TextureLoader;


public class BurnPower extends AbstractPower {
    public static final String POWER_ID = WanderingminibossesMod.makeID("BurnPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.DEBUFF;

    public BurnPower(AbstractCreature owner, int amount) {
        super();
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        updateDescription();
        this.priority = -99;

        this.region128 =
                new TextureAtlas.AtlasRegion(
                        TextureLoader.getTexture(WanderingminibossesMod.makePowerPath("burn128.png")), 0, 0, 128, 128);
        this.region48 =
                new TextureAtlas.AtlasRegion(
                        TextureLoader.getTexture(WanderingminibossesMod.makePowerPath("burn48.png")), 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            damage += this.amount;
        }
        return damage;
    }

    @Override
    public void onRemove() {
        SetPlayerBurnAction.addToBottom();
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
