package wanderingMiniBosses.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@SpirePatch(clz = AbstractDungeon.class, method = SpirePatch.CLASS)
public class DungeonMonsterFieldPatch {
    public static SpireField<AbstractMonster> dungeonMiniboss = new SpireField<>(() -> null);
}
