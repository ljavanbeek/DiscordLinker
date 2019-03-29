package nl.minetopiasdb.discordbot.discord.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import nl.minetopiasdb.discordbot.utils.MessageUtils;
import nl.minetopiasdb.discordbot.utils.data.ConfigUtils;
import nl.minetopiasdb.discordbot.utils.link.DataLinkUtils;
import nl.minetopiasdb.discordbot.utils.link.LinkUtils;

import java.awt.*;
import java.util.UUID;

public class DiscordLinkCMD extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getAuthor().isBot()) return;

        String msg = e.getMessage().getContentRaw().replaceAll("  ", " ");
        if (msg.startsWith(ConfigUtils.getInstance().getPrefix() + "link")) {
            long id = e.getAuthor().getIdLong();
            if (DataLinkUtils.getInstance().isLinked(id)) {
                MessageUtils.sendMessage(e.getChannel(),
                        MessageUtils.getBuilder("Je hebt jouw Discord Account al gelinked!", Color.RED));
                return;
            }
            if (LinkUtils.getInstance().isValidLink(id)) {
                LinkUtils.getInstance().removeLink(id);
            }
            UUID linkUUID = LinkUtils.getInstance().registerLink(id);
            if (!MessageUtils.sendPrivateAndCheckIfCanReceive(e.getAuthor(),
                    MessageUtils
                            .getBuilder("Type het volgende commando in Minecraft om jouw account te linken: ```/link "
                                    + linkUUID.toString() + "```", Color.GREEN))) {
                LinkUtils.getInstance().removeLink(linkUUID);
                MessageUtils.sendMessage(e.getChannel(), MessageUtils.getBuilder(
                        e.getAuthor().getAsMention() + ", je moet je priveberichten aan hebben staan!", Color.RED));
            } else {
                e.getMessage().delete().queue();
            }

        } else if (msg.startsWith(ConfigUtils.getInstance().getPrefix() + "unlink")) {
            long id = e.getAuthor().getIdLong();
            if (!DataLinkUtils.getInstance().isLinked(id)) {
                MessageUtils.sendMessage(e.getChannel(), MessageUtils.getBuilder(
                        e.getAuthor().getAsMention() + ", je hebt jouw discord account niet gelinked!", Color.RED));
            } else {
                LinkUtils.getInstance().removeLink(id);
                DataLinkUtils.getInstance().removeLink(id);
                MessageUtils.sendMessage(e.getChannel(), MessageUtils.getBuilder(
                        e.getAuthor().getAsMention() + ", succesvol jouw Minecraft account unlinked!", Color.GREEN));
            }

        }

    }


}