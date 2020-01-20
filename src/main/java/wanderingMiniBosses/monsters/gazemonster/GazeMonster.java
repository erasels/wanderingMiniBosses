package wanderingMiniBosses.monsters.gazemonster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Bone;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import com.megacrit.cardcrawl.vfx.combat.SearingBlowEffect;
import com.megacrit.cardcrawl.vfx.combat.SweepingBeamEffect;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.actions.GoingOutSpinningAction;
import wanderingMiniBosses.cards.BossBurn;
import wanderingMiniBosses.monsters.AbstractRotatableBoss;
import wanderingMiniBosses.powers.delayedpowers.DelayBurnPower;
import wanderingMiniBosses.powers.delayedpowers.StrengthenAllPower;
import wanderingMiniBosses.powers.delayedpowers.VulnerableAllPower;
import wanderingMiniBosses.powers.gazepowers.GazeOne;
import wanderingMiniBosses.powers.gazepowers.GazeThree;
import wanderingMiniBosses.powers.gazepowers.GazeTwo;
import wanderingMiniBosses.relics.AbyssPearl;
import wanderingMiniBosses.vfx.general.GaudyEntranceVFX;

public class GazeMonster extends AbstractRotatableBoss {
    public static final String ID = WanderingminibossesMod.makeID("GazeMonster");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    private Orbital[] orbs;
    private boolean renderThis;

    private static final byte LASERSHOWER = 0;
    private static final byte STRENGTHEN = 1;
    private static final byte BOSSBURN = 2;
    private static final byte LIGHTNING = 3;

    public GazeMonster() {
        super(NAME, ID, 400, 0.0F, 10.0F, 280.0F, 280.0F, null, -450F, 300F);
        this.drawX = Settings.WIDTH / 2F;

        this.loadAnimation(WanderingminibossesMod.makeMonsterPath("gazemonster/skeleton.atlas"), WanderingminibossesMod.makeMonsterPath("gazemonster/skeleton.json"), 1.0F);
        this.state.setTimeScale(0.4F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Hit", "Idle", 0.2F);
        this.stateData.setMix("Idle", "Attack", 0.1F);

        orbs = new Orbital[3];
        orbs[0] = new Orbital(skeleton.findBone("Orb_Down"), new NumberlessDark());
        orbs[1] = new Orbital(skeleton.findBone("Orb_Left"), new NumberlessLightning());
        orbs[2] = new Orbital(skeleton.findBone("Orb_Right"), new Plasma());

        rewards.add(new RewardItem(RelicLibrary.getRelic(AbyssPearl.ID).makeCopy()));
        renderThis = false;
    }

    @Override
    protected void populateMoves() {
        this.moves.put(LASERSHOWER, new EnemyMoveInfo(LASERSHOWER, Intent.ATTACK, 4 * AbstractDungeon.actNum, 2, true));
        this.moves.put(STRENGTHEN, new EnemyMoveInfo(STRENGTHEN, Intent.BUFF, -1, 0, false));
        this.moves.put(BOSSBURN, new EnemyMoveInfo(BOSSBURN, Intent.STRONG_DEBUFF, -1, 0, false));
        this.moves.put(LIGHTNING, new EnemyMoveInfo(LIGHTNING, Intent.ATTACK_DEBUFF, 6 * AbstractDungeon.actNum, 0, false));
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GazeOne(this)));
        if(AbstractDungeon.actNum > 1) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GazeTwo(this)));
            if(AbstractDungeon.actNum > 2) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GazeThree(this)));
            }
        }
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new GaudyEntranceVFX(this), 1.5F));
        renderThis = true;
    }

    @Override
    public void takeCustomTurn(DamageInfo info, int multiplier) {
        switch(this.nextMove) {
            case LASERSHOWER:
                addToBot(new VFXAction(new SweepingBeamEffect(hb.x + hb.width / 4F, hb.y + hb.height * 3F / 4F, false)));
                addToBot(new VFXAction(new SweepingBeamEffect(hb.x + hb.width / 4F, hb.y + hb.height / 4F, true)));
                addToBot(new VFXAction(new SweepingBeamEffect(hb.x + hb.width * 3F / 4F, hb.y + hb.height * 3F / 4F, true)));
                addToBot(new VFXAction(new SweepingBeamEffect(hb.x + hb.width * 3F / 4F, hb.y + hb.height / 4F, false)));
                for(int i = 0; i < multiplier; i++) {
                    addToBot(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                }
                for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if(mo != this && !mo.isDeadOrEscaped()) {
                        for (int i = 0; i < multiplier; i++) {
                            info.applyPowers(this, mo);
                            addToBot(new DamageAction(mo, info, AbstractGameAction.AttackEffect.NONE));
                        }
                    }
                }
                break;

            case STRENGTHEN:
                addToBot(new ApplyPowerAction(this, this, new StrengthenAllPower(this, AbstractDungeon.actNum * 2), AbstractDungeon.actNum * 2));
                break;

            case BOSSBURN:
                addToBot(new VFXAction(new SearingBlowEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, AbstractDungeon.actNum)));
                addToBot(new MakeTempCardInDrawPileAction(new BossBurn(), AbstractDungeon.actNum, true, true));
                addToBot(new ApplyPowerAction(this, this, new DelayBurnPower(this)));
                break;

            case LIGHTNING:
                addToBot(new VFXAction(new LightningEffect(hb.cX, hb.cY), 0.1F));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ORB_LIGHTNING_EVOKE"));
                addToBot(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if(mo != this && !mo.isDeadOrEscaped()) {
                        info.applyPowers(this, mo);
                        addToBot(new VFXAction(new LightningEffect(mo.hb.cX, mo.hb.cY), 0.1F));
                        AbstractDungeon.actionManager.addToBottom(new SFXAction("ORB_LIGHTNING_EVOKE"));
                        addToBot(new DamageAction(mo, info, AbstractGameAction.AttackEffect.NONE));
                    }
                }
                addToBot(new ApplyPowerAction(this, this, new VulnerableAllPower(this, 1), 1));
                break;
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
            this.state.setAnimation(0, "Hit", false);
            this.state.setTimeScale(0.4F);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
    }

    @Override
    public void update() {
        super.update();
        if(!isDead && !escaped) {
            for (int i = 0; i < orbs.length; i++) {
                orbs[i].update(this.skeleton.getX(), this.skeleton.getY());
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if(renderThis && !isDead && !escaped) {
            for (int i = 0; i < orbs.length; i++) {
                if (orbs[i].renderBehind) {
                    orbs[i].render(sb);
                }
            }
            super.render(sb);
            for (int i = 0; i < orbs.length; i++) {
                if (!orbs[i].renderBehind) {
                    orbs[i].render(sb);
                }
            }
        }
    }

    @Override
    public void onEscape() {
        addToBot(new GoingOutSpinningAction(this));
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }

    static class Orbital {
        AbstractOrb orb;
        Bone bone;
        boolean renderBehind;

        public Orbital(Bone bone, AbstractOrb orb) {
            this.bone = bone;
            this.orb = orb;
        }

        public void update(float skeletonX, float skeletonY) {
            orb.cX = skeletonX + bone.getWorldX();
            orb.cY = skeletonY + bone.getWorldY();
            renderBehind = orb.cX > orb.tX + AbstractDungeon.player.animX;
            orb.tX = orb.cX - AbstractDungeon.player.animX;
            orb.tY = orb.cY - AbstractDungeon.player.animY;
            orb.updateAnimation();
        }

        public void render(SpriteBatch sb) {
            orb.render(sb);
        }
    }
}
