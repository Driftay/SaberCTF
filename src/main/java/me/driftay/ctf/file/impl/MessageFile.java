package me.driftay.ctf.file.impl;

import me.driftay.ctf.SaberCTF;
import me.driftay.ctf.file.CustomFile;
import me.driftay.ctf.util.Message;

public class MessageFile extends CustomFile {

    public MessageFile() {
        super(SaberCTF.instance, "", "messages");
        for (Message message : Message.values()) {
            getConfig().addDefault(message.getConfig(), message.getMessage());
        }
        getConfig().options().copyDefaults(true);
        saveConfig();
    }


    public void init() {
        for (Message message : Message.values()) {
            message.setMessage(getConfig().getString(message.getConfig()));
        }
    }
}
