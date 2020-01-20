package wanderingMiniBosses.monsters.banditking;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.ThieveryPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.monsters.AbstractWanderingBoss;
import wanderingMiniBosses.relics.ThiefScarf;
import wanderingMiniBosses.vfx.general.StealRelicEffect;

import java.util.ArrayList;

public class BanditKing extends AbstractWanderingBoss {
    public static final String ID = WanderingminibossesMod.makeID("Timic");
    private static final float HB_W = 160.0F;
    private static final float HB_H = 260.0F;

    private static int MAX_HEALTH = 222;

    private static final byte PUNCH = 0;
    private static final byte TAKEURSHIT = 1;

    private int turnCounter = 0;

    private static int myGold = 0;
    private static ArrayList<String> relicList = new ArrayList<>();

    public static String funnyNameThing() {
        if (MathUtils.randomBoolean()) {
            return "Bandit Ninja";
        }
        return "Ninja Bandit";
    }

    public BanditKing() {
        this(funnyNameThing(), ID, MAX_HEALTH);
    }

    public BanditKing(String name, String ID, int maxHealth) {
        super(name, ID, maxHealth, 0, 0, HB_W, HB_H, WanderingminibossesMod.makeMonsterPath("Timic.png"), -1200F, 0F);
        rewards.add(new RewardItem(RelicLibrary.getRelic(ThiefScarf.ID).makeCopy()));
    }

    @Override
    public void onEscape() {
        this.addToBot(new VFXAction(new SmokeBombEffect(this.hb.cX, this.hb.cY)));// 34
        super.onEscape();
    }

    public void usePreBattleAction() {
        super.usePreBattleAction();
        setMoveShortcut(PUNCH);
        this.addToBot(new VFXAction(new SmokeBombEffect(this.hb.cX, this.hb.cY)));// 34
        int bruh = 0;
        if (AbstractDungeon.actNum == 1) {
            bruh = 15;
        } else if (AbstractDungeon.actNum == 2) {
            bruh = 25;
        } else if (AbstractDungeon.actNum == 3) {
            bruh = 33;
        }
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, "I've come to take your stuff, see?", 0.5F, 2.0F));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ThieveryPower(this, bruh), bruh));
    }

    @Override
    protected void populateMoves() {
        this.moves.put(PUNCH, new EnemyMoveInfo(PUNCH, Intent.ATTACK, 5, AbstractDungeon.actNum, AbstractDungeon.actNum > 1));
        this.moves.put(TAKEURSHIT, new EnemyMoveInfo(TAKEURSHIT, Intent.STRONG_DEBUFF, -1, 0, false));
    }

    public void takeCustomTurn(DamageInfo info, int amulti) {
        switch (this.nextMove) {
            case 0:
                if (turnCounter == 0)
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(this, "Gimme your wallet!", 0.5F, 2.0F));
                else
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(this, "The gold's mine, see?", 0.5F, 2.0F));
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));// 95
                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {// 96
                    public void update() {
                        myGold = myGold + Math.min(BanditKing.this.getPower(ThieveryPower.POWER_ID).amount, AbstractDungeon.player.gold);// 100
                        this.isDone = true;// 101
                    }// 102
                });
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, this.getPower(ThieveryPower.POWER_ID).amount));// 104 105
                break;
            case 1:
                ArrayList<AbstractRelic> eligibleRelicsList = new ArrayList<>();
                try {
                    for (AbstractRelic r : AbstractDungeon.player.relics) {
                        if (r.getClass().getMethod("onEquip").getDeclaringClass() == AbstractRelic.class && r.getClass().getMethod("onUnequip").getDeclaringClass() == AbstractRelic.class) {
                            eligibleRelicsList.add(r);
                        }
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                if (eligibleRelicsList.isEmpty()) {
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(this, "Crippling Cloud!", 0.5F, 2.0F));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -2), -2));
                } else {
                    AbstractRelic q = eligibleRelicsList.get(AbstractDungeon.cardRandomRng.random(eligibleRelicsList.size() - 1));
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(this, "Hand over the " + q.name + "!", 0.5F, 2.0F));
                    AbstractDungeon.player.loseRelic(q.relicId);
                    AbstractDungeon.effectList.add(new StealRelicEffect(q, this));
                    relicList.add(q.relicId);
                }
                break;
        }

        ++turnCounter;
    }

    protected void getMove(int num) {
        if (turnCounter == 2) {
            setMoveShortcut(TAKEURSHIT);
        } else {
            setMoveShortcut(PUNCH);
        }
    }

    @Override
    public void die() {
        for (String s : relicList) {
            rewards.add(new RewardItem(RelicLibrary.getRelic(s).makeCopy()));
        }
        rewards.add(new RewardItem(myGold, true));
        super.die();
    }
}
