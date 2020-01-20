package wanderingMiniBosses.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.OnPlayerDeathRelic;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.MarkOfTheBloom;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.util.TextureLoader;
import wanderingMiniBosses.vfx.general.CoinEffect;
import wanderingMiniBosses.vfx.general.LoseGoldTextEffect;

import static wanderingMiniBosses.WanderingminibossesMod.makeRelicOutlinePath;
import static wanderingMiniBosses.WanderingminibossesMod.makeRelicPath;

public class LockLocket extends CustomRelic implements OnPlayerDeathRelic {

    public static final String ID = WanderingminibossesMod.makeID("LockLocket");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("LockLocket.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("LockLocket.png"));

    public LockLocket() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public boolean onPlayerDeath(AbstractPlayer abstractPlayer, DamageInfo damageInfo) {
        if (AbstractDungeon.player.gold > 0 && !AbstractDungeon.player.hasRelic(MarkOfTheBloom.ID)) {
            flash();
            AbstractDungeon.effectList.add(new LoseGoldTextEffect(Math.min(AbstractDungeon.player.gold, 100)));
            for (int i = 0; i < Math.min(AbstractDungeon.player.gold, 100); i++)
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new CoinEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY)));
            AbstractDungeon.player.gold -= Math.min(AbstractDungeon.player.gold, 100);
            AbstractDungeon.player.heal(1, true);
            return false;
        }
        return true;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
