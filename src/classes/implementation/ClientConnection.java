package classes.implementation;

import classes.interfaces.ClientConnectionInterface;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * Класс реализующий соединение с клиентом
 * @author Lomovskoy Kirill
 * @since 23.03.2019
 */
class ClientConnection implements Runnable, ClientConnectionInterface {

    /** Сокет конкретного соединения с клиентом. */
    public Socket connections;
    
    /** Отсылает сообщения клиенту. */
    public PrintWriter messageFromClient;
    
    /** Получает сообщения от клиента */
    public BufferedReader messageToClient;
    
    /** Строка переданная от клиента */
    public String line = "";
    
    /** Объект Сервера */
    public Server server;
    
    /** Имя для приветствия клиента */
    public String name = "";

    /**
     * Метод соединения с клиентом:
     *  - Устанавливает сокет для соединения
     *  -   Устанавливает поток входящих от клиента сообщений
     *  -   Устанавливает поток исходящих клиенты сообщений
     *  -   Устанавливает новый поток и передаёт в нём серверу этот коннект
     * @param socket - содержит сокет на котором будет работать сервер.
     * @param server -
     * @throws IOException - исключение которое модет выкинуть ServerSocket
     */
    ClientConnection(Socket socket, Server server) throws IOException {

        //Устанавливаем соединение по конкретному сокету
        this.connections = socket;

        //Устанавливаем флаг работы
        this.server = server;

        //Отсылаем сообщение клиенту
        this.messageFromClient = new PrintWriter(new OutputStreamWriter(this.connections.getOutputStream(), StandardCharsets.UTF_8),true);

        //Получаем ответ от клиента
        this.messageToClient = new BufferedReader(new InputStreamReader(connections.getInputStream(), StandardCharsets.UTF_8));

        //Создаём объект нового потока
        Thread threadClient = new Thread(this);

        //Запускаем процесс в новом потоке
        threadClient.start();
    }

    @Override
    public void run() {

        //запускаем сервер в новом потоке
        server.add(this);
    }
}
