package wanderingMiniBosses.monsters;

import basemod.BaseMod;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.rewards.RewardItem;
import wanderingMiniBosses.util.WanderingBossHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractWanderingBoss extends CustomMonster {
    public static final int RUNTIMER = 3;
    public static final byte RUN = Byte.MIN_VALUE;

    protected Map<Byte, EnemyMoveInfo> moves;
    protected int runTimer;
    protected ArrayList<RewardItem> rewards;

    public AbstractWanderingBoss(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
        setupMisc();
    }

    public AbstractWanderingBoss(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY, boolean ignoreBlights) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY, ignoreBlights);
        setupMisc();
    }

    public AbstractWanderingBoss(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl);
        setupMisc();
    }
    private void setupMisc() {
        this.type = EnemyType.BOSS;
        this.moves = new HashMap<>();
        this.runTimer = RUNTIMER;

        this.moves.put(RUN, new EnemyMoveInfo(RUN, Intent.ESCAPE, -1, 0, false));
        populateMoves();
        this.rewards = new ArrayList<>();
    }

    protected abstract void populateMoves();

    public void usePreBattleAction() {
        moves.clear();
        this.moves.put(RUN, new EnemyMoveInfo(RUN, Intent.ESCAPE, -1, 0, false));
        populateMoves();
    }

    @Override
    public void takeTurn() {
        if(this.nextMove == RUN) {
            onEscape();
            AbstractDungeon.actionManager.addToBottom(new EscapeAction(this));
        } else {
            DamageInfo info = null;
            int multiplier = 0;
            if(moves.containsKey(this.nextMove)) {
                EnemyMoveInfo emi = moves.get(this.nextMove);
                info = new DamageInfo(this, emi.baseDamage, DamageInfo.DamageType.NORMAL);
                if(emi.baseDamage > -1) {
                    info.applyPowers(this, AbstractDungeon.player);
                    multiplier = emi.multiplier;
                }
            } else {
                info = new DamageInfo(this, 0, DamageInfo.DamageType.NORMAL);
                BaseMod.logger.error(this.name + " MOVECODE " + this.nextMove + " NOT FOUND!");
            }
            takeCustomTurn(info, multiplier);
        }
        runTimer--;
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }
    public abstract void takeCustomTurn(DamageInfo info, int multiplier);

    @Override
    public void rollMove() {
        if(this.runTimer <= 0) {
            this.setMoveShortcut(RUN);
        } else {
            super.rollMove();
        }
    }

    @Override
    protected void getMove(int num) {
        ArrayList<Byte> possibilities = new ArrayList<>(this.moves.keySet());
        possibilities.remove(RUN);
        for(int i = this.moveHistory.size() - 1, found = 0; i >= 0 && found < moves.size() - 2; i--) {
            boolean foundThisCycle = false;
            int before;
            do {
                before = possibilities.size();
                possibilities.remove(this.moveHistory.get(i));
                if(!foundThisCycle && possibilities.size() != before) {
                    found++;
                    foundThisCycle = true;
                }
            } while(before != possibilities.size());
        }
        this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
    }

    public void onEscape() {

    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        WanderingBossHelper.getMonster().currentHealth = this.currentHealth;
    }

    public void setMoveShortcut(byte next, String text) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(text, next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }
    public void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    public void die(boolean triggerRelics) {
        AbstractDungeon.getCurrRoom().rewards.addAll(rewards);
        super.die(triggerRelics);
    }
}
