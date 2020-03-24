package wanderingMiniBosses.blights;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.abstracts.CustomSavableRaw;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import wanderingMiniBosses.WanderingminibossesMod;

import java.util.ArrayList;


public class FullOfOpenings extends AbstractBlight implements CustomSavable<ArrayList<Integer>> {

	public static final String ID = WanderingminibossesMod.makeID("FullOfOpenings");
	private static final BlightStrings blightStrings = 
			CardCrawlGame.languagePack.getBlightString(ID);
	public static final String NAME = blightStrings.NAME;
	public static final String[] DESC = blightStrings.DESCRIPTION;

	public static final String FULL_OF_OPENINGS_SAVE_FIELD_KEY =
			"wandering_mini_bosses_full_openings_data";
	public static int AMOUNT_OF_GOLD_STOLEN = 5;
	public static int AMOUNT_OF_GOLD_LEFT_TO_LOSE = 200;

	
	public FullOfOpenings() {
		super(ID, NAME, DESC[0] + AMOUNT_OF_GOLD_STOLEN + DESC[1], "ancient.png", false);
		this.counter = 0;
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
	
	@Override
	public void updateDescription() {
		description = DESC[0] + this.counter + DESC[1];
	}
	
    public void stack() {
        AMOUNT_OF_GOLD_STOLEN += 5;
        this.updateDescription();
        this.flash();
    }
	
    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
		if (card.type == CardType.ATTACK) {
			AbstractDungeon.player.loseGold(AMOUNT_OF_GOLD_STOLEN);
			AMOUNT_OF_GOLD_LEFT_TO_LOSE -= AMOUNT_OF_GOLD_STOLEN;

			this.flash();
		}
		super.onPlayCard(card, m);

		if (AMOUNT_OF_GOLD_LEFT_TO_LOSE <= 0) {
			for (int i = AbstractDungeon.player.blights.size() - 1; i >= 0; i--) {
				AbstractBlight blight = AbstractDungeon.player.blights.get(i);
				if (blight.blightID.equals(FullOfOpenings.ID)) {
					AbstractDungeon.player.blights.remove(i);
					break;
				}
			}
		}
	}
}
