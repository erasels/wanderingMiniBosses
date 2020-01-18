package wanderingMiniBosses.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wanderingMiniBosses.actions.CustomSpawnMonsterAction;
import wanderingMiniBosses.util.WanderingBossHelper;

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
        return turnCounter >= 0;
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
        if(WanderingBossHelper.isMonsterAlive()) {
            logger.error("-------------- Waiting on Dude Spawn? " + (spawningDudeThisFight() ? "Yes" : "No") + "! ---------------");
            if (spawningDudeThisFight()) {
                turnCounter++;
                logger.error("TurnCount: " + turnCounter + ", Chance: " + (((float) turnCounter - MIN_TURNS + 1) / (MAX_TURNS - MIN_TURNS + 1)));
                if (turnCounter >= MIN_TURNS && (turnCounter >= MAX_TURNS || AbstractDungeon.monsterRng.randomBoolean(((float) turnCounter - MIN_TURNS + 1) / (MAX_TURNS - MIN_TURNS + 1)))) {
                    logger.error("Spawning Dude");
                    turnCounter = -1;

                    AbstractDungeon.actionManager.addToBottom(new CustomSpawnMonsterAction(WanderingBossHelper.getMonster(), false));
                }
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