package wanderingMiniBosses.util;

import basemod.BaseMod;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.monsters.eternalPrincess.EternalPrincess;
import wanderingMiniBosses.monsters.gazemonster.GazeMonster;
import wanderingMiniBosses.monsters.immortalflame.ImmortalFlame;
import wanderingMiniBosses.monsters.inkman.InkMan;

import java.util.HashMap;
import java.util.Map;

public class WanderingBossHelper {
    private static final float BASE_CHANCE = 0.08F; //Is actually 15% because it gets incremented at the start of every fight
    private static final float INC_CHANCE = 0.07F;
    private static float spawnChance = BASE_CHANCE;

    private static AbstractMonster wanderingBoss;
    public static Map<String, BaseMod.GetMonster> monsterMap = new HashMap<>();

    public static void populateMonsterMap() {
        if(monsterMap.isEmpty()) {
            //monsterMap.put(ImmortalFlame.ID, ImmortalFlame::new);
            //monsterMap.put(GazeMonster.ID, GazeMonster::new);
            monsterMap.put(EternalPrincess.ID, EternalPrincess::new);
            //monsterMap.put(InkMan.ID, InkMan::new);
        }
    }

    public static AbstractMonster getMonster() {
        return wanderingBoss;
    }

    public static void setMonster(AbstractMonster m) {
        wanderingBoss = m;
    }

    public static boolean isMonsterAlive() {
        return wanderingBoss.currentHealth > 0;
    }

    public static AbstractMonster getMonsterFromID(String id) {
        return monsterMap.get(id).get();
    }

    public static AbstractMonster getRandomMonster() {
        if(!monsterMap.isEmpty()) {
            AbstractMonster tmp = ((BaseMod.GetMonster) monsterMap.values().toArray()[AbstractDungeon.monsterRng.random(monsterMap.size()-1)]).get();
            WanderingminibossesMod.logger.info("Nemesis for this run: " + tmp.name);
            return tmp;
        }
        return null;
    }

    public static float getSpawnChance() {
        return spawnChance;
    }

    public static void resetSpawnChance() {
        spawnChance = BASE_CHANCE;
    }

    public static void incrementSpawnChance() {
        spawnChance += INC_CHANCE;
    }

    public static void setSpawnChance(float newChance) {
        spawnChance = newChance;
    }

    public static boolean viableFloor() {
        return AbstractDungeon.floorNum > 1;
    }
}
