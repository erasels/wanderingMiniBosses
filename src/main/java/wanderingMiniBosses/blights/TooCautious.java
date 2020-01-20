package wanderingMiniBosses.blights;

import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TooCautious extends AbstractBlight {

	public static final String ID = "TooCautious";
	private static final BlightStrings blightStrings = 
			CardCrawlGame.languagePack.getBlightString("TooCautious");
	public static final String NAME = blightStrings.NAME;
	public static final String[] DESC = blightStrings.DESCRIPTION;

	public static final int AMOUNT_OF_GOLD_LOST = 1;
	
	public TooCautious() {
		super(ID, NAME, DESC[0] + AMOUNT_OF_GOLD_LOST + DESC[1], "accursed.png", false);
		this.counter = 1;
	}
	
	@Override
	public void updateDescription() {
		description = DESC[0] + this.counter + DESC[1];
	}
	
    public void stack() {
        ++this.counter;
        this.updateDescription();
        this.flash();
    }
	
    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
    	if (card.type == CardType.SKILL) {
    		AbstractDungeon.player.loseGold(this.counter);
    		
    		this.flash();
    	}
    	super.onPlayCard(card, m);
    }
    
}
