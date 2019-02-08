package components;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

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
        ExecutorCommand executorCommand = new ExecutorCommand();
        String str;
        if (getVersion() != null)
            str = "sudo -S apt-get install " + getName() + "=" + getVersion();
        else
            str = "sudo -S apt-get install " + getName();
        System.out.println(str);
        String[] command = new String[]{"/bin/sh", "-c", str};
        executorCommand.execute(command);

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
