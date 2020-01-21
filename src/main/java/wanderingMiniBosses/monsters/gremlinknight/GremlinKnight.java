package wanderingMiniBosses.monsters.gremlinknight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.actions.RammingEntranceAction;
import wanderingMiniBosses.monsters.AbstractWanderingBoss;

public class GremlinKnight extends AbstractWanderingBoss {
    public static final String ID = WanderingminibossesMod.makeID("GremlinKnight");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    private static final byte SUMMONDAGGER = 0;

    private final int entranceThreshold = 3;

    public GremlinKnight() {
        super(NAME, ID, 400, 0.0F, 10.0F, 230.0F, 230.0F, null, 0, -30);
        this.drawX = -this.hb.width;
        this.flipHorizontal = true;

        this.loadAnimation(WanderingminibossesMod.makeMonsterPath("gremlinknight/skeleton.atlas"), WanderingminibossesMod.makeMonsterPath("gremlinknight/skeleton.json"), 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    @Override
    protected void populateMoves() {
        this.moves.put(SUMMONDAGGER, new EnemyMoveInfo(SUMMONDAGGER, Intent.UNKNOWN, -1, 0, false));
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.effectList.add(new SpeechBubble(Settings.WIDTH * 0.01F, AbstractDungeon.floorY + 150F * Settings.scale, DIALOG[MathUtils.random(entranceThreshold - 1)], true));
        AbstractDungeon.actionManager.addToBottom(new RammingEntranceAction(AbstractDungeon.player, this));
    }

    @Override
    public void takeCustomTurn(DamageInfo info, int multiplier) {
        switch(this.nextMove) {
        }
    }

    @Override
    public void onEscape() {
        super.onEscape();
        AbstractDungeon.effectList.add(new SpeechBubble(this.drawX + this.hb.width / 2F, this.drawY + this.hb.height / 2F, DIALOG[entranceThreshold + MathUtils.random(DIALOG.length - entranceThreshold - 1)], true));
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }

    @SpireOverride
    protected void updateEscapeAnimation() {
        if (this.escapeTimer != 0.0F) {
            this.flipHorizontal = false;
            this.escapeTimer -= Gdx.graphics.getDeltaTime();
            this.drawX -= Gdx.graphics.getDeltaTime() * 400.0F * Settings.scale;
        }

        if (this.escapeTimer < 0.0F) {
            this.escaped = true;
            if (AbstractDungeon.getMonsters().areMonstersDead() && !AbstractDungeon.getCurrRoom().isBattleOver && !AbstractDungeon.getCurrRoom().cannotLose) {
                AbstractDungeon.getCurrRoom().endBattle();
            }
        }

    }
}
