package wanderingMiniBosses.monsters.immortalflame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.RunicDome;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.monsters.AbstractWanderingBoss;
import wanderingMiniBosses.powers.BlazingPower;
import wanderingMiniBosses.powers.InnerFlamePower;
import wanderingMiniBosses.relics.CarrionFlame;
import wanderingMiniBosses.vfx.general.CalmFireEffect;
import wanderingMiniBosses.vfx.general.CasualFlameParticleEffect;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.actNum;

public class ImmortalFlame extends AbstractWanderingBoss {
    public static final String ID = WanderingminibossesMod.makeID("ImmortalFlame");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    protected static final float HB_WIDTH = 140.0F; //scale is all multiplied in abstract monster class
    protected static final float HB_HEIGHT = 140.0F;

    protected static final float MAX_OFFSET = 30.0F * Settings.scale;
    protected static final float FLAME_CHANCE = 0.45f;

    protected float fireTimer = 0.0F;
    protected static final float FIRE_TIME = 0.01F;

    private int turnCounter = 0;

    private static final byte INNERFLAME = 0;
    private static final byte EXPLOSION = 1;
    private static final byte FLAMEWALL = 2;

    private static final int MAX_HEALTH = 190;

    private static final int EXPLOSION_DMG = 7;
    private static final int EXPLOSION_VULN = 2;

    private static final int FW_DMG = 2;
    private static final int FW_MULTI = 6;

    private static final int IF_HPL = 4;
    private static final int IF_SG = 1;

    private static final float MAX_Y = 250.0F;
    private static final float MIN_Y = 150.0F;
    private static final float MIN_X = -350.0F;
    private static final float MAX_X = 150.0F;

    public ImmortalFlame() {
        this(NAME, ID, MAX_HEALTH);
    }

    public ImmortalFlame(String name, String id, int maxHealth) {
        super(name, id, maxHealth, MathUtils.random(MIN_X, MAX_X), MathUtils.random(MIN_Y, MAX_Y), HB_WIDTH, HB_HEIGHT, "");
        rewards.add(new RewardItem(new CarrionFlame()));
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this, this, new BlazingPower(this)));
        positionSelf();
        setMoveShortcut(INNERFLAME, MOVES[0]);
    }

    @Override
    protected void populateMoves() {
        this.moves.put(INNERFLAME, new EnemyMoveInfo(INNERFLAME, Intent.BUFF, -1, 0, false));
        this.moves.put(EXPLOSION, new EnemyMoveInfo(EXPLOSION, Intent.ATTACK_DEBUFF, EXPLOSION_DMG * actNum, 0, false));
        this.moves.put(FLAMEWALL, new EnemyMoveInfo(FLAMEWALL, Intent.ATTACK, FW_DMG + actNum, FW_MULTI, true));
    }

    @Override
    public void takeCustomTurn(DamageInfo info, int multiplier) {
        switch (this.nextMove) {
            case INNERFLAME:
                for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if(!m.isDeadOrEscaped() && !m.id.equals(ID)) {
                        addToBot(new ApplyPowerAction(m, this, new InnerFlamePower(m, IF_HPL*actNum, IF_SG*actNum), IF_HPL*actNum));
                    }
                }
                break;
            case EXPLOSION:
                addToBot(new VFXAction(new ShockWaveEffect(this.hb.cX, this.hb.cY, Color.ORANGE, ShockWaveEffect.ShockWaveType.CHAOTIC)));
                addToBot(new VFXAction(new ShockWaveEffect(this.hb.cX, this.hb.cY, Color.FIREBRICK, ShockWaveEffect.ShockWaveType.CHAOTIC)));
                addToBot(new VFXAction(new ShockWaveEffect(this.hb.cX, this.hb.cY, Color.ORANGE, ShockWaveEffect.ShockWaveType.CHAOTIC)));
                addToBot(new VFXAction(new ShockWaveEffect(this.hb.cX, this.hb.cY, Color.SALMON, ShockWaveEffect.ShockWaveType.CHAOTIC)));

                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.FIRE));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, EXPLOSION_VULN, true), EXPLOSION_VULN));
                for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDeadOrEscaped() && !m.id.equals(ID)) {
                        info.applyPowers(this, m);
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, info, AbstractGameAction.AttackEffect.FIRE));
                        addToBot(new ApplyPowerAction(m, this, new VulnerablePower(m, EXPLOSION_VULN, true), EXPLOSION_VULN));
                    }
                }
                break;
            case FLAMEWALL:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new ScreenOnFireEffect(), 1.0F));
                for (int i = 0; i < multiplier; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.FIRE));
                    for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                        if (!m.isDeadOrEscaped() && !m.id.equals(ID)) {
                            info.applyPowers(this, m);
                            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, info, AbstractGameAction.AttackEffect.FIRE));
                        }
                    }
                }
                break;
        }

        turnCounter++;
    }

    @Override
    protected void getMove(int i) {
        if (turnCounter < 1) {
            setMoveShortcut(INNERFLAME, MOVES[0]);
        } else {
            if (turnCounter % 2 == 0) {
                setMoveShortcut(EXPLOSION, MOVES[1]);
            } else {
                setMoveShortcut(FLAMEWALL, MOVES[2]);
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (!this.isDying) {
            this.fireTimer -= Gdx.graphics.getRawDeltaTime();
            if (this.fireTimer < 0.0F) {
                this.fireTimer = FIRE_TIME;
                AbstractDungeon.effectList.add(new CalmFireEffect(hb.cX, hb.cY, Color.ORANGE.cpy()));
                if (MathUtils.randomBoolean(FLAME_CHANCE)) {
                    float distance = MathUtils.random(MAX_OFFSET);
                    float direction = MathUtils.random(MathUtils.PI2);
                    float xOffset = MathUtils.cos(direction) * distance;
                    float yOffset = MathUtils.sin(direction) * distance;

                    AbstractDungeon.effectList.add(new CasualFlameParticleEffect(hb.cX + xOffset, hb.cY + yOffset));
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!this.isDying && !this.isEscaping && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.player.isDead) {
            if (!AbstractDungeon.player.hasRelic(RunicDome.ID) && this.intent != Intent.NONE && !Settings.hideCombatElements) {
                renderIntentVfxBehind(sb);
                renderIntent(sb);
                renderIntentVfxAfter(sb);
                renderDamageRange(sb);
            }
        }

        if (!this.isDead && !this.escaped) {
            this.hb.render(sb);
            this.intentHb.render(sb);
            this.healthHb.render(sb);
        }

        if (!AbstractDungeon.player.isDead) {
            this.renderHealth(sb);
            this.renderName(sb);
        }
    }

    private void positionSelf() {
        float actualX = this.hb.x;
        float actualY = this.hb.y;
        float adjustDistance = 0;
        float adjustAngle = 0;
        float xOffset;
        float yOffset;
        boolean success = false;

        //check if this is a fine position.
        while (!success) {
            success = true;
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (!(monster.isDeadOrEscaped()) && monster != this) {
                    if (overlap(monster.hb, this.hb)) {
                        success = false;

                        adjustAngle = (adjustAngle + 0.1f) % (MathUtils.PI2);
                        adjustDistance += 10.0f;

                        xOffset = MathUtils.cos(adjustAngle) * adjustDistance;
                        yOffset = MathUtils.sin(adjustAngle) * adjustDistance;

                        this.hb.x = actualX + xOffset;
                        this.hb.y = actualY + yOffset;

                        break;
                    }
                }
            }
        }
        this.hb.move(this.hb.x + this.hb.width / 2.0f, this.hb.y + this.hb.height / 2.0f);
        this.hb_x = this.hb.cX - (this.drawX + this.animX);
        this.hb_y = this.hb.cY - (this.drawY + this.animY);
        this.healthHb.move(this.hb.cX, this.hb.cY - this.hb_h / 2.0F - this.healthHb.height / 2.0F);
    }

    private static final float BORDER = 20.0F * Settings.scale;

    private static boolean overlap(Hitbox a, Hitbox b) {
        if (a.x > b.x + (b.width + BORDER) || b.x > a.x + (a.width + BORDER))
            return false;

        return !(a.y > b.y + (b.height + BORDER) || b.y > a.y + (a.height + BORDER));
    }
}
