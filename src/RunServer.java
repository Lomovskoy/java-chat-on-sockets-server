import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс запуска прилоджения сервер - содержащий главные метод main
 * @author Lomovskoy Kirill
 * @since 23.03.2019
 */
public class RunServer {

    /** Создаём логер, для записи событий сервера */
    private static final Logger log = Logger.getLogger(RunServer.class.getName());

    static Integer port = 3001;

    /**
     * Главный стартовый метод приложения.
     *
     * @param args - массив аргументов, содержит 1н аргумент, который определяет какой порт слушать.
     *             Если аргумент не указан, то есть массив пустой, слашуется порт по умолчани.
     *
     * @author Lomovskoy Kirill
     * @since 23.03.2019
     */
    public static void main(String[] args) {

        //Если порт передан то брать его?
        if(args.length > 0) try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            port = 3001;
            log.log(Level.INFO, "порт: {0} неверен", args[0]);
        }

        System.out.println("Hello World!");
    }

}
