package wanderingMiniBosses.blights;

import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import basemod.interfaces.PostPotionUseSubscriber;
import wanderingMiniBosses.WanderingminibossesMod;

public class TrapsAtThePotionSlots extends AbstractBlight implements PostPotionUseSubscriber {

	public static final String ID = WanderingminibossesMod.makeID("TrapsAtThePotionSlots");
	private static final BlightStrings blightStrings = 
			CardCrawlGame.languagePack.getBlightString(ID);
	public static final String NAME = blightStrings.NAME;
	public static final String[] DESC = blightStrings.DESCRIPTION;

	public static final int AMOUNT_OF_HP_LOST = 5;
	public static final int FIRST_STACK_ADDS = 2;
	public static final int FINAL_STACK_ADDS = 3;
	
	public TrapsAtThePotionSlots() {
		super(ID, NAME, DESC[0] + AMOUNT_OF_HP_LOST + DESC[1], "accursed.png", false);
		this.counter = AMOUNT_OF_HP_LOST;
	}
	
	@Override
	public void updateDescription() {
		description = DESC[0] + this.counter + DESC[1];
	}
	
    public void stack() {
        if (this.counter == AMOUNT_OF_HP_LOST) {
        	this.counter += FIRST_STACK_ADDS;
        }
        else if (this.counter == AMOUNT_OF_HP_LOST + FIRST_STACK_ADDS) {
        	this.counter += FINAL_STACK_ADDS;
        }
        this.updateDescription();
        this.flash();
    }

	@Override
	public void receivePostPotionUse(AbstractPotion arg0) {
		AbstractDungeon.player.heal(this.counter * -1);
		
	}
}
