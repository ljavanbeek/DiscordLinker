package nl.minetopiasdb.discordbot.discord.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import nl.minetopiasdb.discordbot.utils.MessageUtils;
import nl.minetopiasdb.discordbot.utils.data.ConfigUtils;
import org.bukkit.Bukkit;

import java.awt.*;

public class ServerinfoCMD extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent e) {
        String msg = e.getMessage().getContentRaw().replaceAll("  ", " ");
        if (msg.startsWith(ConfigUtils.getInstance().getPrefix() + "server")) {
            String ip = ConfigUtils.getInstance().getIP();

            EmbedBuilder embed = MessageUtils.getBuilder(Color.GREEN);

            embed.addField("IP", ip, false);
            embed.addField("Spelers", Bukkit.getOnlinePlayers().size() + " / " + Bukkit.getMaxPlayers(), false);
            MessageUtils.sendMessage(e.getChannel(), embed);
        }
    }
}