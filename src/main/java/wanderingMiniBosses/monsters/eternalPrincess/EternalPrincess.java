package wanderingMiniBosses.monsters.eternalPrincess;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.monsters.AbstractWanderingBoss;

public class EternalPrincess extends AbstractWanderingBoss {
    public static final String ID = WanderingminibossesMod.makeID("EternalPrincess");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    protected static final float HB_WIDTH = 140.0F; //scale is all multiplied in abstract monster class
    protected static final float HB_HEIGHT = 140.0F;

    private static final byte FINALE_OF_GLORY = 0; //give strength to everyone
    private static final byte FINALE_OF_DEVASTATION = 1; //apply 2 Vulnerable to everyone
    private static final byte FINALE_OF_PROMISE = 2; //heal everyone for 10% max HP
    private static final byte FINALE_OF_ETERNITY = 3; //only uses this if all other enemies are dead

    private static final int MAX_HEALTH = 250;

    private static final int STRENGTH = 3;
    private static final int STRENGTH_ACT_BONUS = 1;
    private static final int BYRD_STRENGTH = 1; //make exception for byrds cause that would be kind of brutal lol
    private static final int VULNERABLE = 2;
    private static final float HEAL = 0.10F;

    private static final int ETERNITY_DAMAGE = 10;
    private static final int ETERNITY_ACT_DAMAGE_BONUS = 5;
    private static final int ETERNITY_MAX_HITS = 3;

    private int strength;
    private int eternityDamage;
    private int eternityHits;
    private int moveCounter = 0;

    public EternalPrincess() {
        this(NAME, ID, MAX_HEALTH);
    }

    public EternalPrincess(String name, String id, int maxHealth) {
        super(name, id, maxHealth, 0, 0, HB_WIDTH, HB_HEIGHT, "");
    }

    @Override
    public void usePreBattleAction() {
        this.strength = STRENGTH + (STRENGTH_ACT_BONUS * (AbstractDungeon.actNum - 1));
        this.eternityDamage = ETERNITY_DAMAGE + (ETERNITY_ACT_DAMAGE_BONUS * (AbstractDungeon.actNum - 1));
        this.eternityHits = ETERNITY_MAX_HITS;
        this.moves.put(FINALE_OF_GLORY, new EnemyMoveInfo(FINALE_OF_GLORY, Intent.BUFF, -1, 0, false));
        this.moves.put(FINALE_OF_DEVASTATION, new EnemyMoveInfo(FINALE_OF_DEVASTATION, Intent.STRONG_DEBUFF, -1, 0, false));
        this.moves.put(FINALE_OF_PROMISE, new EnemyMoveInfo(FINALE_OF_PROMISE, Intent.BUFF, -1, 0, false));
        this.moves.put(FINALE_OF_ETERNITY, new EnemyMoveInfo(FINALE_OF_ETERNITY, Intent.ATTACK, eternityDamage, eternityHits, true));
        this.damage.add(new DamageInfo(this, this.eternityDamage));
        setMoveShortcut(FINALE_OF_GLORY);
    }

    @Override
    public void takeCustomTurn(DamageInfo info, int multiplier) {
        switch (this.nextMove) {
            case FINALE_OF_GLORY:
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, strength), strength));
                for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if(!m.isDeadOrEscaped() && !m.id.equals(ID)) {
                        if (m instanceof Byrd) {
                            addToBot(new ApplyPowerAction(m, this, new StrengthPower(m, BYRD_STRENGTH), BYRD_STRENGTH));
                        } else {
                            addToBot(new ApplyPowerAction(m, this, new StrengthPower(m, strength), strength));
                        }
                    }
                }
                moveCounter++;
                break;
            case FINALE_OF_DEVASTATION:
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, VULNERABLE, true), VULNERABLE));
                for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDeadOrEscaped() && !m.id.equals(ID)) {
                        addToBot(new ApplyPowerAction(m, this, new VulnerablePower(m, VULNERABLE, true), VULNERABLE));
                    }
                }
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
                for (int i = 0; i < eternityHits; i++) {
                    addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                }
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        if (isOnlyMonsterAlive()) {
            setMoveShortcut(FINALE_OF_ETERNITY);
        } else {
            if (moveCounter == FINALE_OF_GLORY) {
                setMoveShortcut(FINALE_OF_GLORY);
            } else if (moveCounter == FINALE_OF_DEVASTATION) {
                setMoveShortcut(FINALE_OF_DEVASTATION);
            } else if (moveCounter == FINALE_OF_PROMISE) {
                setMoveShortcut(FINALE_OF_PROMISE);
            } else {
                setMoveShortcut(FINALE_OF_ETERNITY);
            }
        }
    }

    private boolean isOnlyMonsterAlive() {
        for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if(!m.isDeadOrEscaped() && !m.id.equals(ID)) {
                return true;
            }
        }
        return false;
    }
}
