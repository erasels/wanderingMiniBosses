package wanderingMiniBosses.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Bone;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.powers.gazepowers.GazeOne;
import wanderingMiniBosses.powers.gazepowers.GazeThree;
import wanderingMiniBosses.powers.gazepowers.GazeTwo;

public class GazeMonster extends AbstractWanderingBoss {
    public static final String ID = WanderingminibossesMod.makeID("GazeMonster");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    private Bone[] orbs;

    private static final byte LASERSHOWER = 0;
    private static final byte STRENGTHEN = 1;
    private static final byte BOSSBURN = 2;

    public GazeMonster(float offsetX, float offsetY) {
        super(NAME, ID, 400, 0.0F, 10.0F, 280.0F, 280.0F, null, offsetX, offsetY);

        this.loadAnimation(WanderingminibossesMod.makeMonsterPath("gazemonster/skeleton.atlas"), WanderingminibossesMod.makeMonsterPath("gazemonster/skeleton.json"), 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Hit", "Idle", 0.2F);
        this.stateData.setMix("Idle", "Attack", 0.1F);

        orbs = new Bone[3];
        orbs[0] = this.skeleton.findBone("Orb_Down");
        orbs[1] = this.skeleton.findBone("Orb_Left");
        orbs[2] = this.skeleton.findBone("Orb_Right");
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GazeOne(this)));
        if(AbstractDungeon.actNum > 1) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GazeTwo(this)));
            if(AbstractDungeon.actNum > 2) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GazeThree(this)));
            }
        }

        this.moves.clear();
        this.moves.put(LASERSHOWER, new EnemyMoveInfo(LASERSHOWER, Intent.ATTACK, 4 * AbstractDungeon.actNum, 2, true));
        this.moves.put(STRENGTHEN, new EnemyMoveInfo(STRENGTHEN, Intent.BUFF, -1, 0, false));
        this.moves.put(BOSSBURN, new EnemyMoveInfo(BOSSBURN, Intent.STRONG_DEBUFF, -1, 0, false));
    }

    @Override
    public void takeCustomTurn(DamageInfo info, int multiplier) {

    }

    @Override
    protected void getMove(int i) {

    }


    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
