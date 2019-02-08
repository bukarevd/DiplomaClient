package components;


abstract public class CommandsObject {
    public abstract void execute();

    public abstract CommandsObject getObjectDependecy();
    public abstract void setObjectDependecy(CommandsObject objectDependecy);
}
