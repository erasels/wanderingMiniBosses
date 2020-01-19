package wanderingMiniBosses.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.RunicPyramid;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.actions.SetPlayerBurnAction;

public class BossBurn extends AbstractCard {
    public static final String ID = WanderingminibossesMod.makeID("BossBurn");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "status/burn";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.STATUS;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 2;

    public BossBurn() {
        this(0);
    }
    public BossBurn(int timesUpgraded) {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, CardColor.CURSE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 1;
        this.exhaust = true;
        if(this.timesUpgraded > 0) {
            if(this.timesUpgraded > 1) {
                this.upgradeMagicNumber(timesUpgraded - 1);
                this.timesUpgraded = timesUpgraded - 1;
            }
            this.upgrade();
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        //SetPlayerBurnAction.addToBottom();
        return super.makeStatEquivalentCopy();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        SetPlayerBurnAction.addToBottom();
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        super.triggerOnEndOfPlayerTurn();
        if(AbstractDungeon.player.hasRelic(RunicPyramid.ID)) {
            AbstractDungeon.player.hand.moveToDiscardPile(this);
        }
    }

    @Override
    public void triggerOnExhaust() {
        SetPlayerBurnAction.addToBottom();
    }

    @Override
    public AbstractCard makeCopy() {
        return new BossBurn();
    }

    @Override
    public void upgrade() {
        this.upgradeMagicNumber(1);
        ++this.timesUpgraded;
        this.name = NAME + "+" + this.timesUpgraded;
        this.initializeTitle();
        this.upgraded = true;
        this.exhaust = false;
        SetPlayerBurnAction.addToBottom();
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    public void triggerWhenDrawn() {
        if(!AbstractDungeon.player.hasPower("No Draw")) {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
