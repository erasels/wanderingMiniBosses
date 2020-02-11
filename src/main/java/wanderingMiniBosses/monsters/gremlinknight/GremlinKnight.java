package wanderingMiniBosses.monsters.gremlinknight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.city.Champ;
import com.megacrit.cardcrawl.powers.AngryPower;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.SurroundedPower;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.actions.OneMoreKickAction;
import wanderingMiniBosses.actions.RammingEntranceAction;
import wanderingMiniBosses.monsters.AbstractWanderingBoss;
import wanderingMiniBosses.powers.delayedpowers.VulnerableAllPower;
import wanderingMiniBosses.powers.delayedpowers.WeakAllPower;
import wanderingMiniBosses.relics.OtherGremlinHorn;

public class GremlinKnight extends AbstractWanderingBoss {
    public static final String ID = WanderingminibossesMod.makeID("GremlinKnight");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    private static final byte STAB = 0;
    private static final byte STABSTAB = 1;
    private static final byte BONK = 2;
    private static final byte TAUNT = 3;

    private final int entranceThreshold = 3;

    public GremlinKnight() {
        super(NAME, ID, 200, 0.0F, 10.0F, 230.0F, 230.0F, null, -Settings.WIDTH, -30);
        this.isPlayer = true;
        this.drawX = -this.hb.width;
        this.flipHorizontal = true;

        this.dialogX = this.hb.width / 4F;
        this.dialogY = 0;

        this.loadAnimation(WanderingminibossesMod.makeMonsterPath("gremlinknight/skeleton.atlas"), WanderingminibossesMod.makeMonsterPath("gremlinknight/skeleton.json"), 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        rewards.add(new RewardItem(new OtherGremlinHorn()));
    }

    @Override
    protected void populateMoves() {
        this.moves.put(STAB, new EnemyMoveInfo(STAB, Intent.ATTACK, 1 + 5 * AbstractDungeon.actNum, 0, false));
        this.moves.put(STABSTAB, new EnemyMoveInfo(STABSTAB, Intent.ATTACK, 3, 1 + AbstractDungeon.actNum, true));
        this.moves.put(BONK, new EnemyMoveInfo(BONK, Intent.ATTACK_DEBUFF, 3 + 2 * AbstractDungeon.actNum, 0, false));
        this.moves.put(TAUNT, new EnemyMoveInfo(TAUNT, Intent.DEBUFF, -1, 0, false));
        this.healthBarUpdatedEvent();
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.effectList.add(new SpeechBubble(Settings.WIDTH * 0.01F, AbstractDungeon.floorY + 150F * Settings.scale, DIALOG[MathUtils.random(entranceThreshold - 1)], true));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new AngryPower(this, 1)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BarricadePower(this)));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 25 + 25 * AbstractDungeon.actNum));
        AbstractDungeon.actionManager.addToBottom(new RammingEntranceAction(AbstractDungeon.player, this));
    }

    @Override
    public void takeCustomTurn(DamageInfo info, int multiplier) {
        switch(this.nextMove) {
            case STAB:
                this.useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SMASH));
                break;

            case STABSTAB:
                this.useFastAttackAnimation();
                for(int i = 0; i < multiplier; i++) {
                    AbstractGameAction.AttackEffect tmp = null;
                    switch(MathUtils.random(2)) {
                        case 0:
                            tmp = AbstractGameAction.AttackEffect.SLASH_DIAGONAL;
                            break;
                        case 1:
                            tmp = AbstractGameAction.AttackEffect.SLASH_HORIZONTAL;
                            break;
                        case 2:
                            tmp = AbstractGameAction.AttackEffect.SLASH_VERTICAL;
                            break;
                    }
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, tmp));
                }
                break;

            case BONK:
                this.useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 1, true), 1));
                break;

            case TAUNT:
                AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_CHAMP_2A", 0.4F, true));
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, Champ.DIALOG[MathUtils.random(3)]));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new WeakAllPower(this, 2), 2));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new VulnerableAllPower(this, 2), 2));
                break;
        }
    }

    @Override
    public void onEscape() {
        super.onEscape();
        this.flipHorizontal = !this.flipHorizontal;
        AbstractDungeon.actionManager.addToTop(new TalkAction(this, DIALOG[entranceThreshold + MathUtils.random(DIALOG.length - entranceThreshold - 1)]));
    }

    private void removeSurrounded() {
        boolean leftOfPlayer = false;
        for(final AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.isDead && !m.isDying) {
                leftOfPlayer |= m.drawX < AbstractDungeon.player.drawX;
                m.removeSurroundedPower();
            }
        }

        if (!leftOfPlayer && AbstractDungeon.player.hasPower(SurroundedPower.POWER_ID)) {
            AbstractDungeon.player.flipHorizontal = false;
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, SurroundedPower.POWER_ID));
        }
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
            this.escapeTimer -= Gdx.graphics.getDeltaTime();
            this.drawX += Gdx.graphics.getDeltaTime() * 400.0F * Settings.scale * (flipHorizontal ? 1 : -1);
        }

        if (this.escapeTimer < 0.0F) {
            this.removeSurrounded();
            if (this.escapeTimer > -10F && AbstractDungeon.monsterRng.randomBoolean(0.1F)) {
                this.setMove((byte)0, Intent.NONE);
                this.createIntent();
                this.escapeTimer = 0.0F;
                this.isEscaping = false;
                this.escaped = false;
                this.tint.color = Color.WHITE.cpy();
                AbstractDungeon.actionManager.addToBottom(new OneMoreKickAction(AbstractDungeon.player, this));
            } else {
                this.escaped = true;
                checkBattleEnd();
            }
        }
    }

    public static void checkBattleEnd() {
        if (AbstractDungeon.getMonsters().areMonstersDead() && !AbstractDungeon.getCurrRoom().isBattleOver && !AbstractDungeon.getCurrRoom().cannotLose) {
            AbstractDungeon.getCurrRoom().endBattle();
        }
    }
}
