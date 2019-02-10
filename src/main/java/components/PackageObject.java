package components;

import java.io.*;

public class PackageObject extends CommandsObject implements Externalizable {
    private static final long serialVersionUID = 1L;
    private static final int VERSION = 1;
    private String name = "";
    private String version = "";
    private String dependency = "";
    CommandsObject objectDependecy = null;


    private void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private String getVersion() {
        return version;
    }

    public String getDependency() {
        return dependency;
    }

    private void setVersion(String version) {
        this.version = version;
    }

    private void setDependence(String dependence) {
        this.dependency = dependence;
    }

    public CommandsObject getObjectDependecy() {
        return objectDependecy;
    }

    public void setObjectDependecy(CommandsObject objectDependecy) {
        this.objectDependecy = objectDependecy;
    }

    public void execute() {
        String pm = null;
        String verDel = null;
        switch (getOsType()) {
            case "Ubuntu":
                pm = "apt-get";
                verDel = "=";
                System.out.println(pm);
                break;
            case "CentOS":
                pm = "yum";
                verDel = "-";
                System.out.println(pm);
                break;
        }
        if (!pm.equals("Unknown")) {
            ExecutorCommand executorCommand = new ExecutorCommand();
            String str;
            System.out.println(getVersion());
            if (!getVersion().isEmpty())
                str = "sudo -S " + pm + " install -y " + getName() + verDel + getVersion();
            else
                str = "sudo -S " + pm + " install -y " + getName();
            System.out.println(str);
            String[] command = new String[]{"/bin/sh", "-c", str};
            executorCommand.execute(command);
        }else{
            System.out.println("Unsupport OS");;
        }

    }

    private String getOsType() {
        InputStreamReader stdInput;
        String stdData = null;
        String[] typeOS = new String[]{"/bin/sh", "-c", "cat /etc/*release"};
        try {
            Process process = new ProcessBuilder(typeOS).start();
            stdInput = new InputStreamReader(process.getInputStream());
            int stdBytes;
            char[] stdBuffer = new char[1024];
            while ((stdBytes = stdInput.read(stdBuffer, 0, 1024)) != -1) {
                if (stdBytes == 0) continue;
                stdData = String.valueOf(stdBuffer, 0, stdBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (stdData.contains("Ubuntu")) return "Ubuntu";
        else if (stdData.contains("CentOS")) return "CentOS";
        else return "Unknown";
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(VERSION);
        out.writeUTF(getName());
        out.writeUTF(getVersion());
        out.writeUTF(getDependency());
        out.writeObject(getObjectDependecy());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int version = in.readInt();
        if (version > VERSION) {
            throw new IOException("Unsupport version PackageObject " + version);
        }
        setName(in.readUTF());
        setVersion(in.readUTF());
        setDependence(in.readUTF());
        setObjectDependecy((CommandsObject) in.readObject());
    }
}
