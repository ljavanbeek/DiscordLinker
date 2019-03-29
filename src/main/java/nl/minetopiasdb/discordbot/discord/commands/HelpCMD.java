package nl.minetopiasdb.discordbot.discord.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import nl.minetopiasdb.discordbot.utils.MessageUtils;
import nl.minetopiasdb.discordbot.utils.data.ConfigUtils;

import java.awt.*;

public class HelpCMD extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent e) {
        String msg = e.getMessage().getContentRaw().replaceAll("  ", " ");
        if (msg.startsWith(ConfigUtils.getInstance().getPrefix() + "help")) {
            String prefix = ConfigUtils.getInstance().getPrefix();

            EmbedBuilder embed = MessageUtils.getBuilder(Color.GREEN);


            embed.addField(prefix + "help",
                    "De pagina die jij nu bekijkt", false);
            embed.addField(prefix + "link",
                    "Link jouw Minecraft account met jouw Discord Account.", false);
            embed.addField(prefix + "unlink",
                    "Unlink jouw Minecraft account.", false);
            embed.addField(prefix + "server",
                    "Bekijk informatie over de server.", false);
            embed.addField(prefix + "stats",
                    "Bekijk jouw MinetopiaSDB informatie.", false);

            MessageUtils.sendMessage(e.getChannel(), embed);
        }
    }
}