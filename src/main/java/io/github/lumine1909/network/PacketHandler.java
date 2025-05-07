package io.github.lumine1909.network;

import io.github.lumine1909.Util;
import io.github.lumine1909.messageutil.api.MessageReceiver;
import io.github.lumine1909.messageutil.object.PacketContext;
import io.github.lumine1909.messageutil.object.PacketEvent;
import io.github.lumine1909.messageutil.util.ProtocolUtil;
import net.minecraft.network.FriendlyByteBuf;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;

import static io.github.lumine1909.PaperBoatUtils.MOD_ID;
import static io.github.lumine1909.PaperBoatUtils.plugin;

public class PacketHandler extends MessageReceiver {

    public static final Set<String> ACTIVE_PLAYERS = new HashSet<>();

    public static void syncPacket(FriendlyByteBuf buf) {
        for (String playerName : ACTIVE_PLAYERS) {
            ProtocolUtil.send(playerName, MOD_ID, buf);
        }
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Bytebuf(key = MOD_ID)
    public void handleModMessage(PacketContext context, PacketEvent event, FriendlyByteBuf buf) {
        int version = ServerboundPackets.handleVersionPacket(buf);
        if (version == -1) {
            return;
        }
        ACTIVE_PLAYERS.add(context.name().orElseThrow());
        event.setCancelled(true);
    }

    public static class PlayerListener implements Listener {

        @EventHandler
        public void onChangeWorld(PlayerChangedWorldEvent e) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                String playerName = e.getPlayer().getName();
                Util.sendSettings(playerName);
            });
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent e) {
            ACTIVE_PLAYERS.remove(e.getPlayer().getName());
        }
    }
}