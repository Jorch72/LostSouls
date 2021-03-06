package mcjty.lostsouls;

import mcjty.lostcities.api.ILostCities;
import mcjty.lostsouls.commands.CommandDebug;
import mcjty.lostsouls.commands.CommandSetHaunt;
import mcjty.lostsouls.data.LostSoulData;
import mcjty.lostsouls.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.function.Function;

@Mod(modid = LostSouls.MODID, name = "Lost Souls",
        dependencies =
                "required-after:lostcities@[" + LostSouls.LOSTCITY_VERSION + ",);" +
                        "after:forge@[" + LostSouls.MIN_FORGE11_VER + ",)",
        version = LostSouls.VERSION,
        acceptedMinecraftVersions = "[1.12,1.13)",
        acceptableRemoteVersions = "*")
public class LostSouls {
    public static final String MODID = "lostsouls";
    public static final String VERSION = "1.1.4";
    public static final String LOSTCITY_VERSION = "2.0.3";
    public static final String MIN_FORGE11_VER = "13.19.0.2176";

    @SidedProxy(clientSide = "mcjty.lostsouls.proxy.ClientProxy", serverSide = "mcjty.lostsouls.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance("lostsouls")
    public static LostSouls instance;

    public static ILostCities lostCities;

    public static Logger logger;

    /**
     * Run before anything else. Read your config, create blocks, items, etc, and
     * register them with the GameRegistry.
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        logger = e.getModLog();
        this.proxy.preInit(e);

        FMLInterModComms.sendFunctionMessage("lostcities", "getLostCities", "mcjty.lostsouls.LostSouls$GetLostCities");
    }

    /**
     * Do your mod setup. Build whatever data structures you care about. Register recipes.
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        this.proxy.init(e);
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandDebug());
        event.registerServerCommand(new CommandSetHaunt());
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        LostSoulData.clearInstance();
    }

    /**
     * Handle interaction with other mods, complete your setup based on this.
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        this.proxy.postInit(e);
    }

    public static class GetLostCities implements Function<ILostCities, Void> {
        @Nullable
        @Override
        public Void apply(ILostCities lc) {
            lostCities = lc;
            return null;
        }
    }

}
