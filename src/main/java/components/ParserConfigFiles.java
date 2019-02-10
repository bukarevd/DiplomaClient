package components;

import Client.Client;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;

public class ParserConfigFiles {
    private DiplomaApp app;

    public ParserConfigFiles(DiplomaApp app) {
        this.app = app;
    }

    public void getConfig() {
        if (app instanceof Client) {
            readClientFile((Client) app);
        }
    }

    private void readClientFile(Client client) {

        File fileConfig = client.getCLIENTCONFIG();
        String configClientString = null;
        if(fileConfig.exists()) {
            configClientString = reader(fileConfig);
            setValue(client, configClientString);
        }
        else {
            System.out.println("Config file not found");
        }


    }

    private String reader(File file) {
        String str = null;
        try (InputStream in = new FileInputStream(file);
             ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                bout.write(buf, 0, len);
                str = new String(bout.toByteArray(), Charset.forName("UTF-8"));
            }
        } catch (IOException e) {
            e.getMessage();
        }
        return str;
    }

    private void setValue(Client client, String string) {
        HashMap<String, String> configClientValues = new HashMap<>();
        String[] configArray = string.split("\n");
        for (String str : configArray) {
            String[] tempString = str.split("=>");
            configClientValues.put(tempString[0], tempString[1]);
        }
        client.setClientPort(Integer.parseInt(configClientValues.get("clientport")));
        client.setNameClient(configClientValues.get("name"));
        client.setServer(configClientValues.get("server"));
        client.setServerPort(Integer.parseInt(configClientValues.get("serverport")));
    }
}
