# PaperBoatUtils

Paper plugin for support https://github.com/o7Moon/OpenBoatUtils

**Usage**

You can use ```/spawnboat``` to create a configurable boat in your location.

There will be no difference to use the mod on server with this plugin than use mod on both side, but if you do not use this command to create boat, it won't work perfectly correct.

The command ```/spawnboat``` needs permission ```boatutils.boat```, and other mod command need ```boatutils.admin```

**Bugs and Features**

I'm very appreciate for your bugs or features issue, but before you open it in this repository, please make sure it is caused by plugin but not client mod or your server.

**Further Developing**

1. More version support: in order to get rid of disgusting spigot mappings name, this plugin only support paper 1.19.4. If you want support other version, you can open an issue or do it yourself.
```
build.gradle.kts:
    paperweight.paperDevBundle("X.XX.X-R0.1-SNAPSHOT")
    
source codes:
    You will find some mark of mojang name like this:
        // Mojang name: tickBubbleColumn
        Util.invokeSuperPrivateMethod(this, "w", null, null);
    To support other version, you should change the obfuscated name to corresponding version.
```


2. Spigot support: this plugin use ```io.papermc.paper.network.ChannelInitializeListenerHolder``` to handle player packets, if you want to migrate it to spigot server, try to handle it by ```PlayerJoinEvent```.
```
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Channel channel = ((CraftPlayer) e.getPlayer()).getHandle().connection.connection.channel;
        channel.pipeline().addBefore("packet_handler", "boatutils_handler", new PacketManager(channel));
    }
```
