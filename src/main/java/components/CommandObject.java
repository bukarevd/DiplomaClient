package components;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class CommandObject extends CommandsObject implements Externalizable {
    private static final long serialVersionUID = 1L;
    private static final int VERSION = 1;
    private String name = "";
    private String exec = "";
    private String dependency = "";

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    private String getExec() {
        return exec;
    }

    private void setExec(String exec) {
        this.exec = exec;
    }

    private String getDependency() {
        return dependency;
    }

    private void setDependency(String dependency) {
        this.dependency = dependency;
    }


    public void execute() {
        ExecutorCommand executorCommand = new ExecutorCommand();
        String str = getExec();
        System.out.println(str);
        String[] command = new String[]{"/bin/sh", "-c", str};
        executorCommand.execute(command);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(VERSION);
        out.writeUTF(getName());
        out.writeUTF(getExec());
        out.writeUTF(getDependency());

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        int version = in.readInt();
        if (version > VERSION) {
            throw new IOException("Unsupport version CommandObject");
        }
        setName(in.readUTF());
        setExec(in.readUTF());
        setDependency(in.readUTF());
    }
}
