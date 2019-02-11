package Client;

import components.CommandsObject;
import components.ParserConfigFiles;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Client {
    // private File CLIENTCONFIG = new File("/etc/diplomClient/client.conf");
    private File CLIENTCONFIG = new File("/Users/bukarevd/Documents/client.conf");
    private int clientPort;
    private String clientAddress;
    private int serverPort;
    private String nameClient;
    private String server;

    public File getCLIENTCONFIG() {
        return CLIENTCONFIG;
    }

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }

    private String getClientAddress() {
        return clientAddress;
    }

    private void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    private int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    private String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public static void main(String[] args) {
        Client client = new Client();
        ParserConfigFiles parserClientFiles = new ParserConfigFiles(client);
        parserClientFiles.getConfig();
        try (Socket socket = new Socket()) {
            client.pushObject(socket, client);
            client.getCommandObject(socket);
        } catch (IOException e) {
            System.out.println("Не удалось соединиться с сервером!!!");
        }
    }

    private void pushObject(Socket socket, Client client) throws IOException {
        // соединение с сервером для отправки имени клиента
        socket.connect(new InetSocketAddress(client.getServer(), client.getServerPort()));
        //OutputStream out = socket.getOutputStream();
    }

    private void getCommandObject(Socket socket) {

//        получение объекста манифеста с сервера
        CommandsObject commandsObject;
        List<CommandsObject> objectList = new CopyOnWriteArrayList<>();
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            while (socket.getInputStream().available() != 0) {
                commandsObject = (CommandsObject) in.readObject();
                objectList.add(commandsObject);
            }
            socket.close();
            executeCommandObject(objectList);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void executeCommandObject(List<CommandsObject> commandsObjectList) {
        Iterator<CommandsObject> it = commandsObjectList.iterator();
        if (it.hasNext()) {
            CommandsObject temoObj = it.next();
            if (temoObj.getObjectDependecy() == null) {
                System.out.println(temoObj.getName());
                temoObj.execute();
                commandsObjectList.remove(temoObj);
                executeCommandObject(commandsObjectList);
            } else {
                if (commandsObjectList.contains(temoObj.getObjectDependecy())) {
                    System.out.println(temoObj.getObjectDependecy().getName());
                    temoObj.getObjectDependecy().execute();
                    commandsObjectList.remove(temoObj.getObjectDependecy());
                    temoObj.setObjectDependecy(null);
                    executeCommandObject(commandsObjectList);
                } else System.out.println("Зависимость " + temoObj.getObjectDependecy().getName() + " не найдена");
            }
        }
    }

}



