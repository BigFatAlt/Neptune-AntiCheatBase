package cc.bigfatman.anticheat.util.reflection.type;

public interface ConstructorInvoker {
    /**
     * Invoke a constructor for a specific class.
     *
     * @param arguments - the arguments to pass to the constructor.
     * @return The constructed object.
     */
    public Object invoke(Object... arguments);
}