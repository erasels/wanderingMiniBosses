package wanderingMiniBosses.patches;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.actions.CustomSpawnMonsterAction;
import wanderingMiniBosses.monsters.AbstractWanderingBoss;
import wanderingMiniBosses.util.BattleTowersDependencyHelper;
import wanderingMiniBosses.util.WanderingBossHelper;
import wanderingMiniBosses.vfx.combat.AnnouncementEffect;

@SpirePatch(
        clz = GameActionManager.class,
        method = "getNextAction"
)
public class MaybeSpawnDudePatch {
    private static final Logger logger = LogManager.getLogger(MaybeSpawnDudePatch.class.getName());
    private static final int MIN_TURNS = 1;
    private static final int MAX_TURNS = 4;

    private static int turnCounter;

    public static boolean spawningDudeThisFight() {
        return turnCounter >= 0 && WanderingBossHelper.viableFloor() && WanderingBossHelper.nemesisCheck()
                && (!WanderingminibossesMod.hasBT || !BattleTowersDependencyHelper.isBT());
    }

    public static void resetTurnCounter() {
        turnCounter = 0;
    }

    public static void noEncounterThisFight() {
        turnCounter = -1;
    }

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(GameActionManager __instance) {
        logger.info("-------------- Waiting on Dude Spawn? " + (spawningDudeThisFight() ? "Yes" : "No") + "! ---------------");
        if (spawningDudeThisFight()) {
            turnCounter++;
            logger.info("TurnCount: " + turnCounter + ", Chance: " + (((float) turnCounter - MIN_TURNS + 1) / (MAX_TURNS - MIN_TURNS + 1)));
            if (Settings.isDebug || (turnCounter >= MIN_TURNS && (turnCounter >= MAX_TURNS || AbstractDungeon.monsterRng.randomBoolean(((float) turnCounter - MIN_TURNS + 1) / (MAX_TURNS - MIN_TURNS + 1))))) {
                logger.info("Spawning Dude");
                turnCounter = -1;
                WanderingBossHelper.resetSpawnChance();
                for(final AbstractWanderingBoss awb : WanderingBossHelper.getMonster().getLivingMonsters()) {
                    AbstractDungeon.actionManager.addToBottom(new CustomSpawnMonsterAction(awb));
                }
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderLongFlashEffect(Color.WHITE.cpy())));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new AnnouncementEffect(Color.SALMON.cpy(), CustomSpawnMonsterAction.TEXT[0], 5.5f)));
            }
        }

    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "applyStartOfTurnRelics");

            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}