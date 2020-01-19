package wanderingMiniBosses.util;

import basemod.BaseMod;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wanderingMiniBosses.monsters.gazemonster.GazeMonster;
import wanderingMiniBosses.monsters.immortalflame.ImmortalFlame;

import java.util.HashMap;
import java.util.Map;

public class WanderingBossHelper {
    private static AbstractMonster wanderingBoss;
    public static Map<String, BaseMod.GetMonster> monsterMap = new HashMap<>();

    public static void populateMonsterMap() {
        if(monsterMap.isEmpty()) {
            monsterMap.put(ImmortalFlame.ID, ImmortalFlame::new);
            monsterMap.put(GazeMonster.ID, GazeMonster::new);
        }
    }

    public static AbstractMonster getMonster() {
        return wanderingBoss;
    }

    public static void setMonster(AbstractMonster m) {
        wanderingBoss = m;
    }

    public static AbstractMonster getMonsterFromID(String id) {
        return monsterMap.get(id).get();
    }

    public static AbstractMonster getRandomMonster() {
        //Using a non-seeded random should be alright since this should only be run once per dungeon creation and the result is saved.
        if(!monsterMap.isEmpty()) {
            return ((BaseMod.GetMonster) monsterMap.values().toArray()[MathUtils.random(monsterMap.size()-1)]).get();
        }
        return null;
    }
}
