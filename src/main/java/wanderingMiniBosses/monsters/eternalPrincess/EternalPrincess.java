package wanderingMiniBosses.monsters.eternalPrincess;

import basemod.animations.SpriterAnimation;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.rewards.RewardItem;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.monsters.AbstractWanderingBoss;
import wanderingMiniBosses.powers.delayedpowers.StrengthenAllPower;
import wanderingMiniBosses.powers.delayedpowers.VulnerableAllPower;
import wanderingMiniBosses.powers.eternalPrincessPowers.BountyOfPlenty;
import wanderingMiniBosses.relics.Blackblade;

public class EternalPrincess extends AbstractWanderingBoss {
    public static final String ID = WanderingminibossesMod.makeID("EternalPrincess");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;

    protected static final float HB_WIDTH = 280.0F; //scale is all multiplied in abstract monster class
    protected static final float HB_HEIGHT = 220.0F;

    private static final byte FINALE_OF_GLORY = 0; //give strength to everyone
    private static final byte FINALE_OF_DEVASTATION = 1; //apply 2 Vulnerable to everyone
    private static final byte FINALE_OF_PROMISE = 2; //heal everyone for 10% max HP
    private static final byte FINALE_OF_ETERNITY = 3; //only uses this if all other enemies are dead

    private static final int MAX_HEALTH = 250;

    private static final int STRENGTH = 3;
    private static final int STRENGTH_ACT_BONUS = 1;
    private static final int BYRD_STRENGTH = 1; //make exception for byrds cause that would be kind of brutal lol
    private static final int VULNERABLE = 3;
    private static final float HEAL = 0.10F;

//    private static final int ETERNITY_DAMAGE = 8;
//    private static final int ETERNITY_ACT_DAMAGE_BONUS = 4;
//    private static final int ETERNITY_MAX_HITS = 3;

    private int strength;
    //private int eternityDamage;
    //private int eternityHits;
    private int moveCounter = 0;

    public EternalPrincess() {
        this(0.0F, 0.0F);
    }

    public EternalPrincess(final float x, final float y) {
        super(NAME, ID, MAX_HEALTH, 0.0F, 300.0F, HB_WIDTH, HB_HEIGHT, null, x, y);
        this.animation = new SpriterAnimation("wanderingMiniBossesResources/images/eternalPrincess/Spriter/EternalPrincessAnimation.scml");
        rewards.add(new RewardItem(RelicLibrary.getRelic(Blackblade.ID).makeCopy()));
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new BountyOfPlenty(AbstractDungeon.player)));
    }

    @Override
    protected void populateMoves() {
        this.strength = STRENGTH + (STRENGTH_ACT_BONUS * (AbstractDungeon.actNum - 1));
        //this.eternityDamage = ETERNITY_DAMAGE + (ETERNITY_ACT_DAMAGE_BONUS * (AbstractDungeon.actNum - 1));
        //this.eternityHits = ETERNITY_MAX_HITS;
        this.moves.clear();
        this.moves.put(FINALE_OF_GLORY, new EnemyMoveInfo(FINALE_OF_GLORY, Intent.BUFF, -1, 0, false));
        this.moves.put(FINALE_OF_DEVASTATION, new EnemyMoveInfo(FINALE_OF_DEVASTATION, Intent.STRONG_DEBUFF, -1, 0, false));
        this.moves.put(FINALE_OF_PROMISE, new EnemyMoveInfo(FINALE_OF_PROMISE, Intent.BUFF, -1, 0, false));
        this.moves.put(FINALE_OF_ETERNITY, new EnemyMoveInfo(FINALE_OF_ETERNITY, Intent.UNKNOWN, -1, 0, false));
        this.moves.put(RUN, new EnemyMoveInfo(RUN, Intent.ESCAPE, -1, 0, false));
        //this.damage.add(new DamageInfo(this, this.eternityDamage));
    }

    @Override
    public void takeCustomTurn(DamageInfo info, int multiplier) {
        switch (this.nextMove) {
            case FINALE_OF_GLORY:
                addToBot(new ApplyPowerAction(this, this, new StrengthenAllPower(this, strength), strength));
                moveCounter++;
                break;
            case FINALE_OF_DEVASTATION:
                addToBot(new ApplyPowerAction(this, this, new VulnerableAllPower(this, VULNERABLE), VULNERABLE));
                moveCounter++;
                break;
            case FINALE_OF_PROMISE:
                addToBot(new HealAction(AbstractDungeon.player, this, (int)(AbstractDungeon.player.maxHealth * HEAL)));
                for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDeadOrEscaped() && !m.id.equals(ID)) {
                        addToBot(new HealAction(m, this, (int)(m.maxHealth * HEAL)));
                    }
                }
                moveCounter++;
                break;
            case FINALE_OF_ETERNITY:
                for (int i = 0; i < AbstractDungeon.getCurrRoom().monsters.monsters.size(); i++) {
                    AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.monsters.get(i);
                    if (m.isDead && !m.id.equals(ID)) {
                        Wraith wraith = new Wraith(0.0F, 0.0F, m.maxHealth / 3);
                        wraith.drawX = m.drawX;
                        wraith.drawY = m.drawY;
                        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(wraith, false, i));
                    }
                }
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
        if (isOnlyMonsterAlive()) {
            setMoveShortcut(FINALE_OF_ETERNITY, MOVES[FINALE_OF_ETERNITY]);
        } else {
            if (moveCounter == FINALE_OF_GLORY) {
                setMoveShortcut(FINALE_OF_GLORY, MOVES[FINALE_OF_GLORY]);
            } else if (moveCounter == FINALE_OF_DEVASTATION) {
                setMoveShortcut(FINALE_OF_DEVASTATION, MOVES[FINALE_OF_DEVASTATION]);
            } else if (moveCounter == FINALE_OF_PROMISE) {
                setMoveShortcut(FINALE_OF_PROMISE, MOVES[FINALE_OF_PROMISE]);
            } else {
                setMoveShortcut(FINALE_OF_ETERNITY, MOVES[FINALE_OF_ETERNITY]);
            }
        }
    }

    private boolean isOnlyMonsterAlive() {
        for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if(!m.isDeadOrEscaped() && !m.id.equals(ID)) {
                return false;
            }
        }
        return true;
    }
}
