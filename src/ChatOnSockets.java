import classes.implementation.RunServer;

/**
 * Класс точка входа в приложение - содержащий главный метод main
 * @author Lomovskoy Kirill
 * @since 23.03.2019
 */
public class ChatOnSockets {

    /**
     * Главный стартовый метод приложения.
     * @param args - массив аргументов, с командной строки.
     * @author Lomovskoy Kirill
     * @since 23.03.2019
     */
    public static void main(String[] args)throws Exception { new RunServer().run(args); }

}
