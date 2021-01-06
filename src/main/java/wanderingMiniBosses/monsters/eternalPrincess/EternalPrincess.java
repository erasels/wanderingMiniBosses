package wanderingMiniBosses.monsters.eternalPrincess;

import basemod.animations.SpriterAnimation;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.rewards.RewardItem;
import org.apache.commons.lang3.math.NumberUtils;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.cards.FinaleOfPromise;
import wanderingMiniBosses.monsters.AbstractWanderingBoss;
import wanderingMiniBosses.powers.delayedpowers.StrengthenAllPower;
import wanderingMiniBosses.powers.eternalPrincessPowers.DevastateEnemy;
import wanderingMiniBosses.powers.eternalPrincessPowers.DevastatePlayer;
import wanderingMiniBosses.relics.Blackblade;

import java.util.ArrayList;

public class EternalPrincess extends AbstractWanderingBoss {
    public static final String ID = WanderingminibossesMod.makeID("EternalPrincess");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;

    protected static final float HB_WIDTH = 280.0F; //scale is all multiplied in abstract monster class
    protected static final float HB_HEIGHT = 220.0F;

    private static final byte FINALE_OF_DEVASTATION = 0; //Kills all other enemies at end of round
    private static final byte FINALE_OF_ETERNITY = 1; //Revives all dead enemies as Wraiths
    private static final byte FINALE_OF_GLORY = 2; //give strength to everyone and block to other enemies
    //private static final byte FINALE_OF_PROMISE = 3; //heal everyone for 10% max HP

    private static final int MAX_HEALTH = 300;

    private static final int STRENGTH = 1;
    private static final int STRENGTH_ACT_BONUS = 1;
    //private static final float HEAL = 0.10F;
    private static final float WRAITH_HP_PERCENT = 0.70F;

    private static final int DEVASTATE_DAMAGE = 15;
    private static final int DEVASTATE_ACT_DAMAGE_BONUS = 5;

    private static final int BLOCK = 8;
    private static final int BLOCK_ACT_BONUS = 4;

    private int strength;
    private int devastateDamage;
    private int block;
    private int moveCounter = 0;

    private ArrayList<Float> xPositions = new ArrayList<>();
    private ArrayList<Float> yPositions = new ArrayList<>();

    public EternalPrincess() {
        this(0.0F, 0.0F);
    }

    public EternalPrincess(final float x, final float y) {
        super(NAME, ID, MAX_HEALTH, 0.0F, 300.0F, HB_WIDTH, HB_HEIGHT, null, x, y);
        this.animation = new SpriterAnimation("wanderingMiniBossesResources/images/eternalPrincess/Spriter/EternalPrincessAnimation.scml");
        rewards.add(new RewardItem(RelicLibrary.getRelic(Blackblade.ID).makeCopy()));
        RewardItem cardReward = new RewardItem(AbstractCard.CardColor.COLORLESS);
        cardReward.cards.clear();
        cardReward.cards.add(new FinaleOfPromise());
        rewards.add(cardReward);
    }

    @Override
    protected void populateMoves() {
        this.strength = STRENGTH + (STRENGTH_ACT_BONUS * (AbstractDungeon.actNum - 1));
        this.devastateDamage = DEVASTATE_DAMAGE + (DEVASTATE_ACT_DAMAGE_BONUS * (AbstractDungeon.actNum - 1));
        this.block = BLOCK + (BLOCK_ACT_BONUS * (AbstractDungeon.actNum - 1));
        this.moves.put(FINALE_OF_GLORY, new EnemyMoveInfo(FINALE_OF_GLORY, Intent.DEFEND_BUFF, -1, 0, false));
        this.moves.put(FINALE_OF_DEVASTATION, new EnemyMoveInfo(FINALE_OF_DEVASTATION, Intent.STRONG_DEBUFF, -1, 0, false));
        //this.moves.put(FINALE_OF_PROMISE, new EnemyMoveInfo(FINALE_OF_PROMISE, Intent.BUFF, -1, 0, false));
        this.moves.put(FINALE_OF_ETERNITY, new EnemyMoveInfo(FINALE_OF_ETERNITY, Intent.UNKNOWN, -1, 0, false));
    }

    @Override
    public void takeCustomTurn(DamageInfo info, int multiplier) {
        switch (this.nextMove) {
            case FINALE_OF_GLORY:
                for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDeadOrEscaped() && !m.id.equals(ID)) {
                        addToBot(new GainBlockAction(m, this, block));
                    }
                }
                addToBot(new ApplyPowerAction(this, this, new StrengthenAllPower(this, strength), strength));
                moveCounter++;
                break;
            case FINALE_OF_DEVASTATION:
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new DevastatePlayer(AbstractDungeon.player, devastateDamage), devastateDamage));
                addToBot(new ApplyPowerAction(this, this, new DevastateEnemy(this)));
                //Grabs the positions of the monsters at this point in time in case they leave later or something
                xPositions.clear();
                yPositions.clear();
                for (int i = 0; i < AbstractDungeon.getCurrRoom().monsters.monsters.size(); i++) {
                    AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.monsters.get(i);
                    xPositions.add(m.drawX);
                    yPositions.add(m.drawY);
                }
                moveCounter++;
                break;
//            case FINALE_OF_PROMISE:
//                addToBot(new HealAction(AbstractDungeon.player, this, (int)(AbstractDungeon.player.maxHealth * HEAL)));
//                for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
//                    if (!m.isDeadOrEscaped() && !m.id.equals(ID)) {
//                        addToBot(new HealAction(m, this, (int)(m.maxHealth * HEAL)));
//                    }
//                }
//                moveCounter++;
//                break;
            case FINALE_OF_ETERNITY:
                for (int i = 0; i < AbstractDungeon.getCurrRoom().monsters.monsters.size(); i++) {
                    AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.monsters.get(i);
                    if (m.isDead && !m.escaped && !m.id.equals(ID)) {
                        Wraith wraith = new Wraith(0.0F, 0.0F, (int)(NumberUtils.min(m.maxHealth, 500) * WRAITH_HP_PERCENT));
                        wraith.drawX = xPositions.get(i);
                        wraith.drawY = yPositions.get(i);
                        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(wraith, false, i));
                    }
                }
                moveCounter++;
                break;
        }
    }

    @Override
    public void onEscape() {
        super.onEscape();
        this.animation.setFlip(true, false);
    }

    @Override
    protected void getMove(int i) {
        if (moveCounter == FINALE_OF_DEVASTATION) {
            setMoveShortcut(FINALE_OF_DEVASTATION, MOVES[FINALE_OF_DEVASTATION]);
        } else if (moveCounter == FINALE_OF_ETERNITY) {
            setMoveShortcut(FINALE_OF_ETERNITY, MOVES[FINALE_OF_ETERNITY]);
        } else if (moveCounter == FINALE_OF_GLORY) {
            setMoveShortcut(FINALE_OF_GLORY, MOVES[FINALE_OF_GLORY]);
        } else {
            setMoveShortcut(FINALE_OF_DEVASTATION, MOVES[FINALE_OF_DEVASTATION]);
        }
    }

//    private boolean isOnlyMonsterAlive() {
//        for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
//            if(!m.isDeadOrEscaped() && !m.id.equals(ID)) {
//                return false;
//            }
//        }
//        return true;
//    }
}
