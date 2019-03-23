package classes.implementation;

import classes.interfaces.RunServerInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс запуска прилоджения сервер - содержащий метод run
 * @author Lomovskoy Kirill
 * @since 23.03.2019
 */
public class RunServer implements RunServerInterface {

    /** Создаём логер, для записи событий сервера */
    private static final Logger log = Logger.getLogger(RunServer.class.getName());

    /** Порт который слушает сервер по умолчанию */
    private Integer port = 3001;

    /** Условие работы цыкла */
    private volatile Boolean working = true;

    /** Создаём обьект сервера */
    private Server server = new Server();

    /** Имя потока для работы сервера */
    private String nameThread = "work";

    /**
     * Метод запуска работы сервера:
     *  -   проверяет и определяет порт работы сервера
     *  -   если аргумент не указан, слашуется порт по умолчани.
     *  -   создаёт совет лоя работы сервера
     *  -   запускает сервер
     *
     * @param args - массив аргументов, содержит 1н аргумент, который определяет какой порт слушать.
     * @throws IOException - исключение которое модет выкинуть ServerSocket
     * @author Lomovskoy Kirill
     * @since 23.03.2019
     */
    @Override
    public void run(String[] args) throws IOException {

        if (args.length > 0) {                                              //Если порт передан то брать его?
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception e) {                                         // В случае если аргумент всё таки передан
                port = 3001;                                                // но он не содаржит цыфру, выдаём ошибку.
                log.log(Level.INFO, "Порт: {0} неверен", args[0]);
            }
        }

        ServerSocket serverSocket = new ServerSocket(port);                 //Создаём сокет для работы сервера
        log.log(Level.INFO, "Server слушает порт: {0}", port);        //Пишем в лог информацию от сервера

        work(serverSocket);

    }

    /**
     * Класс обеспечивающий работы сервера.
     * @param serverSocket - сокет для работы сервера
     * @throws IOException - исключение которое модет выкинуть ServerSocket
     * @author Lomovskoy Kirill
     * @since 23.03.2019
     */
    private void work(ServerSocket serverSocket) throws IOException {

        //Создаём новый поток для выполнения, передаём туда аргумент роботы
        Thread myWork = new Thread(server, nameThread);

        //Запускаем в новом потоке сервер
        myWork.start();

        //Запускаем цикл
        while (working) {

            //Создаём новое соединение для клиена
            new ClientConnection(serverSocket.accept(), server);
        }
    }

}
