package wanderingMiniBosses.blights;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.actions.RemoveSpecificBlightAction;

import java.util.ArrayList;


public class FullOfOpenings extends AbstractBlight implements CustomSavable<ArrayList<Integer>> {

	public static final String ID = WanderingminibossesMod.makeID("FullOfOpenings");
	private static final BlightStrings blightStrings = 
			CardCrawlGame.languagePack.getBlightString(ID);
	public static final String NAME = blightStrings.NAME;
	public static final String[] DESC = blightStrings.DESCRIPTION;

	public static final String FULL_OF_OPENINGS_SAVE_FIELD_KEY =
			"wandering_mini_bosses_full_openings_data";
	public static final int INITIAL_AMOUNT_OF_GOLD_STOLEN = 5;
	public static int AMOUNT_OF_GOLD_STOLEN =
			INITIAL_AMOUNT_OF_GOLD_STOLEN;
	public static final int INITIAL_AMOUNT_OF_GOLD_LEFT_TO_LOSE = 200;
	public static int AMOUNT_OF_GOLD_LEFT_TO_LOSE =
			INITIAL_AMOUNT_OF_GOLD_LEFT_TO_LOSE;

	
	public FullOfOpenings() {
		super(ID, NAME, DESC[0] + AMOUNT_OF_GOLD_STOLEN + DESC[1] +
				DESC[2] + AMOUNT_OF_GOLD_LEFT_TO_LOSE + DESC[3],
				"ancient.png", false);
		this.counter = 0;
		AMOUNT_OF_GOLD_STOLEN = INITIAL_AMOUNT_OF_GOLD_STOLEN;
		AMOUNT_OF_GOLD_LEFT_TO_LOSE = INITIAL_AMOUNT_OF_GOLD_LEFT_TO_LOSE;
	}

	@Override
	public ArrayList<Integer> onSave()
	{
		ArrayList<Integer> list_of_static = new ArrayList<Integer>();
		list_of_static.add(AMOUNT_OF_GOLD_STOLEN);
		list_of_static.add(AMOUNT_OF_GOLD_LEFT_TO_LOSE);
		return list_of_static;
		// Return the location of the card in your deck. AbstractCard cannot be serialized so we use an Integer instead.
	}

	public void onLoad(ArrayList<Integer> static_values)
	{
		AMOUNT_OF_GOLD_STOLEN = static_values.get(0);
		AMOUNT_OF_GOLD_LEFT_TO_LOSE = static_values.get(1);
	}

	public void updateTips(){
		updateDescription();
		tips.clear();
		this.tips.add(new PowerTip(this.name, this.description));
		this.initializeTips();
	}

	@Override
	public void updateDescription() {
		description =
				DESC[0] + AMOUNT_OF_GOLD_STOLEN + DESC[1] +
				DESC[2] + AMOUNT_OF_GOLD_LEFT_TO_LOSE + DESC[3];
	}
	
    public void stack() {
        AMOUNT_OF_GOLD_STOLEN += 5;
        this.updateTips();
        this.flash();
    }

	@Override
    public void atBattleStart(){
		updateTips();
	}
	
    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
		if (card.type == CardType.ATTACK) {
			counter += 1;
			if (counter >= 10){
				AbstractDungeon.player.loseGold(AMOUNT_OF_GOLD_STOLEN);
				AMOUNT_OF_GOLD_LEFT_TO_LOSE -= AMOUNT_OF_GOLD_STOLEN;
				this.updateTips();
				if (AMOUNT_OF_GOLD_LEFT_TO_LOSE > 0){
					this.flash();
					CardCrawlGame.sound.playA("VO_LOOTER_1A", 0.2F);
					CardCrawlGame.sound.playA("VO_LOOTER_1B", 0.5F);
					CardCrawlGame.sound.playA("VO_LOOTER_1C", 0.0F);
				}
				counter = 0;
			}
		}

		if (AMOUNT_OF_GOLD_LEFT_TO_LOSE <= 0) {
			AbstractDungeon.actionManager.addToBottom(
					new RemoveSpecificBlightAction(this));
		}
	}
}
