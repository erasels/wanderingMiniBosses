package wanderingMiniBosses.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.actions.FinaleOfPromiseAction;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static wanderingMiniBosses.WanderingminibossesMod.makeCardPath;

public class FinaleOfPromise extends CustomCard {

    public static final String ID = WanderingminibossesMod.makeID(FinaleOfPromise.class.getSimpleName());
    public static final String IMG = makeCardPath("FinaleOfPromise.png");

    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.SPECIAL;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.NONE;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = AbstractCard.CardColor.COLORLESS;

    private static final int COST = -1;
    private static final int PLAY_AMOUNT = 1;
    private static final int NUM_CARDS = 2;
    private static final int X_THRESHOLD = 6;

    public FinaleOfPromise() {
        super(ID, languagePack.getCardStrings(ID).NAME, IMG, COST, languagePack.getCardStrings(ID).DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = baseMagicNumber = NUM_CARDS;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (p.hasRelic(ChemicalX.ID)) {
            effect += 2;
            p.getRelic(ChemicalX.ID).flash();
        }

        if (upgraded) {
            effect++;
        }

        int playAmount = PLAY_AMOUNT;
        if (effect >= X_THRESHOLD) {
            playAmount++;
        }

        AbstractDungeon.actionManager.addToBottom(new FinaleOfPromiseAction(playAmount, effect, magicNumber));

        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = languagePack.getCardStrings(cardID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
