package Client;

import components.*;

import java.io.*;
import java.net.*;
import java.util.Enumeration;

public class Client extends DiplomaApp {
    private File CLIENTCONFIG = new File("/Users/bukarevd/Documents/client.conf");
    private int clientPort;
    private String clientAddress;
    private int serverPort;
    private String nameClient;
    private String server;
    private String shell;

    public File getCLIENTCONFIG() {
        return CLIENTCONFIG;
    }

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
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

    public String getShell() {
        return shell;
    }

    public void setShell(String shell) {
        this.shell = shell;
    }

    public static void main(String[] args) {
        Client client = new Client();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (!iface.isUp() || !iface.isLoopback()) continue;
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet6Address) continue;
                    client.setClientAddress(addr.getHostAddress());
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        System.out.println(client.getClientAddress());
        ParserConfigFiles parserClientFiles = new ParserConfigFiles(client);
        parserClientFiles.getConfig();
        try (Socket socket = new Socket()) {
            client.pushObject(socket, client);
            client.getCommandObject(socket);
        } catch (IOException e) {
            System.out.println("Не удалось соединиться с сервером!!!");
        }
        //client.start();
    }

    private void pushObject(Socket socket, Client client) throws IOException {
// соединение с сервером
        socket.connect(new InetSocketAddress(client.getServer(), client.getServerPort()));
        OutputStream out = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
    }

    private void getCommandObject(Socket socket) {

//        получение объекста манифеста с сервера
        CommandsObject commandsObject = null;
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
           while (socket.getInputStream().available() != 0) {
               commandsObject = (CommandsObject) in.readObject();
                if (commandsObject instanceof FileObject) {
                    System.out.println(((FileObject) commandsObject).getName());
                    ((FileObject) commandsObject).execute();
                }
                if (commandsObject instanceof CommandObject) {
                    System.out.println(((CommandObject) commandsObject).getName());
                    ((CommandObject) commandsObject).execute();
                }
                if (commandsObject instanceof PackageObject) {
                    System.out.println(((PackageObject) commandsObject).getName());
                    ((PackageObject) commandsObject).execute();
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
