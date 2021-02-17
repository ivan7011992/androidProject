package com.gorvodokanalVer1.meters.net;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpCookie;
import java.util.Date;


public class SerializableCookie implements Serializable {
    private static final long serialVersionUID = 6374381828722046732L;

    private transient final HttpCookie cookie;
    private transient HttpCookie readedCookie;

    public SerializableCookie(HttpCookie cookie) {
        this.cookie = cookie;

    }

    public HttpCookie getCookie() {
        HttpCookie bestCookie = cookie;
        if (readedCookie != null) {
            bestCookie = readedCookie;
        }
        return bestCookie;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(cookie.getName());
        out.writeObject(cookie.getValue());
        out.writeObject(cookie.getComment());
        out.writeObject(cookie.getDomain());
        out.writeObject(cookie.getPath());
        out.writeInt(cookie.getVersion());
       // out.writeBoolean(cookie.isHttpOnly());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String key = (String) in.readObject();
        String value = (String) in.readObject();

        readedCookie = new HttpCookie(key, value);
        readedCookie.setComment((String) in.readObject());
        readedCookie.setDomain((String) in.readObject());
        readedCookie.setPath((String) in.readObject());
        readedCookie.setVersion(in.readInt());
        //readedCookie.setSecure(in.readBoolean());
    }
}