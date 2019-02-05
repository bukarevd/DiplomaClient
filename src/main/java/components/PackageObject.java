package components;

import java.io.*;
import java.util.HashMap;

public class PackageObject extends CommandsObject implements Externalizable {
    private static final long serialVersionUID = 1L;
    private static final int VERSION = 1;
    String name;
    String version;
    String dependence;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getDependence() {
        return dependence;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setDependence(String dependence) {
        this.dependence = dependence;
    }

    public void setValue(HashMap<String, String> ValuesHashMap) {
        setName(ValuesHashMap.get("name"));
        setVersion(ValuesHashMap.get("version"));
        setDependence(ValuesHashMap.get("dependence"));
    }


    public void execute(){
        ExecutorCommand executorCommand = new ExecutorCommand();
        String str;
        if (getVersion() != null)
            str = "sudo -S apt-get install" + getName() + "=" + getVersion();
        else
            str = "sudo -S apt-get install" + getName();
        System.out.println(str);
        String[] command = new String[]{"/bin/sh", "-c", str};
        executorCommand.execute(command);

    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(VERSION);
        out.writeUTF(getName());
        out.writeUTF(getVersion());
        out.writeUTF(getDependence());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int version = in.readInt();
        if (version > VERSION){
            throw new IOException("Unsupport version PackageObject " + version);
        }
        setName(in.readUTF());
        setVersion(in.readUTF());
        setDependence(in.readUTF());
    }
}
