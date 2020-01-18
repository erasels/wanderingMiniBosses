package wanderingMiniBosses.util;

import basemod.BaseMod;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Map;

public class WanderingBossHelper {
    private static AbstractMonster wanderingBoss;
    public static Map<String, BaseMod.GetMonster> monsterMap;

    public static AbstractMonster getMonster() {
        return wanderingBoss;
    }

    public static void setMonster(AbstractMonster m) {
        wanderingBoss = m;
    }

    //TODO: Make actual logic
    public static AbstractMonster getMonsterFromID(String id) {
        return monsterMap.get(id).get();
    }

    //TODO: Make actual logic
    public static AbstractMonster getRandomMonster() {
        //Using a non-seeded random should be alright since this should only be run once per dungeon creation and the result is saved.
        return (AbstractMonster) monsterMap.values().toArray()[MathUtils.random(monsterMap.size()-1)];
    }
}
