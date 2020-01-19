package wanderingMiniBosses.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.util.TextureLoader;

import static wanderingMiniBosses.WanderingminibossesMod.makeRelicOutlinePath;
import static wanderingMiniBosses.WanderingminibossesMod.makeRelicPath;

public class AbyssPearl extends CustomRelic {

    public static final String ID = WanderingminibossesMod.makeID("AbyssPearl");
    public static final int STRENGTH = 2;
    public static final int VULNERABLE = 1;
    public static final int BLOCK = 5;

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("AbyssPearl.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("AbyssPearl.png"));

    public AbyssPearl() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    private boolean triggerEffects;

    public void onUseCard(AbstractCard card, UseCardAction action) {
        this.triggerEffects = false;
        if(!card.purgeOnUse && card.type == AbstractCard.CardType.ATTACK && this.counter > -1) {
            if(++this.counter >= 4) {
                this.counter = -1;
            } else {
                this.triggerEffects = true;
            }
        }
    }

    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if(triggerEffects && info.type == DamageInfo.DamageType.NORMAL) {
            switch (this.counter) {
                case 1:
                    if(target.hasPower(ArtifactPower.POWER_ID)) {
                        addToBot(new ReducePowerAction(target, AbstractDungeon.player, ArtifactPower.POWER_ID, 1));
                    } else {
                        addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new StrengthPower(target, -STRENGTH), -STRENGTH));
                        addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new GainStrengthPower(target, STRENGTH), STRENGTH));
                    }
                    break;
                case 2:
                    addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new VulnerablePower(target, VULNERABLE, false), VULNERABLE));
                    break;
                case 3:
                    addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, BLOCK));
                    break;
            }
        }
    }

    public void atTurnStart() {
        this.counter = 0;
    }

    @Override
    public void onVictory() {
        this.counter = -1;
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + STRENGTH + DESCRIPTIONS[1] + VULNERABLE + DESCRIPTIONS[2] + BLOCK + DESCRIPTIONS[3];
    }

}
