package classes.interfaces;

import java.io.IOException;

public interface RunServerInterface {

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
     void run(String[] args) throws IOException;

}
