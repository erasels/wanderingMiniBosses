package wanderingMiniBosses.monsters.thiefOfABillion;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rewards.RewardItem;

import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.monsters.AbstractWanderingBoss;
import wanderingMiniBosses.powers.InkPower;
import wanderingMiniBosses.relics.Inkheart;
import wanderingMiniBosses.vfx.general.SpookyStuff;

public class ThiefOfABillionGuards extends AbstractWanderingBoss {

	public static final String ID = WanderingminibossesMod.makeID("ThiefOfABillionGuards");
	private static final MonsterStrings monsterstrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static String NAME = monsterstrings.NAME;
    public static final String[] DIALOG = monsterstrings.DIALOG;
    private static final float HB_W = 375.0F;
    private static final float HB_H = 300.0F;
    
    private static int MAX_HEALTH_A0_LOWER_BOUND = 289;
    private static int MAX_HEALTH_A0_UPPER_BOUND = 295;
    private static int MAX_HEALTH_A9_LOWER_BOUND = 309;
    private static int MAX_HEALTH_A9_UPPER_BOUND = 319;
    
    private static final byte ACT_1_GIVE_GOLD = 0;
    private static final byte ACT_1_STEAL_GOLD = 0;
    private static final byte ACT_1_VULNERABLE_BOMB = 1;
    private static final byte ACT_1_PRANK_THEFT = 2;
    
    private static final byte SQUICK = 0;
    private static final byte BUFFTIME = 1;
    private static final byte VOIDBRUH = 2;

    private int turnCounter = 0;
    
    public ThiefOfABillionGuards() {
        this(defineThiefName(AbstractDungeon.ascensionLevel),
        		ID,
        		defineMaxHealth(AbstractDungeon.ascensionLevel));
    }
    
    public ThiefOfABillionGuards(String name, String ID, int maxHealth) {
        super(name, ID, maxHealth, 0, 0, HB_W, HB_H, WanderingminibossesMod.makeMonsterPath("InkMan.png"), -100F, 300F);
    }
    
    private static String defineThiefName(int ascensionLevel) {
    	if (ascensionLevel < 0) return DIALOG[0];
    	else if (ascensionLevel < 9) return DIALOG[1];
    	else if (ascensionLevel < 13) return DIALOG[2];
    	else if (ascensionLevel < 16) return DIALOG[3];
    	else if (ascensionLevel < 19) return DIALOG[4];
    	else if (ascensionLevel < 21) return DIALOG[5];
    	else return DIALOG[6];
    }
    
    private static int defineMaxHealth(int ascensionLevel) {
    	int extra_amount;
    	if (ascensionLevel < 9) {
    		extra_amount = MAX_HEALTH_A0_UPPER_BOUND - MAX_HEALTH_A0_LOWER_BOUND;
    		return MAX_HEALTH_A0_LOWER_BOUND + AbstractDungeon.monsterHpRng.random(extra_amount);
    	}
    	else {
    		extra_amount = MAX_HEALTH_A9_UPPER_BOUND - MAX_HEALTH_A9_LOWER_BOUND;
    		return MAX_HEALTH_A9_LOWER_BOUND + AbstractDungeon.monsterHpRng.random(extra_amount);
    	}
    }
    
    public void usePreBattleAction() {
        super.usePreBattleAction();
        setMoveShortcut(SQUICK);
        for (int i = 0; i < 33; i++) {
            AbstractDungeon.effectList.add(new SpookyStuff(this.hb.cX, this.hb.cY));
        }
        CardCrawlGame.sound.playA("NECRONOMICON", 0.9F);
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, "HELLO", 0.5F, 2.0F));
    }

    @Override
    protected void populateMoves() {
        this.moves.put(SQUICK, new EnemyMoveInfo(SQUICK, Intent.STRONG_DEBUFF, -1, 0, false));
        this.moves.put(BUFFTIME, new EnemyMoveInfo(BUFFTIME, Intent.ATTACK_BUFF, 5, 1, false));
        this.moves.put(VOIDBRUH, new EnemyMoveInfo(VOIDBRUH, Intent.ATTACK_DEBUFF, 5, 1, false));
    }

    public void takeCustomTurn(DamageInfo info, int amulti) {
        switch (this.nextMove) {
            case 0:
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, "blurb... blurb", 0.5F, 2.0F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new InkPower(AbstractDungeon.player, this, AbstractDungeon.actNum), AbstractDungeon.actNum));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, "SPLAT!", 0.5F, 2.0F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AttackEffect.BLUNT_LIGHT));
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, 2), 2));
                }
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, "glorp... glurp", 0.5F, 2.0F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new VoidCard(), 1, true, false));
        }

        ++turnCounter;
    }

    protected void getMove(int num) {
        if (turnCounter == 0) {
            setMoveShortcut(SQUICK);
        } else if (turnCounter == 1) {
            setMoveShortcut(BUFFTIME);
        } else {
            setMoveShortcut(VOIDBRUH);
        }
    }

}
