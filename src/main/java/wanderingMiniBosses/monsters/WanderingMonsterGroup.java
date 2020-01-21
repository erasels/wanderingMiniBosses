package wanderingMiniBosses.monsters;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.*;

public class WanderingMonsterGroup implements CustomSavable<ArrayList<WanderingMonsterGroup.WanderingBossInfo>> {
    protected RewardItem[] rewardsForKillingAll;
    public ArrayList<WanderingBossInfo> monsterInfo;
    public ArrayList<BaseMod.GetMonster> monsters;

    //If this is false, the monstergroup counts as dead for future encounters as soon as 1 enemy from it dies.
    public boolean survivorsStillReturn;

    public WanderingMonsterGroup() {
        this(null);
    }
    public WanderingMonsterGroup(RewardItem[] rewards) {
        this(rewards, true);
    }
    public WanderingMonsterGroup(RewardItem[] rewards, boolean survivorsStillReturn) {
        this.monsterInfo = new ArrayList<>();
        this.monsters = new ArrayList<>();
        this.rewardsForKillingAll = rewards;
        this.survivorsStillReturn = survivorsStillReturn;
    }

    //Makes "this" take on the properties of the passed instance. This is to keep the reference in WanderingBossHelper constant.
    public void setParameters(WanderingMonsterGroup template) {
        rewardsForKillingAll = template.rewardsForKillingAll;
        monsters = template.monsters;
        monsterInfo = new ArrayList<>();
        for(int i = 0; i < template.monsterInfo.size(); i++) {
            WanderingBossInfo wbi = new WanderingBossInfo(template.monsterInfo.get(i).currentHealth);
            for(final Map.Entry<String, Integer> entry : template.monsterInfo.get(i).getAllMagicNumbers()) {
                wbi.setMagicNumber(entry.getKey(), entry.getValue());
            }
            monsterInfo.add(wbi);
        }
    }

    public void add(BaseMod.GetMonster monster) {
        monsterInfo.add(new WanderingBossInfo());
        monsters.add(monster);
    }

    //Instantiate the monsters and return a list of them for spawning in the current room.
    public ArrayList<AbstractWanderingBoss> getLivingMonsters() {
        ArrayList<AbstractWanderingBoss> result = new ArrayList<>();
        for(int i = 0; i < monsters.size(); i++) {
            WanderingBossInfo info = monsterInfo.get(i);
            if(info.currentHealth > 0) {
                AbstractWanderingBoss awb = (AbstractWanderingBoss) monsters.get(i).get();
                if(info.currentHealth != Integer.MAX_VALUE) {
                    awb.currentHealth = info.getCurrentHealth();
                }
                awb.setMonsterInfo(info);

                result.add(awb);
            }
        }
        return result;
    }

    //Iterates through the monster Hps and checks whether the group can still show up based on their behaviour.
    public boolean isAlive() {
        boolean alive = !survivorsStillReturn;
        for(final WanderingBossInfo mo : monsterInfo) {
            if(survivorsStillReturn) {
                if (mo.currentHealth > 0) {
                    alive = true;
                    break;
                }
            } else {
                if (mo.currentHealth <= 0) {
                    alive = false;
                    break;
                }
            }
        }
        return alive;
    }

    //If all pieces of the group are dead, add the rewards.
    public void dispenseReward() {
        if(rewardsForKillingAll != null) {
            AbstractDungeon.getCurrRoom().rewards.addAll(Arrays.asList(rewardsForKillingAll));
            rewardsForKillingAll = null;
        }
    }

    @Override
    public ArrayList<WanderingBossInfo> onSave() {
        return monsterInfo;
    }

    @Override
    public void onLoad(ArrayList<WanderingBossInfo> monsters) {
        this.monsterInfo = monsters;
    }


    class WanderingBossInfo {
        private int currentHealth;
        private Map<String, Integer> magicNumbers;

        public WanderingBossInfo() {
            this(Integer.MAX_VALUE);
        }
        public WanderingBossInfo(int currentHealth) {
            this.currentHealth = currentHealth;
            this.magicNumbers = new HashMap<>();
        }

        public int getCurrentHealth() {
            return this.currentHealth;
        }
        public void setCurrentHealth(int hp) {
            this.currentHealth = hp;
        }
        public void setMagicNumber(String key, int val) {
            this.magicNumbers.put(key, val);
        }
        public int getMagicNumber(String key) {
            return this.magicNumbers.get(key);
        }
        public Set<Map.Entry<String, Integer>> getAllMagicNumbers() {
            return this.magicNumbers.entrySet();
        }
    }
}
