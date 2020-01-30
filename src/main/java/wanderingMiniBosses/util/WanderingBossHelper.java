package wanderingMiniBosses.util;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.monsters.AbstractWanderingBoss;
import wanderingMiniBosses.monsters.WanderingMonsterGroup;
import wanderingMiniBosses.monsters.banditking.BanditKing;
import wanderingMiniBosses.monsters.eternalPrincess.EternalPrincess;
import wanderingMiniBosses.monsters.gazemonster.GazeMonster;
import wanderingMiniBosses.monsters.immortalflame.ImmortalFlame;
import wanderingMiniBosses.monsters.inkman.InkMan;
import wanderingMiniBosses.monsters.gremlinknight.GremlinKnight;
import wanderingMiniBosses.monsters.thiefOfABillion.ThiefOfABillionGuards;
import wanderingMiniBosses.monsters.timic.Timic;

import java.util.ArrayList;
import java.util.Map;

public class WanderingBossHelper {

    private static final float BASE_CHANCE = 0.08F; //Is actually 15% because it gets incremented at the start of every fight
    private static final float INC_CHANCE = 0.07F;
    private static float spawnChance = BASE_CHANCE;

    private static final float NEMESIS_CHANCE = 0.4f;
    public static boolean HAS_NEMESIS;


    private static WanderingMonsterGroup wanderingBoss = new WanderingMonsterGroup();
    public static WanderingMonsterMap monsterMap = new WanderingMonsterMap();
    private static String currentMonsterID = "";

    public static void populateMonsterMap() {
    	monsterMap.put(ThiefOfABillionGuards.ID, ThiefOfABillionGuards::new);
        if (monsterMap.isEmpty()) {
            monsterMap.put(ImmortalFlame.ID, ImmortalFlame::new);
            monsterMap.put(GazeMonster.ID, GazeMonster::new);
            monsterMap.put(EternalPrincess.ID, EternalPrincess::new);
            monsterMap.put(InkMan.ID, InkMan::new);
            monsterMap.put(BanditKing.ID, BanditKing::new);
            monsterMap.put(ThiefOfABillionGuards.ID, ThiefOfABillionGuards::new);
            monsterMap.put(Timic.ID, Timic::new);
            monsterMap.put(GremlinKnight.ID, GremlinKnight::new);
        }
    }

    public static WanderingMonsterGroup getMonster() {
        return wanderingBoss;
    }

    public static void setMonster(WanderingMonsterGroup m) {
        wanderingBoss.setParameters(m);
    }

    public static boolean isMonsterAlive() {
        return wanderingBoss.isAlive();
    }

    public static ArrayList<AbstractWanderingBoss> getMonsterFromID(String id) {
        return monsterMap.get(id).getLivingMonsters();
    }
    public static void setCurrentMonsterID(String ID) {
        currentMonsterID = ID;
        if(monsterMap.containsKey(ID)) {
            wanderingBoss.setParameters(monsterMap.get(ID));
        }
    }
    public static String getCurrentMonsterID() {
        return currentMonsterID;
    }

    public static WanderingMonsterGroup getRandomMonster() {
        if (!monsterMap.isEmpty()) {
            int index = AbstractDungeon.monsterRng.random(monsterMap.size() - 1);
            Object[] entryset = monsterMap.entrySet().toArray();
            WanderingminibossesMod.logger.info(entryset[index]);
            Map.Entry<String, WanderingMonsterGroup> entry = (Map.Entry<String, WanderingMonsterGroup>) entryset[index];
            WanderingminibossesMod.logger.info("Nemesis for this run: " + (currentMonsterID = entry.getKey()));
            return entry.getValue();
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
        return Settings.isDebug || AbstractDungeon.floorNum > 1;
    }

    public static void nemesisDetermination() {
        HAS_NEMESIS =  AbstractDungeon.monsterRng.randomBoolean(NEMESIS_CHANCE);
        WanderingminibossesMod.logger.info("Will there be a Nemesis this run: " + nemesisCheck());
    }

    public static boolean nemesisCheck() {
        return HAS_NEMESIS || WanderingminibossesMod.permaNemesis();
    }

    //Dispense rewards if either all are dead or 1 is dead and the rest don't reappear when one dies.
    public static void checkForRewardDispensal() {
        if(!wanderingBoss.survivorsStillReturn || !isMonsterAlive()) {
            wanderingBoss.dispenseReward();
        }
    }
}
