package catserver.server.threads;

import catserver.server.CatServer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLLog;
import org.bukkit.Bukkit;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

public class WatchCatThread extends TimerTask {
    private static Timer timer = new Timer();
    private static long lastTime = 0;
    private static long lastWarnTime = 0;

    @Override
    public void run() {
        long curTime = System.currentTimeMillis();
        if (lastTime > 0 && curTime - lastTime > 2000 && curTime - lastWarnTime > 30000) {
            lastWarnTime = curTime;
            FMLLog.log.debug("------------------------------");
            FMLLog.log.debug("[Cat detection system] main server thread has been stalled" + (curTime - lastTime) + "ms! Your server is deadlocked!");
            FMLLog.log.debug("Current main thread stack trace:");
            for ( StackTraceElement stack : MinecraftServer.getServerInst().primaryThread.getStackTrace() )
            {
                FMLLog.log.debug("\t\t" + stack);
            }
            FMLLog.log.debug("--------------Please note that this is not an error! Do not report this! catserver.yml in check.threadLag shut down----------------");
        }
    }

    public static void update() {
        lastTime = System.currentTimeMillis();
    }

    public static void startThread() {
        if (CatServer.threadLag)
        timer.schedule(new WatchCatThread(), 30 * 1000, 500);
    }

    public static void stopThread() {
        timer.cancel();
    }
}
