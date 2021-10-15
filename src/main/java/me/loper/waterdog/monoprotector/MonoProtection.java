package me.loper.waterdog.monoprotector;

import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import me.loper.waterdog.monoprotector.upstream.network.PlayerConnectionHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MonoProtection {

    private final Map<String, PlayerConnectionHandler> connections = new ConcurrentHashMap<>();

    public MonoProtection() {

    }

    public void connect(ProxiedPlayer player) {
        PlayerConnectionHandler connection = new PlayerConnectionHandler(player);

        player.getUpstream().setPacketHandler(connection);
        player.getUpstream().setPacketCodec(connection.getCodec());

        if (null != this.connections.putIfAbsent(player.getName().toLowerCase(), connection)) {
            player.disconnect("§cPlayer with this name already play on the server.");
            return;
        }

        player.getProxy().getScheduler().scheduleAsync(() -> {
            connection.init();
            connection.spawn();
            connection.sendCaptcha();
        });
    }

    public void removePlayer(ProxiedPlayer player) {
        if (null != player) {
            this.connections.remove(player.getName().toLowerCase());
        }
    }
}
