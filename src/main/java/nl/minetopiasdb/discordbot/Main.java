package nl.minetopiasdb.discordbot;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import nl.minetopiasdb.discordbot.bukkit.commands.LinkCMD;
import nl.minetopiasdb.discordbot.bukkit.listeners.JoinListener;
import nl.minetopiasdb.discordbot.discord.commands.DiscordLinkCMD;
import nl.minetopiasdb.discordbot.discord.commands.HelpCMD;
import nl.minetopiasdb.discordbot.discord.commands.ServerinfoCMD;
import nl.minetopiasdb.discordbot.discord.commands.StatCMD;
import nl.minetopiasdb.discordbot.utils.UpdateChecker;
import nl.minetopiasdb.discordbot.utils.data.ConfigUtils;
import nl.minetopiasdb.discordbot.utils.data.UserData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Plugin plugin;
    private static JDA bot;

    public static JDA getBot() {
        return bot;
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static void updatePresence() {
        getBot().getPresence().setStatus(OnlineStatus.valueOf(ConfigUtils.getInstance().getStatus().toUpperCase()));
        switch (ConfigUtils.getInstance().getPlayingType().toLowerCase()) {
            case "playing":
                getBot().getPresence().setGame(Game.playing(ConfigUtils.getInstance().getPlayingMessage().replaceAll("<Spelers>", "" + Bukkit.getOnlinePlayers().size())));
            case "watching":
                getBot().getPresence().setGame(Game.watching(ConfigUtils.getInstance().getPlayingMessage().replaceAll("<Spelers>", "" + Bukkit.getOnlinePlayers().size())));
            case "listening":
                getBot().getPresence().setGame(Game.listening(ConfigUtils.getInstance().getPlayingMessage().replaceAll("<Spelers>", "" + Bukkit.getOnlinePlayers().size())));
            case "streaming":
                getBot().getPresence().setGame(Game.streaming(ConfigUtils.getInstance().getPlayingMessage().replaceAll("<Spelers>", "" + Bukkit.getOnlinePlayers().size()), null));
        }
    }

    public void onEnable() {
        plugin = this;
        if (!Bukkit.getPluginManager().isPluginEnabled("MinetopiaSDB")) {
            Bukkit.getPluginManager().registerEvents(new Listener() {
                @EventHandler
                public void onJoin(PlayerJoinEvent e) {
                    if (e.getPlayer().isOp()) {
                        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> e.getPlayer().sendMessage(ChatColor.RED
                                + "DiscordLinker >> MinetopiaSDB is vereist om DiscordLinker te laten werken!"), 5 * 20L);
                    }
                }
            }, Main.plugin);
        }
        ConfigUtils.getInstance().addDefault(plugin);
        UserData.getInstance().setup(plugin);
        loginBot();
        getCommand("link").setExecutor(new LinkCMD());
        Bukkit.getPluginManager().registerEvents(new UpdateChecker(), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
    }

    private void loginBot() {
        JDABuilder client = new JDABuilder();
        String token = ConfigUtils.getInstance().getBotToken();
        try {
            client.setToken(token);
            client.addEventListener(new DiscordLinkCMD());
            client.addEventListener(new HelpCMD());
            client.addEventListener(new ServerinfoCMD());
            client.addEventListener(new StatCMD());
            bot = client.build();
        } catch (Exception ex) {
            if (ex.getMessage().contains("The provided token is invalid")) {
                Bukkit.getPluginManager().registerEvents(new Listener() {
                    @EventHandler
                    public void onJoin(final PlayerJoinEvent e) {
                        if (e.getPlayer().isOp()) {
                            Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
                                e.getPlayer().sendMessage(ChatColor.RED
                                        + "DiscordLinker >> Geen geldige discordtoken ingesteld. De plugin doet nu niets!");
                                e.getPlayer().sendMessage(ChatColor.RED
                                        + "Voor info: https://projects.minetopiasdb.nl/discordlinker");
                            }, 5 * 20L);
                        }
                    }
                }, Main.plugin);
                Main.plugin.getLogger().warning("GEEN GELDIGE DISCORDTOKEN INGESTELD!");
                return;
            }
            ex.printStackTrace();
        }
        try {
            bot.awaitReady();
            updatePresence();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void onDisable() {
        bot.shutdownNow();
        UserData.getInstance().saveData();
    }
}