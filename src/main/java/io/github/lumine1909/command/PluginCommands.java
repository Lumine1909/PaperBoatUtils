package io.github.lumine1909.command;

import io.github.lumine1909.Util;
import io.github.lumine1909.entity.OpenBoat;
import net.minecraft.world.entity.vehicle.Boat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class PluginCommands implements TabExecutor {
    public static void registerCommands() {
        PluginCommands commands = new PluginCommands();
        Bukkit.getPluginCommand("spawnboat").setExecutor(commands);
        Bukkit.getPluginCommand("spawnboat").setTabCompleter(commands);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player) || !player.hasPermission("boatutils.boat")) {
            return true;
        }
        Location loc = player.getLocation();
        Boat openBoat = new OpenBoat(((CraftWorld) loc.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ());
        Util.addEntityToWorld(openBoat, loc.getWorld());
        openBoat.getBukkitEntity().teleport(loc);
        player.sendMessage(ChatColor.AQUA + "Successfully spawn a boat to your location!");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
