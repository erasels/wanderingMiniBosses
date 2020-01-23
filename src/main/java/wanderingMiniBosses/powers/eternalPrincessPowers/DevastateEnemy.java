package wanderingMiniBosses.powers.eternalPrincessPowers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.util.TextureLoader;

import java.util.ArrayList;

import static wanderingMiniBosses.WanderingminibossesMod.makePowerPath;


public class DevastateEnemy extends AbstractPower {

    public static final String POWER_ID = WanderingminibossesMod.makeID("DevastateEnemy");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Devastation84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Devastation32.png"));

    public DevastateEnemy(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atEndOfRound() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, ID));
        ArrayList<AbstractMonster> notDeadMonsters = new ArrayList<>();

        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!mo.isDeadOrEscaped() && mo != this.owner) {
                notDeadMonsters.add(mo);
            }
        }

        for (int i = 0; i < notDeadMonsters.size(); i++) {
            AbstractMonster mo = notDeadMonsters.get(i);
            //makes the special effects appear all at once for multiple monsters instead of one-by-one
            AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_COLLECTOR_DEBUFF"));
            if (i == notDeadMonsters.size() - 1) {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new CollectorCurseEffect(mo.hb.cX, mo.hb.cY), 2.0F));
            } else {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new CollectorCurseEffect(mo.hb.cX, mo.hb.cY)));
            }
        }

        for (int i = 0; i < notDeadMonsters.size(); i++) {
            AbstractMonster mo = notDeadMonsters.get(i);
            AbstractDungeon.actionManager.addToBottom(new InstantKillAction(mo));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}
