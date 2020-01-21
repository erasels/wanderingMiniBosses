package wanderingMiniBosses.util;

import basemod.BaseMod;
import com.megacrit.cardcrawl.rewards.RewardItem;
import wanderingMiniBosses.monsters.WanderingMonsterGroup;

import java.util.HashMap;

public class WanderingMonsterMap extends HashMap<String, WanderingMonsterGroup> {
    public void put(String ID, BaseMod.GetMonster... monsters) {
        this.put(ID, new RewardItem[0], monsters);
    }
    public void put(String ID, RewardItem[] rewardsForKillingAll, BaseMod.GetMonster... monsters) {
        WanderingMonsterGroup rmp = new WanderingMonsterGroup(rewardsForKillingAll);
        for(final BaseMod.GetMonster mo : monsters) {
            rmp.add(mo);
        }
        this.put(ID, rmp);
    }
}
