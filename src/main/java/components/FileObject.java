package components;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

public class FileObject extends CommandsObject implements Externalizable {
    private static final long serialVersionUID = 1L;
    private static final int VERSION = 1;
    String name;
    String path;
    String content;
    String owner;
    String group;
    String dependence;
    int chmod;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDependence() {
        return dependence;
    }

    public void setDependence(String dependence) {
        this.dependence = dependence;
    }

    public int getChmod() {
        return chmod;
    }

    public void setChmod(int chown) {
        this.chmod = chown;
    }


//    public void setValue(HashMap<String, String> ValuesHashMap) {
//        setName(ValuesHashMap.get("name"));
//        setPath(ValuesHashMap.get("path"));
//        setContent(ValuesHashMap.get("content"));
//        setOwner(ValuesHashMap.get("owner"));
//        setGroup(ValuesHashMap.get("group"));
//        setDependence(ValuesHashMap.get("dependence"));
//        setChmod(Integer.parseInt(ValuesHashMap.get("chown")));
//    }


    public void execute() {
//        дописать проверку на пустоту Getter, установку владельца и прав на файл
        ExecutorCommand executorCommand = new ExecutorCommand();
        String str = "sudo -S echo " + getContent() + " >> " + getPath() + getName();
        System.out.println(str);
        String[] command = new String[]{"/bin/sh", "-c", str};
        executorCommand.execute(command);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(VERSION);
        out.writeUTF(getName());
        out.writeUTF(getPath());
        out.writeUTF(getContent());
        out.writeUTF(getOwner());
        out.writeUTF(getGroup());
       // out.writeUTF(getDependence());
        out.writeInt(getChmod());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int version = in.readInt();
        if (version > VERSION){
            throw new IOException("Unsupport version FileObject");
        }
        setName(in.readUTF());
        setPath(in.readUTF());
        setContent(in.readUTF());
        setOwner(in.readUTF());
        setGroup(in.readUTF());
       // setDependence(in.readUTF());
        setChmod(in.readInt());
    }
}

