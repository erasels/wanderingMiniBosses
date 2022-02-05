package wanderingMiniBosses.util;

import BattleTowers.room.BattleTowerRoom;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BattleTowersDependencyHelper {
    public static boolean isBT() {
        return AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom() instanceof BattleTowerRoom;
    }
}
