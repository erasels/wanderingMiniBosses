package wanderingMiniBosses.monsters.thiefOfABillion;

import com.megacrit.cardcrawl.actions.animations.AnimateHopAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
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

public class ThiefOfABillionGuards extends AbstractWanderingBoss {

	public static final String ID = WanderingminibossesMod.makeID("ThiefOfABillionGuards");
	private static final MonsterStrings monsterstrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static String NAME = monsterstrings.NAME;
    public static final String[] DIALOG = monsterstrings.DIALOG;
    private static final float HB_W = 100.0F;
    private static final float HB_H = 100.0F;

    private static int MAX_HEALTH_A0_UPPER_BOUND = 215;
    private static int MAX_HEALTH_A9_UPPER_BOUND = 219;
    
    private static final int MAXIMUM_AMOUNT_OF_GOLD_TO_START_GIVING = 49;
    private static final int AMOUNT_OF_GOLD_TO_GIVE = 150;
    
    private static final int AMOUNT_OF_GOLD_TO_STEAL_PER_ATTACK = 25;
    
    private static final int STOLEN_WHIRLWIND_AMOUNT_OF_HITS = 6;
    
    private static final byte ACT_1_GIVE_GOLD = 0;
    private static final byte ACT_1_STEAL_GOLD = 100;
    private static final byte ACT_1_VULNERABLE_BOMB = 1;
    private static final byte ACT_1_PRANK_THEFT = 2;
    
    private static final byte ACT_2_GIVE_GOLD = 3;
    private static final byte ACT_2_STEAL_GOLD = 4;
    private static final byte ACT_2_FRAIL_BOMB = 5;
    private static final byte ACT_2_PRANK_THEFT = 6;
    
    private static final byte ACT_3_WEAK_BOMB = 7;
    private static final byte ACT_3_STEAL_GOLD = 8;
    private static final byte ACT_3_STOLEN_WHIRLWIND = 9;
    
    private static boolean gave_money = false;

    private int turnCounter = 0;
    
    public ThiefOfABillionGuards() {
        this(defineThiefName(AbstractDungeon.ascensionLevel),
        		ID,
        		defineMaxHealth(AbstractDungeon.ascensionLevel));
    }
    
    public ThiefOfABillionGuards(String name, String ID, int maxHealth) {
        super(name, ID, maxHealth, 0, 0, HB_W, HB_H, "", -100F, 300F);
        rewards.add(new RewardItem(600, true));
        rewards.add(new RewardItem(new MasterThiefsPresence()));
        loadAnimation("images/monsters/theBottom/looter/skeleton.atlas", "images/monsters/theBottom/looter/skeleton.json", 2.0F);
    }
    
    @Override
    public void die() {
    	super.die();
    	for (int i = AbstractDungeon.player.blights.size() - 1; i >= 0; i--) {
    		AbstractBlight blight = AbstractDungeon.player.blights.get(i);
    		if ((blight.blightID.equals(FullOfOpenings.ID)) ||
    			(blight.blightID.equals(NotAskedDonationsToTheBloodFund.ID)) ||
    			(blight.blightID.equals(TooCautious.ID))) {
    			AbstractDungeon.player.blights.remove(i);
    		}
    	}
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
    	if (ascensionLevel < 9) {
			return MAX_HEALTH_A0_UPPER_BOUND;
    	}
    	else {
			return MAX_HEALTH_A9_UPPER_BOUND;
    	}
    }
    
    public void usePreBattleAction() {
        super.usePreBattleAction();
        gave_money = false;
        receiveAscensionBuffs();
        throwEntranceSmokeBombs();
        byte debugging = defineFirstMove();
        MiscFunctions.fastLoggerLine(debugging);
        setMoveShortcut(debugging);
        AbstractDungeon.actionManager.addToBottom(
        	new ApplyPowerAction(this, this, new ThieveryPower(this, AMOUNT_OF_GOLD_TO_STEAL_PER_ATTACK))
        	);

        CardCrawlGame.sound.playA("VO_LOOTER_1A", 0.2F);
        CardCrawlGame.sound.playA("VO_LOOTER_1B", 0.5F);
        CardCrawlGame.sound.playA("VO_LOOTER_1C", 1.0F);
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
    
    private byte defineFirstMove() {
    	
    	byte first_move;
    	
    	MiscFunctions.fastLoggerLine(AbstractDungeon.actNum);
    	switch (AbstractDungeon.actNum) {
    		case 1:
    	    	first_move = ACT_1_STEAL_GOLD;
    	    	return first_move;
    		case 2:
    	    	first_move = ACT_2_STEAL_GOLD;
    	    	return first_move;
    			
    		case 3:
    			first_move = 0;
    	    	if (AbstractDungeon.player.gold < 99){
    		    	/*int amount_of_keys = (Settings.hasEmeraldKey ? 1 : 0) +
    		    				(Settings.hasRubyKey ? 1 : 0) +
    		    				(Settings.hasSapphireKey ? 1 : 0);
    		    		
    		    	if (amount_of_keys >= 2) first_move = ACT_2_GIVE_GOLD;*/
    	    		first_move = ACT_3_WEAK_BOMB;
    	    	} else {
    	    		first_move = ACT_3_WEAK_BOMB;
    	    	}
    	    	return first_move;
    	
    	}
    	
    	return 0;
    	
    }
    
    private void throwEntranceSmokeBombs() {
    	AbstractDungeon.actionManager.addToBottom(
    			new VFXAction(
    				new SmokeBombEffect(this.hb.cX, this.hb.cY)));
    	AbstractDungeon.actionManager.addToBottom(
    			new VFXAction(
    				new SmokeBombEffect(this.hb.cX - Settings.WIDTH/5f,
    						this.hb.cY)));
    	AbstractDungeon.actionManager.addToBottom(
    			new VFXAction(
    				new SmokeBombEffect(this.hb.cX + Settings.WIDTH/5f,
    						this.hb.cY)));
    }
    
    @Override
    protected void populateMoves() {
    	
    	if (AbstractDungeon.ascensionLevel < 19) {
    		if (AbstractDungeon.actNum == 1) {
        		
    	        this.moves.put(ACT_1_GIVE_GOLD, new EnemyMoveInfo(ACT_1_GIVE_GOLD, Intent.MAGIC, -1, 0, false));
    	        this.moves.put(ACT_1_STEAL_GOLD, new EnemyMoveInfo(ACT_1_STEAL_GOLD, Intent.ATTACK, 6, 1, false));
    	        this.moves.put(ACT_1_VULNERABLE_BOMB, new EnemyMoveInfo(ACT_1_VULNERABLE_BOMB, Intent.DEBUFF, 0, 1, false));
    	        this.moves.put(ACT_1_PRANK_THEFT, new EnemyMoveInfo(ACT_1_PRANK_THEFT, Intent.DEBUFF, 0, 1, false));
        
        	} else if (AbstractDungeon.actNum == 2) {
           
        		this.moves.put(ACT_2_GIVE_GOLD, new EnemyMoveInfo(ACT_2_GIVE_GOLD, Intent.MAGIC, -1, 0, false));
    	        this.moves.put(ACT_2_STEAL_GOLD, new EnemyMoveInfo(ACT_2_STEAL_GOLD, Intent.ATTACK, 3, 4, true));
    	        this.moves.put(ACT_2_FRAIL_BOMB, new EnemyMoveInfo(ACT_2_FRAIL_BOMB, Intent.DEBUFF, 0, 1, false));
    	        this.moves.put(ACT_2_PRANK_THEFT, new EnemyMoveInfo(ACT_1_PRANK_THEFT, Intent.DEBUFF, 0, 1, false));
            
        	} else if (AbstractDungeon.actNum >= 3){
        		
        		this.moves.put(ACT_3_WEAK_BOMB, new EnemyMoveInfo(ACT_3_WEAK_BOMB, Intent.DEBUFF, 0, 1, false));
                this.moves.put(ACT_3_STEAL_GOLD, new EnemyMoveInfo(ACT_3_STEAL_GOLD, Intent.ATTACK, 3, 1, false));
                this.moves.put(ACT_3_STOLEN_WHIRLWIND, new EnemyMoveInfo(ACT_3_STOLEN_WHIRLWIND, Intent.ATTACK, 4, 6, true));
        		
        	}
    	} else {
			if (AbstractDungeon.actNum == 1) {
        		
    	        this.moves.put(ACT_1_STEAL_GOLD, new EnemyMoveInfo(ACT_1_STEAL_GOLD, Intent.ATTACK, 6, 1, false));
    	        this.moves.put(ACT_1_VULNERABLE_BOMB, new EnemyMoveInfo(ACT_1_VULNERABLE_BOMB, Intent.DEBUFF, 0, 1, false));
    	        this.moves.put(ACT_1_PRANK_THEFT, new EnemyMoveInfo(ACT_1_PRANK_THEFT, Intent.DEBUFF, 0, 1, false));
        
        	} else if (AbstractDungeon.actNum == 2) {
           
        		this.moves.put(ACT_2_GIVE_GOLD, new EnemyMoveInfo(ACT_2_GIVE_GOLD, Intent.MAGIC, -1, 0, false));
    	        this.moves.put(ACT_2_STEAL_GOLD, new EnemyMoveInfo(ACT_2_STEAL_GOLD, Intent.ATTACK, 3, 4, true));
    	        this.moves.put(ACT_2_FRAIL_BOMB, new EnemyMoveInfo(ACT_2_FRAIL_BOMB, Intent.DEBUFF, 0, 1, false));
    	        this.moves.put(ACT_2_PRANK_THEFT, new EnemyMoveInfo(ACT_1_PRANK_THEFT, Intent.DEBUFF, 0, 1, false));
            
        	} else if (AbstractDungeon.actNum >= 3) {
        		
        		this.moves.put(ACT_3_WEAK_BOMB, new EnemyMoveInfo(ACT_3_WEAK_BOMB, Intent.DEBUFF, 0, 1, false));
                this.moves.put(ACT_3_STEAL_GOLD, new EnemyMoveInfo(ACT_3_STEAL_GOLD, Intent.ATTACK, 3, 1, true));
                this.moves.put(ACT_3_STOLEN_WHIRLWIND, new EnemyMoveInfo(ACT_3_STOLEN_WHIRLWIND, Intent.ATTACK, 6, 4, true));
        		
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
            					info, AMOUNT_OF_GOLD_TO_STEAL_PER_ATTACK));
            	break;
            case ACT_1_PRANK_THEFT:
            	AbstractDungeon.actionManager.addToBottom(new AnimateHopAction(this));
            	doPrankTheft(AbstractDungeon.actNum);
            	break;
            case ACT_1_VULNERABLE_BOMB:
            	AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
            	AbstractDungeon.actionManager.addToBottom(
            			new VFXAction(
            				new SmokeBombEffect(AbstractDungeon.player.hb.cX,
            						AbstractDungeon.player.hb.cY)));
            	AbstractDungeon.actionManager.addToBottom(
            			new ApplyPowerAction(AbstractDungeon.player,
            					this,
            					new VulnerablePower(AbstractDungeon.player, 2, true)));
            	break;
            case ACT_2_STEAL_GOLD:
            	playStealSfx();
            	AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
            	AbstractDungeon.actionManager.addToBottom(
            			new DamageAction(AbstractDungeon.player, 
            					info, AMOUNT_OF_GOLD_TO_STEAL_PER_ATTACK));
            	AbstractDungeon.actionManager.addToBottom(
            			new DamageAction(AbstractDungeon.player, 
            					info, AMOUNT_OF_GOLD_TO_STEAL_PER_ATTACK));
            	AbstractDungeon.actionManager.addToBottom(
            			new DamageAction(AbstractDungeon.player, 
            					info, AMOUNT_OF_GOLD_TO_STEAL_PER_ATTACK));
            	AbstractDungeon.actionManager.addToBottom(
            			new DamageAction(AbstractDungeon.player, 
            					info, AMOUNT_OF_GOLD_TO_STEAL_PER_ATTACK));
            	break;
            case ACT_2_FRAIL_BOMB:
            	AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
            	AbstractDungeon.actionManager.addToBottom(
            			new VFXAction(
            				new SmokeBombEffect(AbstractDungeon.player.hb.cX,
            						AbstractDungeon.player.hb.cY)));
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
            				new SmokeBombEffect(AbstractDungeon.player.hb.cX,
            						AbstractDungeon.player.hb.cY)));
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
            					info, AMOUNT_OF_GOLD_TO_STEAL_PER_ATTACK));
            	break;
            case ACT_3_STOLEN_WHIRLWIND:
            	AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
            	AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_WHIRLWIND"));
            	for (int i = 0; i < STOLEN_WHIRLWIND_AMOUNT_OF_HITS; i++) {
            	      AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_HEAVY"));
            	      AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new CleaveEffect(true), 0.15F));
            	      AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AMOUNT_OF_GOLD_TO_STEAL_PER_ATTACK));
            	    } 
            	break;
            default:
            	break;
        }
       
        
        ++turnCounter;
    }
    
    private void playStealSfx() {
        AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_LOOTER_1A", 1f));
        AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_LOOTER_1B", 1f));
        AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_LOOTER_1C", 1f));
    }
    
    private void doPrankTheft(int actNum) {
    	
    	switch (actNum) {
    		case 1:
    			if (MiscFunctions.headsOrTails(AbstractDungeon.relicRng) == 1) {
    				giveFullOfOpenings();
    			} else {
    				giveTooCautious();
    			}
    			break;
    		case 2:
    			if (MiscFunctions.headsOrTails(AbstractDungeon.relicRng) == 1) {
    				giveTooCautious();
    			} else {
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
    	AbstractDungeon.actionManager.addToBottom(
        		new VFXAction(
        			new SmokeBombEffect(AbstractDungeon.player.hb.cX,
        				AbstractDungeon.player.hb.cY)));
        	
        if (AbstractDungeon.player.hasBlight(FullOfOpenings.ID)) {
        	AbstractDungeon.player.getBlight(FullOfOpenings.ID).stack();
        } else {
        	AbstractDungeon.currMapNode.room.spawnBlightAndObtain(
           			this.hb_h + this.hb_y/2, this.hb_w + this.hb_x/2,
           			new FullOfOpenings());
        }
    }
    
    private void giveTooCautious() {
    	AbstractDungeon.actionManager.addToBottom(
        		new VFXAction(
        			new SmokeBombEffect(AbstractDungeon.player.hb.cX,
        				AbstractDungeon.player.hb.cY)));
        	
        if (AbstractDungeon.player.hasBlight(TooCautious.ID)) {
        	AbstractDungeon.player.getBlight(TooCautious.ID).stack();
        } else {
        	AbstractDungeon.currMapNode.room.spawnBlightAndObtain(
           			this.hb_h + this.hb_y/2, this.hb_w + this.hb_x/2,
           			new TooCautious());
        }
    }

    private void giveNotAskedDonationsToTheBloodFund() {
    	AbstractDungeon.actionManager.addToBottom(
        	new VFXAction(
        		new SmokeBombEffect(AbstractDungeon.player.hb.cX,
        			AbstractDungeon.player.hb.cY)));
    	
        	AbstractDungeon.currMapNode.room.spawnBlightAndObtain(
           			this.hb_h + this.hb_y/2, this.hb_w + this.hb_x/2,
           			new TooCautious());
    }
    
    protected void getMove(int num) {
    	if (AbstractDungeon.actNum == 1) {
    		if (turnCounter == 0) {
                setMoveShortcut(defineFirstMove());
            } else if ((turnCounter == 1) && (!gave_money)) {
                setMoveShortcut(ACT_1_VULNERABLE_BOMB);
            } else if ((turnCounter == 1) && (gave_money)) {
                setMoveShortcut((byte)-128);
            } else {
                setMoveShortcut(ACT_1_PRANK_THEFT);
            }
    	}
    	
    	else if (AbstractDungeon.actNum == 2) {
    		if (turnCounter == 0) {
                setMoveShortcut(defineFirstMove());
            } else if ((turnCounter == 1) && (gave_money)) {
                setMoveShortcut((byte)-128);
            } else if ((turnCounter == 1)) {
            	setMoveShortcut(ACT_2_FRAIL_BOMB);
            } else {
                setMoveShortcut(ACT_2_PRANK_THEFT);
            }
    	}
    	else {
    		if (turnCounter == 0) {
                setMoveShortcut(ACT_3_WEAK_BOMB);
            } else if (turnCounter == 1) {
                setMoveShortcut(ACT_3_STEAL_GOLD);
            } else {
                setMoveShortcut(ACT_3_STOLEN_WHIRLWIND);
            }
    	}
    	
    }
    
    

}
