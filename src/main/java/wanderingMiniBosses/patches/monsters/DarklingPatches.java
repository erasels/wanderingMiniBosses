package wanderingMiniBosses.patches.monsters;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import wanderingMiniBosses.monsters.AbstractWanderingBoss;

@SpirePatch(clz = Darkling.class, method = "damage")
public class DarklingPatches {
    public static ExprEditor Instrument() {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException {
                if (m.getMethodName().equals("die")) {
                    m.replace("{" +
                            "if(!(m instanceof " + AbstractWanderingBoss.class.getName() + ")) {" +
                                "$proceed($$);" +
                            "}" +
                            "}");
                }
            }
        };
    }
}
