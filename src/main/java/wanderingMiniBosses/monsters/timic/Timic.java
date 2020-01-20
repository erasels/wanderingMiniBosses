package wanderingMiniBosses.monsters.timic;

import com.megacrit.cardcrawl.actions.animations.AnimateHopAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.rewards.RewardItem;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.monsters.AbstractWanderingBoss;
import wanderingMiniBosses.powers.FilledWithCoinsPower;
import wanderingMiniBosses.powers.InterruptMePower;
import wanderingMiniBosses.relics.LockLocket;

public class Timic extends AbstractWanderingBoss {
    public static final String ID = WanderingminibossesMod.makeID("Timic");
    private static final MonsterStrings monsterstrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterstrings.NAME;
    public static final String[] DIALOG = monsterstrings.DIALOG;
    private static final float HB_W = 265.0F;
    private static final float HB_H = 135.0F;

    private static int MAX_HEALTH = 432;

    private static final byte DISTACTION = 0;
    private static final byte FLEEATTEMPTONE = 1;
    private static final byte FLEEATTEMPTTWO = 2;

    private int turnCounter = 0;

    private int bruh1;

    public Timic() {
        this(NAME, ID, MAX_HEALTH);
    }

    public Timic(String name, String ID, int maxHealth) {
        super(name, ID, maxHealth, 0, 0, HB_W, HB_H, WanderingminibossesMod.makeMonsterPath("Timic.png"), -444F, 0F);
        rewards.add(new RewardItem(RelicLibrary.getRelic(LockLocket.ID).makeCopy()));
    }

    public void usePreBattleAction() {
        super.usePreBattleAction();
        setMoveShortcut(DISTACTION);
        if (AbstractDungeon.actNum == 1) {
            bruh1 = 15;
        } else if (AbstractDungeon.actNum == 2) {
            bruh1 = 25;
        } else if (AbstractDungeon.actNum == 3) {
            bruh1 = 33;
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FilledWithCoinsPower(this), 1));
    }

    @Override
    protected void populateMoves() {
        this.moves.put(DISTACTION, new EnemyMoveInfo(DISTACTION, Intent.STRONG_DEBUFF, -1, 0, false));
        this.moves.put(FLEEATTEMPTONE, new EnemyMoveInfo(FLEEATTEMPTONE, Intent.ESCAPE, -1, 0, false));
        this.moves.put(FLEEATTEMPTTWO, new EnemyMoveInfo(FLEEATTEMPTTWO, Intent.ESCAPE, -1, 0, false));
    }

    public void takeCustomTurn(DamageInfo info, int amulti) {
        switch (this.nextMove) {
            case 0:
                addToBot(new AnimateHopAction(this));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 2, true), 2));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 2, true), 2));
                addToBot(new ApplyPowerAction(this, this, new InterruptMePower(this, bruh1), bruh1));
                break;
            case 1:
                onEscape();
                break;
            case 2:
                onEscape();
                break;
        }
        ++turnCounter;
    }

    protected void getMove(int num) {
        if (turnCounter == 0) {
            setMoveShortcut(DISTACTION);
        } else if (turnCounter == 1) {
            setMoveShortcut(FLEEATTEMPTONE);
        } else {
            setMoveShortcut(FLEEATTEMPTTWO);
        }
    }
}
