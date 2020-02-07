package wanderingMiniBosses.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.util.WanderingBossHelper;

public class NemesisListCommand extends ConsoleCommand {
    public NemesisListCommand() {
        maxExtraTokens = 0;
        minExtraTokens = 0;
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        WanderingminibossesMod.logger.info("Dumping Wandering Miniboss IDs.");
        for(final String id : WanderingBossHelper.monsterMap.keySet()) {
            WanderingminibossesMod.logger.info(WanderingBossHelper.getIdWithoutModId(id));
        }
        DevConsole.log("Dumping IDs in ModTheSpire window.");
    }
}