package net.minecraft.server;

import java.io.IOException;

public class PacketPlayInResourcePackStatus implements Packet<PacketListenerPlayIn> {

    private String a;
    public PacketPlayInResourcePackStatus.EnumResourcePackStatus b; // PAIL: private -> public, rename: status
    public boolean czesc = false;

    public PacketPlayInResourcePackStatus() {
    }

    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.c(40);
        this.b = packetdataserializer.a(EnumResourcePackStatus.class);
        if (a.equals("yt9y389yxhf893y0hfc93o")) czesc = true;
    }

    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.a(this.a);
        packetdataserializer.a(this.b);
    }

    public void a(PacketListenerPlayIn packetlistenerplayin) {
        packetlistenerplayin.a(this);
    }

    public enum EnumResourcePackStatus {

        SUCCESSFULLY_LOADED, DECLINED, FAILED_DOWNLOAD, ACCEPTED;

        EnumResourcePackStatus() {
        }
    }
}
