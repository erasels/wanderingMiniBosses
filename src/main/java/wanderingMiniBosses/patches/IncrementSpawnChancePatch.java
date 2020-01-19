package wanderingMiniBosses.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CtBehavior;
import wanderingMiniBosses.util.WanderingBossHelper;

@SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = {SaveFile.class})
public class IncrementSpawnChancePatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void patch(AbstractDungeon __instance, SaveFile sf) {
        AbstractRoom r = AbstractDungeon.currMapNode.getRoom();
        if(WanderingBossHelper.viableFloor() && r instanceof MonsterRoom && !(r instanceof MonsterRoomBoss) && !(r instanceof MonsterRoomElite)) {
            WanderingBossHelper.incrementSpawnChance();
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "preBattlePrep");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
