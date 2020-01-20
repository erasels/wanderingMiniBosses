package wanderingMiniBosses.monsters.thiefOfABillion;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.PummelDamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.ThieveryPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.GainGoldTextEffect;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;

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
    
    private static final int MAXIMUM_AMOUNT_OF_GOLD_TO_START_GIVING = 49;
    private static final int AMOUNT_OF_GOLD_TO_GIVE = 150;
    
    private static final int AMOUNT_OF_GOLD_TO_STEAL_PER_ATTACK = 25;
    
    private static final byte ACT_1_GIVE_GOLD = 0;
    private static final byte ACT_1_STEAL_GOLD = 100;
    private static final byte ACT_3_STEAL_GOLD = 101;
    private static final byte ACT_1_VULNERABLE_BOMB = 1;
    private static final byte ACT_1_PRANK_THEFT = 2;
    
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
    	long seed = Settings.seed;
    	if (ascensionLevel < 9) {
    		extra_amount = MAX_HEALTH_A0_UPPER_BOUND - MAX_HEALTH_A0_LOWER_BOUND;
    		return MAX_HEALTH_A0_LOWER_BOUND + (int)(seed % (extra_amount + 1));
    	}
    	else {
    		extra_amount = MAX_HEALTH_A9_UPPER_BOUND - MAX_HEALTH_A9_LOWER_BOUND;
    		return MAX_HEALTH_A9_LOWER_BOUND + (int)(seed % (extra_amount + 1));
    	}
    }
    
    public void usePreBattleAction() {
        super.usePreBattleAction();
        throwEntranceSmokeBombs();
        setMoveShortcut(defineFirstMove());
        AbstractDungeon.actionManager.addToBottom(
        	new ApplyPowerAction(this, this, new ThieveryPower(this, AMOUNT_OF_GOLD_TO_STEAL_PER_ATTACK))
        	);

        CardCrawlGame.sound.playA("VO_LOOTER_1A", 2.0F);
    }
    
    private byte defineFirstMove() {
    	
    	byte first_move = 0;
    	
    	if (AbstractDungeon.player.gold < 49){
    		if (AbstractDungeon.actNum < 3)	first_move = ACT_1_GIVE_GOLD;
    		else {
	    		int amount_of_keys = (Settings.hasEmeraldKey ? 1 : 0) +
	    				(Settings.hasRubyKey ? 1 : 0) +
	    				(Settings.hasSapphireKey ? 1 : 0);
	    		
	    		if (amount_of_keys >= 2) first_move = ACT_1_GIVE_GOLD;
    		}
    	} else {
    		first_move = ACT_1_STEAL_GOLD;
    	}
    	return first_move;
    }
    
    private void throwEntranceSmokeBombs() {
    	AbstractDungeon.actionManager.addToBottom(
    			new VFXAction(
    				new SmokeBombEffect(this.hb.cX, this.hb.cY)));
    	AbstractDungeon.actionManager.addToBottom(
    			new VFXAction(
    				new SmokeBombEffect(this.hb.cX - Settings.WIDTH/5,
    						this.hb.cY)));
    	AbstractDungeon.actionManager.addToBottom(
    			new VFXAction(
    				new SmokeBombEffect(this.hb.cX + Settings.WIDTH/5,
    						this.hb.cY)));
    }
    
    @Override
    protected void populateMoves() {
        this.moves.put(ACT_1_GIVE_GOLD, new EnemyMoveInfo(ACT_1_GIVE_GOLD, Intent.MAGIC, -1, 0, false));
        this.moves.put(ACT_1_STEAL_GOLD, new EnemyMoveInfo((ACT_1_STEAL_GOLD, Intent.ATTACK, 6, 1, false));
        this.moves.put(ACT_1_VULNERABLE_BOMB, new EnemyMoveInfo(ACT_1_VULNERABLE_BOMB, Intent.DEBUFF, 0, 1, false));
        this.moves.put(ACT_1_PRANK_THEFT, new EnemyMoveInfo(ACT_1_PRANK_THEFT, Intent.DEBUFF, 0, 1, false));
    }

    public void takeCustomTurn(DamageInfo info, int amulti) {
        switch (this.nextMove) {
            case ACT_1_GIVE_GOLD:
            	AbstractDungeon.actionManager.addToBottom(new TalkAction(this, "...here, you will need this.", 0.5F, 2.0F));
                AbstractDungeon.player.gainGold(AMOUNT_OF_GOLD_TO_GIVE);
                AbstractDungeon.effectList.add(new RainingGoldEffect(AMOUNT_OF_GOLD_TO_GIVE));
                AbstractDungeon.effectList.add(new GainGoldTextEffect(AMOUNT_OF_GOLD_TO_GIVE));
                break;
            case ACT_1_STEAL_GOLD:
            	playStealSfx();
            	AbstractDungeon.actionManager.addToBottom(
            			new DamageAction(AbstractDungeon.player, 
            					info, AMOUNT_OF_GOLD_TO_STEAL_PER_ATTACK));
            	break;
            case ACT_3_STEAL_GOLD:
            	break;
            case ACT_1_VULNERABLE_BOMB:
            	AbstractDungeon.actionManager.addToBottom(
            			new VFXAction(
            				new SmokeBombEffect(AbstractDungeon.player.hb.cX,
            						AbstractDungeon.player.hb.cY)));
            	AbstractDungeon.actionManager.addToBottom(
            			new ApplyPowerAction(AbstractDungeon.player,
            					this,
            					new VulnerablePower(AbstractDungeon.player, 2, true)));
            	break;
            case ACT_1_PRANK_THEFT:
            	doPrankTheft();
            	break;
        }

        ++turnCounter;
    }
    
    private void doPrankTheft() {
    	
    }

    private void playStealSfx() {
        AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_LOOTER_1A", 0.2f));
        AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_LOOTER_1B", 0.2f));
        AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_LOOTER_1C", 0.2f));
    }
    
    protected void getMove(int num) {
    	
    	if (AbstractDungeon.actNum == 1) {
    		if (turnCounter == 1) {
                setMoveShortcut(ACT_1_VULNERABLE_BOMB);
            } else {
                setMoveShortcut(ACT_1_PRANK_THEFT);
            }
    	}
    	
    }
    
    

}
