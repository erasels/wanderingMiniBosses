package wanderingMiniBosses.relics;

import static wanderingMiniBosses.WanderingminibossesMod.makeRelicOutlinePath;
import static wanderingMiniBosses.WanderingminibossesMod.makeRelicPath;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import basemod.abstracts.CustomRelic;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.util.TextureLoader;

public class MasterThiefsPresence extends CustomRelic {

	public static final String ID = WanderingminibossesMod.makeID("MasterThiefsPresence");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("test2-Presence.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("test2-PresenceOutline.png"));

    public MasterThiefsPresence() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
    }
    
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DESCRIPTIONS[1];
    }

    @Override
    public void onEquip() {
        CardCrawlGame.sound.play("GOLD_GAIN");
        AbstractDungeon.player.gainGold(1000);
    }

    @Override
    public void atBattleStart() {
    	super.atBattleStart();
        for (AbstractMonster mo : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            addToBot(new RelicAboveCreatureAction(mo, this));
            addToBot(new ApplyPowerAction(mo, AbstractDungeon.player, new WeakPower(mo, 3, false)));
            addToBot(new ApplyPowerAction(mo, AbstractDungeon.player, new VulnerablePower(mo, 3, false)));
        } 
    }

}
