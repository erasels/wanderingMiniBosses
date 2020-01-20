package wanderingMiniBosses.blights;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import wanderingMiniBosses.WanderingminibossesMod;

public class NotAskedDonationsToTheBloodFund extends AbstractBlight {

	public static final String ID = WanderingminibossesMod.makeID("NotAskedDonationsToTheBloodFund");
	private static final BlightStrings blightStrings = 
			CardCrawlGame.languagePack.getBlightString("NotAskedDonationsToTheBloodFund");
	public static final String NAME = blightStrings.NAME;
	public static final String[] DESC = blightStrings.DESCRIPTION;

	public static final int AMOUNT_OF_DAMAGE_TAKEN = 3;
	
	public NotAskedDonationsToTheBloodFund() {
		super(ID, NAME, DESC[0] + AMOUNT_OF_DAMAGE_TAKEN + DESC[1], "void.png", false);
	}
	
    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
    	if (card.type == CardType.POWER) {
    		AbstractDungeon.actionManager.
    			addToBottom(
    				new DamageAction(
    					AbstractDungeon.player,
    					new DamageInfo(AbstractDungeon.player, AMOUNT_OF_DAMAGE_TAKEN)));
    		
    		this.flash();
    	}
    	super.onPlayCard(card, m);
    }
    
}
