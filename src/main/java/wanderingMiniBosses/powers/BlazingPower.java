package wanderingMiniBosses.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import wanderingMiniBosses.WanderingminibossesMod;

public class BlazingPower extends AbstractWBPower implements CloneablePowerInterface {
    public static final String POWER_ID = WanderingminibossesMod.makeID("Blazing");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean triggered = false;
    private static AbstractCard card = new Burn();
    private static final int CARD_AMT = 1;

    public BlazingPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        updateDescription();
        loadRegion("attackBurn");
        this.type = AbstractPower.PowerType.BUFF;
        priority = 0;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + CARD_AMT + DESCRIPTIONS[1];
    }

    public void onInitialApplication() {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(AbstractDungeon.player, new RoomTintEffect(Color.FIREBRICK.cpy(), 0.2f, 999f, true), 0.0F));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(owner, new ScreenOnFireEffect(), 0.25F));
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (!triggered && info.owner == AbstractDungeon.player && damageAmount > 0 && info.type != DamageInfo.DamageType.HP_LOSS && info.type != DamageInfo.DamageType.THORNS) {
            triggered = true;
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(card.makeCopy(), CARD_AMT));
        }
        return damageAmount;
    }

    @Override
    public void atStartOfTurn() {
        triggered = false;
    }

    @Override
    public AbstractPower makeCopy() {
        return new BlazingPower(owner);
    }
}