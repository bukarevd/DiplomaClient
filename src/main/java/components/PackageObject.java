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
    private String dependence = "";

    private void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private String getVersion() {
        return version;
    }

    private String getDependence() {
        return dependence;
    }

    private void setVersion(String version) {
        this.version = version;
    }

    private void setDependence(String dependence) {
        this.dependence = dependence;
    }


    public void execute() {
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
    public void readExternal(ObjectInput in) throws IOException {
        int version = in.readInt();
        if (version > VERSION) {
            throw new IOException("Unsupport version PackageObject " + version);
        }
        setName(in.readUTF());
        setVersion(in.readUTF());
        setDependence(in.readUTF());
    }
}
