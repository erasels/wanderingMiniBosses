package wanderingMiniBosses.monsters.thiefOfABillion;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.AnimateHopAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.ThieveryPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.GainGoldTextEffect;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;

import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.blights.FullOfOpenings;
import wanderingMiniBosses.blights.NotAskedDonationsToTheBloodFund;
import wanderingMiniBosses.blights.TooCautious;
import wanderingMiniBosses.monsters.AbstractWanderingBoss;
import wanderingMiniBosses.relics.MasterThiefsPresence;
import wanderingMiniBosses.util.MiscFunctions;
import wanderingMiniBosses.vfx.general.ColorSmokeBombEffect;

public class ThiefOfABillionGuards extends AbstractWanderingBoss {

	public static final String ID = WanderingminibossesMod.makeID("ThiefOfABillionGuards");
	private static final MonsterStrings monsterstrings =
			CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static String NAME = monsterstrings.NAME;
    public static final String[] DIALOG = monsterstrings.DIALOG;
    public static final String[] MOVES = monsterstrings.MOVES;
    private static final float HB_W = 100.0F;
    private static final float HB_H = 100.0F;

    private static int MAX_HEALTH_A0_UPPER_BOUND = 235;
    private static int MAX_HEALTH_A9_UPPER_BOUND = 239;
    
    //private static final int MAXIMUM_AMOUNT_OF_GOLD_TO_START_GIVING = 49;
    private static final int AMOUNT_OF_GOLD_TO_GIVE = 150;
    
    private static final int AMOUNT_OF_GOLD_TO_STEAL_PER_ATTACK_ACT_NOT_3 = 20;
    private static final int AMOUNT_OF_GOLD_TO_STEAL_PER_ATTACK_ACT_3 = 15;
    
    private static final int STOLEN_WHIRLWIND_AMOUNT_OF_HITS = 6;
    
    private static final byte ACT_1_GIVE_GOLD = 0;
    //private static final String ACT_1_GIVE_GOLD_NAME = MOVES[0];
    
    private static final byte ACT_1_STEAL_GOLD = 100;
    private static final String ACT_1_STEAL_GOLD_NAME = MOVES[1];
    private static final byte ACT_1_BOMB = 1;
    private static final String ACT_1_BOMB_NAME = MOVES[2];
    private static final byte ACT_1_PRANK_THEFT = 2;
    private static final String ACT_1_PRANK_THEFT_NAME = MOVES[3];
    
    private static final byte ACT_2_GIVE_GOLD = 3;
    //private static final String ACT_2_GIVE_GOLD_NAME = MOVES[0];
    private static final byte ACT_2_STEAL_GOLD = 4;
    private static final String ACT_2_STEAL_GOLD_NAME = MOVES[7];
    private static final byte ACT_2_FRAIL_BOMB = 5;
    private static final String ACT_2_FRAIL_BOMB_NAME = MOVES[4];
    private static final byte ACT_2_PRANK_THEFT = 6;
    private static final String ACT_2_PRANK_THEFT_NAME = MOVES[3];
    
    private static final byte ACT_3_WEAK_BOMB = 7;
    private static final String ACT_3_WEAK_BOMB_NAME = MOVES[5];
    private static final byte ACT_3_STEAL_GOLD = 8;
    private static final String ACT_3_STEAL_GOLD_NAME = MOVES[1];
    private static final byte ACT_3_STOLEN_WHIRLWIND = 9;
    private static final String ACT_3_STOLEN_WHIRLWIND_NAME = MOVES[6];
    
    private static final Color THIEF_TINT_COLOR = Color.GREEN;
    
    private static boolean gave_money = false;

    private int turnCounter = 0;
    
    public ThiefOfABillionGuards() {
    	this(DIALOG[0],
        		ID,
        		defineMaxHealth(AbstractDungeon.ascensionLevel));
    }
    
    public ThiefOfABillionGuards(String name, String ID, int maxHealth) {
        super(name, ID, maxHealth, 0, 0, HB_W, HB_H, "", -400F, 400F);
        rewards.add(new RewardItem(new MasterThiefsPresence()));
        loadAnimation("images/monsters/theBottom/looter/skeleton.atlas",
        		"images/monsters/theBottom/looter/skeleton.json", 2.0F);
        
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        
        e.setTime(e.getEndTime() * MathUtils.random());

        this.tint.color = THIEF_TINT_COLOR;
    }
    
    @Override
    public void die() {
    	super.die();
		CardCrawlGame.sound.playA("VO_MUGGER_2A", 0.4F);
		CardCrawlGame.sound.playA("VO_MUGGER_2B", 0.4F);
    	RemoveThisThiefBlights();
    }
    
    private void RemoveThisThiefBlights() {
    	for (int i = AbstractDungeon.player.blights.size() - 1; i >= 0; i--) {
    		AbstractBlight blight = AbstractDungeon.player.blights.get(i);
    		if ((blight.blightID.equals(FullOfOpenings.ID)) ||
    			(blight.blightID.equals(NotAskedDonationsToTheBloodFund.ID)) ||
    			(blight.blightID.equals(TooCautious.ID))) {
    			AbstractDungeon.player.blights.remove(i);
    		}
    	}
    }
    
    private static int defineMaxHealth(int ascensionLevel) {
    	if (ascensionLevel < 9)	return MAX_HEALTH_A0_UPPER_BOUND;
    	else return MAX_HEALTH_A9_UPPER_BOUND;
    }
    
    public void usePreBattleAction() {
        super.usePreBattleAction();
        this.tint.changeColor(THIEF_TINT_COLOR);
        gave_money = false;
        receiveAscensionBuffs();
        setFirstMoveShortcut();
        if (AbstractDungeon.actNum < 3) {
        	AbstractDungeon.actionManager.addToBottom(
                	new ApplyPowerAction(this, this,
                			new ThieveryPower(this,
                					AMOUNT_OF_GOLD_TO_STEAL_PER_ATTACK_ACT_NOT_3))
                	);
        } else {
        	AbstractDungeon.actionManager.addToBottom(
                	new ApplyPowerAction(this, this,
                			new ThieveryPower(this,
                					AMOUNT_OF_GOLD_TO_STEAL_PER_ATTACK_ACT_3))
                	);
        }

        CardCrawlGame.sound.playA("VO_LOOTER_1A", 0.2F);
        CardCrawlGame.sound.playA("VO_LOOTER_1B", 0.5F);
        CardCrawlGame.sound.playA("VO_LOOTER_1C", 0.0F);
    }
    
    private void receiveAscensionBuffs() {
    	int amount_of_mettalicize = 0;
    	if (AbstractDungeon.ascensionLevel >= 9) {
    		switch(AbstractDungeon.actNum) {
    			case 1:
    				amount_of_mettalicize = 2;
    				break;
    			case 2:
    				amount_of_mettalicize = 3;
    				break;
    			case 3:
    				amount_of_mettalicize = 4;
    				break;
    			default:
    				if (AbstractDungeon.actNum > 3)	amount_of_mettalicize = 5;
    				break;
    		}
    		if (amount_of_mettalicize > 0) {
    			AbstractDungeon.actionManager.addToBottom(
    		        new ApplyPowerAction(this, this,
    		        	new MetallicizePower(this, amount_of_mettalicize))
    		    );
    		}
    	}
    	
    }
    
    private void setFirstMoveShortcut() {
    	
    	switch (AbstractDungeon.actNum) {
    		case 1:
    	    	setMoveShortcut(ACT_1_STEAL_GOLD, ACT_1_STEAL_GOLD_NAME);
    	    	return;
    		case 2:
    	    	setMoveShortcut(ACT_2_STEAL_GOLD, ACT_2_STEAL_GOLD_NAME);
    			return;
    		case 3:
    	    	if (AbstractDungeon.player.gold < 99){
    	    		setMoveShortcut(ACT_3_WEAK_BOMB, ACT_3_WEAK_BOMB_NAME);
        	    	return;
    	    	} else {
    	    		setMoveShortcut(ACT_3_WEAK_BOMB, ACT_3_WEAK_BOMB_NAME);
        	    	return;
    	    	}
    	    default:
    	    	setMoveShortcut(ACT_3_WEAK_BOMB, ACT_3_WEAK_BOMB_NAME);
    	    	return;
    	}
    }
    
    @Override
    protected void populateMoves() {
    	
    	if (AbstractDungeon.ascensionLevel < 19) {
    		if (AbstractDungeon.actNum == 1) {
        		
    	        this.moves.put(ACT_1_GIVE_GOLD, new EnemyMoveInfo(ACT_1_GIVE_GOLD, Intent.MAGIC, -1, 0, false));
    	        this.moves.put(ACT_1_STEAL_GOLD, new EnemyMoveInfo(ACT_1_STEAL_GOLD, Intent.ATTACK, 0, 1, false));
    	        this.moves.put(ACT_1_BOMB, new EnemyMoveInfo(ACT_1_BOMB, Intent.DEBUFF, 0, 1, false));
    	        this.moves.put(ACT_1_PRANK_THEFT, new EnemyMoveInfo(ACT_1_PRANK_THEFT, Intent.DEBUFF, 0, 1, false));
        
        	} else if (AbstractDungeon.actNum == 2) {
           
        		this.moves.put(ACT_2_GIVE_GOLD, new EnemyMoveInfo(ACT_2_GIVE_GOLD, Intent.MAGIC, -1, 0, false));
    	        this.moves.put(ACT_2_STEAL_GOLD, new EnemyMoveInfo(ACT_2_STEAL_GOLD, Intent.ATTACK, 0, 3, true));
    	        this.moves.put(ACT_2_FRAIL_BOMB, new EnemyMoveInfo(ACT_2_FRAIL_BOMB, Intent.DEBUFF, 0, 1, false));
    	        this.moves.put(ACT_2_PRANK_THEFT, new EnemyMoveInfo(ACT_1_PRANK_THEFT, Intent.DEBUFF, 0, 1, false));
            
        	} else if (AbstractDungeon.actNum >= 3){
        		
        		this.moves.put(ACT_3_WEAK_BOMB, new EnemyMoveInfo(ACT_3_WEAK_BOMB, Intent.DEBUFF, 0, 1, false));
                this.moves.put(ACT_3_STEAL_GOLD, new EnemyMoveInfo(ACT_3_STEAL_GOLD, Intent.ATTACK, 0, 1, false));
                this.moves.put(ACT_3_STOLEN_WHIRLWIND, new EnemyMoveInfo(ACT_3_STOLEN_WHIRLWIND, Intent.ATTACK, 1, 6, true));
        		
        	}
    	} else {
			if (AbstractDungeon.actNum == 1) {
        		
    	        this.moves.put(ACT_1_STEAL_GOLD, new EnemyMoveInfo(ACT_1_STEAL_GOLD, Intent.ATTACK, 6, 1, false));
    	        this.moves.put(ACT_1_BOMB, new EnemyMoveInfo(ACT_1_BOMB, Intent.DEBUFF, 0, 1, false));
    	        this.moves.put(ACT_1_PRANK_THEFT, new EnemyMoveInfo(ACT_1_PRANK_THEFT, Intent.DEBUFF, 0, 1, false));
        
        	} else if (AbstractDungeon.actNum == 2) {
           
        		this.moves.put(ACT_2_GIVE_GOLD, new EnemyMoveInfo(ACT_2_GIVE_GOLD, Intent.MAGIC, -1, 0, false));
    	        this.moves.put(ACT_2_STEAL_GOLD, new EnemyMoveInfo(ACT_2_STEAL_GOLD, Intent.ATTACK, 3, 3, true));
    	        this.moves.put(ACT_2_FRAIL_BOMB, new EnemyMoveInfo(ACT_2_FRAIL_BOMB, Intent.DEBUFF, 0, 1, false));
    	        this.moves.put(ACT_2_PRANK_THEFT, new EnemyMoveInfo(ACT_1_PRANK_THEFT, Intent.DEBUFF, 0, 1, false));
            
        	} else if (AbstractDungeon.actNum >= 3) {
        		
        		this.moves.put(ACT_3_WEAK_BOMB, new EnemyMoveInfo(ACT_3_WEAK_BOMB, Intent.DEBUFF, 0, 1, false));
                this.moves.put(ACT_3_STEAL_GOLD, new EnemyMoveInfo(ACT_3_STEAL_GOLD, Intent.ATTACK, 3, 1, true));
                this.moves.put(ACT_3_STOLEN_WHIRLWIND, new EnemyMoveInfo(ACT_3_STOLEN_WHIRLWIND, Intent.ATTACK, 3, 6, true));
        		
        	}
    	}
    }

    public void takeCustomTurn(DamageInfo info, int amulti) {
        switch (this.nextMove) {
            case ACT_1_GIVE_GOLD:
            	gave_money = true;
            	AbstractDungeon.actionManager.addToBottom(new TalkAction(this, "...here, you will need this.", 0.5F, 2.0F));
            	AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
                AbstractDungeon.player.gainGold(AMOUNT_OF_GOLD_TO_GIVE);
                AbstractDungeon.effectList.add(new RainingGoldEffect(AMOUNT_OF_GOLD_TO_GIVE));
                AbstractDungeon.effectList.add(new GainGoldTextEffect(AMOUNT_OF_GOLD_TO_GIVE));
                break;
            case ACT_1_STEAL_GOLD:
            	playStealSfx();
            	AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
            	AbstractDungeon.actionManager.addToBottom(
            			new DamageAction(AbstractDungeon.player, 
            					info, AMOUNT_OF_GOLD_TO_STEAL_PER_ATTACK_ACT_NOT_3));
            	break;
            case ACT_1_PRANK_THEFT:
            	AbstractDungeon.actionManager.addToBottom(new AnimateHopAction(this));
            	doPrankTheft(AbstractDungeon.actNum);
            	break;
            case ACT_1_BOMB:
            	AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
            	AbstractDungeon.actionManager.addToBottom(
            			new VFXAction(
            				new ColorSmokeBombEffect(AbstractDungeon.player.hb.cX,
            						AbstractDungeon.player.hb.cY, Color.ROYAL)));
            	break;
            case ACT_2_STEAL_GOLD:
            	playStealSfx();
            	AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
            	for (int i = 0; i < 3; i++) {
            		addToBot(new WaitAction(0.3f));
            		addToBot(new DamageAction(AbstractDungeon.player, info, 
                		AMOUNT_OF_GOLD_TO_STEAL_PER_ATTACK_ACT_NOT_3));
            	}
            	break;
            case ACT_2_FRAIL_BOMB:
            	AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
            	AbstractDungeon.actionManager.addToBottom(
            			new VFXAction(
                				new ColorSmokeBombEffect(AbstractDungeon.player.hb.cX,
                						AbstractDungeon.player.hb.cY, Color.BLUE)));
            	AbstractDungeon.actionManager.addToBottom(
            			new ApplyPowerAction(AbstractDungeon.player,
            					this,
            					new FrailPower(AbstractDungeon.player, 2, true)));
            	break;
            case ACT_2_PRANK_THEFT:
            	doPrankTheft(AbstractDungeon.actNum);
            	break;
            case ACT_3_WEAK_BOMB:
            	AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
            	AbstractDungeon.actionManager.addToBottom(
            			new VFXAction(
            					new ColorSmokeBombEffect(AbstractDungeon.player.hb.cX,
                						AbstractDungeon.player.hb.cY, Color.LIME)));
            	AbstractDungeon.actionManager.addToBottom(
            			new ApplyPowerAction(AbstractDungeon.player,
            					this,
            					new WeakPower(AbstractDungeon.player, 2, true)));
            	break;
            case ACT_3_STEAL_GOLD:
            	playStealSfx();
            	AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
            	AbstractDungeon.actionManager.addToBottom(
            			new DamageAction(AbstractDungeon.player, 
            					info, AMOUNT_OF_GOLD_TO_STEAL_PER_ATTACK_ACT_3));
            	break;
            case ACT_3_STOLEN_WHIRLWIND:
            	AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
            	AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_WHIRLWIND"));
            	for (int i = 0; i < STOLEN_WHIRLWIND_AMOUNT_OF_HITS; i++) {
            		addToBot(new SFXAction("ATTACK_HEAVY"));
            		
            		DamageAction dmg_action = new DamageAction(AbstractDungeon.player,
            				info, AMOUNT_OF_GOLD_TO_STEAL_PER_ATTACK_ACT_3);
            		dmg_action.attackEffect = AttackEffect.NONE;
            	      
            		addToBot(new VFXAction(this, new CleaveEffect(true), 0.15F));
            		addToBot(dmg_action);
            	} 
            	break;
            default:
            	break;
        }

        ++turnCounter;
    }
    
    private void playStealSfx() {
        AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_LOOTER_1A", 0f));
        AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_LOOTER_1B", 0f));
        AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_LOOTER_1C", 0f));
    }
    
    private void doPrankTheft(int actNum) {
    	
    	switch (actNum) {
    		case 1:
    			if (MiscFunctions.headsOrTails(AbstractDungeon.relicRng) == 1) giveFullOfOpenings();
    			else giveTooCautious();
    			break;
    		case 2:
    			if (MiscFunctions.headsOrTails(AbstractDungeon.relicRng) == 1) giveTooCautious();
    			else {
    				if (!AbstractDungeon.player.hasBlight(NotAskedDonationsToTheBloodFund.ID)) {
        				giveNotAskedDonationsToTheBloodFund();
        			} else {
        				AbstractDungeon.actionManager.addToBottom(
                    			new VFXAction(
                    				new SmokeBombEffect(AbstractDungeon.player.hb.cX,
                    						AbstractDungeon.player.hb.cY)));
                    	AbstractDungeon.actionManager.addToBottom(
                    			new ApplyPowerAction(AbstractDungeon.player,
                    				this,
                    				new FrailPower(AbstractDungeon.player, 2, true)));
        			}
    			}
    			break;
    		case 3:
    			giveFullOfOpenings();
    			break;
    		default:
    			giveFullOfOpenings();
    			break;
    	}
    }



    private void giveFullOfOpenings() {
        if (AbstractDungeon.player.hasBlight(FullOfOpenings.ID)) {
        	AbstractDungeon.player.getBlight(FullOfOpenings.ID).stack();
        } else {
        	AbstractDungeon.currMapNode.room.spawnBlightAndObtain(
					AbstractDungeon.player.drawX,
					AbstractDungeon.player.drawY,
           			new FullOfOpenings());
        }
    }
    
    private void giveTooCautious() {
        if (AbstractDungeon.player.hasBlight(TooCautious.ID)) {
        	AbstractDungeon.player.getBlight(TooCautious.ID).stack();
        } else {
        	AbstractDungeon.currMapNode.room.spawnBlightAndObtain(
					AbstractDungeon.player.drawX,
					AbstractDungeon.player.drawY,
           			new TooCautious());
        }
    }

    private void giveNotAskedDonationsToTheBloodFund() {
        AbstractDungeon.currMapNode.room.spawnBlightAndObtain(
				AbstractDungeon.player.drawX,
				AbstractDungeon.player.drawY,
           		new NotAskedDonationsToTheBloodFund());
    }
    
    protected void getMove(int num) {
    	if (AbstractDungeon.actNum == 1) {
    		if (turnCounter == 0) {
                setFirstMoveShortcut();
            } else if ((turnCounter == 1) && (!gave_money)) {
                setMoveShortcut(ACT_1_BOMB, ACT_1_BOMB_NAME);
            } else if ((turnCounter == 1) && (gave_money)) {
                setMoveShortcut((byte)-128);
            } else {
                setMoveShortcut(ACT_1_PRANK_THEFT, ACT_1_PRANK_THEFT_NAME);
            }
    	}
    	
    	else if (AbstractDungeon.actNum == 2) {
    		if (turnCounter == 0) {
                setFirstMoveShortcut();
            } else if ((turnCounter == 1) && (gave_money)) {
                setMoveShortcut((byte)-128);
            } else if ((turnCounter == 1)) {
            	setMoveShortcut(ACT_2_FRAIL_BOMB, ACT_2_FRAIL_BOMB_NAME);
            } else {
                setMoveShortcut(ACT_2_PRANK_THEFT, ACT_2_PRANK_THEFT_NAME);
            }
    	}
    	else {
    		if (turnCounter == 0) {
                setMoveShortcut(ACT_3_WEAK_BOMB, ACT_3_WEAK_BOMB_NAME);
            } else if (turnCounter == 1) {
                setMoveShortcut(ACT_3_STEAL_GOLD, ACT_3_STEAL_GOLD_NAME);
            } else {
                setMoveShortcut(ACT_3_STOLEN_WHIRLWIND,
                		ACT_3_STOLEN_WHIRLWIND_NAME);
            }
    	}
    	
    }
    
    @Override
    public void onEscape() {
    	
    	throwRunSmokeBombs();
        super.onEscape();
    	
    }

    private void throwRunSmokeBombs() {
    	AbstractDungeon.actionManager.addToBottom(
    			new VFXAction(
    				new ColorSmokeBombEffect(this.hb.cX, this.hb.cY, Color.LIME)));
    	AbstractDungeon.actionManager.addToBottom(
    			new VFXAction(
    				new ColorSmokeBombEffect(this.hb.cX + Settings.WIDTH/5f + Settings.WIDTH/15f,
    						this.hb.cY, Color.LIME)));
    	AbstractDungeon.actionManager.addToBottom(
    			new VFXAction(
    				new ColorSmokeBombEffect(this.hb.cX - Settings.WIDTH/5f,
    						this.hb.cY, Color.LIME)));
    	AbstractDungeon.actionManager.addToBottom(
    			new VFXAction(
    				new ColorSmokeBombEffect(this.hb.cX + Settings.WIDTH/5f,
    						this.hb.cY, Color.LIME)));
    }

}
