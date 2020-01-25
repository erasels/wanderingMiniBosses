package wanderingMiniBosses.monsters.eternalPrincess;

import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Decay;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import wanderingMiniBosses.WanderingminibossesMod;

public class Wraith extends CustomMonster
{
    public static final String ID = WanderingminibossesMod.makeID("Wraith");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;

    private static final byte ATTACK = 0;
    private static final byte CURSE = 1;
    private static final int DAMAGE = 4;
    private static final int ACT_DAMAGE_BONUS = 1;
    private static final int DAMAGE_HITS = 2;
    private static final int CURSE_AMT = 1;
    private static final int HP_MAX = 100;
    private int attackDamage;
    private int curses;
    private int damageHits;

    public Wraith() {
        this(0.0f, 0.0f, HP_MAX);
    }

    public Wraith(final float x, final float y, int HP) {
        super(NAME, ID, HP_MAX, -5.0F, 0, 160.0f, 205.0f, null, x, y);
        this.animation = new SpriterAnimation("wanderingMiniBossesResources/images/Wraith/Spriter/WraithAnimation.scml");
        this.type = EnemyType.NORMAL;

        this.curses = CURSE_AMT;
        this.setHp(HP);
        this.attackDamage = DAMAGE + (ACT_DAMAGE_BONUS * (AbstractDungeon.actNum - 1));
        this.damageHits = DAMAGE_HITS;

        this.damage.add(new DamageInfo(this, this.attackDamage));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case ATTACK: {
                for (int i = 0; i < damageHits; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.POISON));
                }
                damageHits++;
                break;
            }
            case CURSE: {
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Decay(), curses, true, true));
                curses++;
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (num < 50 && !this.lastTwoMoves(ATTACK)) {
            this.setMove(ATTACK, Intent.ATTACK, this.damage.get(0).base, damageHits, true);
        }  else if (!this.lastTwoMoves(CURSE)){
            this.setMove(CURSE, Intent.STRONG_DEBUFF);
        } else {
            this.setMove(ATTACK, Intent.ATTACK, this.damage.get(0).base, damageHits, true);
        }
    }
}