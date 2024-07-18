package io.github.lumine1909.network;

import io.github.lumine1909.PaperBoatUtils;
import io.github.lumine1909.Util;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.papermc.paper.network.ChannelInitializeListenerHolder;
import net.kyori.adventure.key.Key;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;

public class PacketHandler {
    static class PlayerListener implements Listener {
        /*
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent e) {
            Channel channel = ((CraftPlayer) e.getPlayer()).getHandle().connection.connection.channel;
            channel.pipeline().addBefore("packet_handler", "boatutil_handler", new PacketManager(e.getPlayer()));
        }

         */
        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent e) {
            Channel channel = ((CraftPlayer) e.getPlayer()).getHandle().connection.connection.channel;
            modedChannels.remove(channel);
        }

        @EventHandler
        public void onChangeWorld(PlayerChangedWorldEvent e) {
            Bukkit.getScheduler().runTask(PaperBoatUtils.instance, () -> {
                Channel channel = ((CraftPlayer) e.getPlayer()).getHandle().connection.connection.channel;
                Util.sendSettings(channel);
            });
        }
    }

    static class PacketManager extends ChannelDuplexHandler {
        private final Channel channel;

        public PacketManager(Channel channel) {
            this.channel = channel;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (!(msg instanceof ServerboundCustomPayloadPacket packet)) {
                super.channelRead(ctx, msg);
                return;
            }
            if (packet.getIdentifier().equals(PaperBoatUtils.modKey)) {
                int version = ServerboundPackets.handleVersionPacket(packet.getData());
                if (version == -1) {
                    //PaperBoatUtils.instance.getLogger().warning("Failed to handle version packet of a player ");
                    return;
                }
                //PaperBoatUtils.instance.getLogger().info("Player joined with version " + version);
                modedChannels.add(channel);
                Util.sendSettings(channel);
            }
        }
    }

    private static final Set<Channel> modedChannels = new HashSet<>();

    public static void init() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), PaperBoatUtils.instance);
        ChannelInitializeListenerHolder.addListener(Key.key("boatutils:packet_handler"), channel -> {
            channel.pipeline().addBefore("packet_handler", "boatutils_handler", new PacketManager(channel));
        });
    }

    public static void syncPacket(ClientboundCustomPayloadPacket packet) {
        for (Channel channel : modedChannels) {
            channel.writeAndFlush(packet);
        }
    }
}