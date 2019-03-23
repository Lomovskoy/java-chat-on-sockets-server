package classes.implementation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс реализующий лолгику сервера
 * @author Lomovskoy Kirill
 * @since 23.03.2019
 */
public class Server implements Runnable {

    //Создаём лист из пользователей
    volatile List<ClientConnection> users = new ArrayList<ClientConnection>();

    //Флег запуска работы сервера
    boolean running = true;

    //Создаём логгер
    static final Logger LOG = Logger.getLogger(Server.class.getName());

    //Если сообщение
    final char message = '0';

    //Если список пользователей
    final char list = '1';

    String usersToString = "";

    /**
     * Метод добавления пользвателя в лист юзеров
     * @param user
     */
    void add(ClientConnection user) {
        //Синхронизируем работу потоков с этим листом
        synchronized (users) {
            //Добавляем в лист нового пользователя
            users.add(user);
        }
    }

    /**
     * Метод удаления пользователя из списка коннекторов
     * @param user
     */
    void remove(ClientConnection user) {
        //Синхронизируем работу потоков с этим листом
        synchronized (users) {
            //Удаляем из листа нового пользователя
            users.remove(user);
            usersToString = usersToString.replace(user.name + "|", "");
            //Отправляем пользователя код, для завершения клиентской программы
            user.messageFromClient.println("by-by");

        }
    }

    /**
     * Метод отправляющий всем пользователем в чат
     * @param text
     */
    void sendToAll(String text) {
        //Перебор всех пользователей из листа
        for (ClientConnection user : users) {
            //Отправление текущему пользователю
            user.messageFromClient.println(text);
        }

    }

    @Override
    public void run(){
        try {
            do {
                //Синхнхринизируем потомк
                synchronized (users) {
                    List<ClientConnection> userDell = new ArrayList<>();
                    //Перебираем лист из пользователей
                    for (ClientConnection user : users) {
                        //Вывродит имя потока, и работает или нет
                        //LOG.log(Level.INFO, "{0}  = {1}", new Object[]{Thread.currentThread().getName(), Thread.currentThread().isAlive()});
                        //Если текущийц пользователь чтото написал
                        if (user.messageToClient.ready()) {
                            //Испольнять цикл пока в сокете есть сообщения
                            while (user.messageToClient.ready()) {
                                //Создаём символ и читаем его из сокета
                                char c = (char) user.messageToClient.read();
                                //Если символ = переноса строки
                                if (c == '\n') {
                                    //Если имя пользователя пустое
                                    if ("".equals(user.name)) {
                                        //берём имя пользователя из входящего сообщения
                                        user.name = user.line.trim();
                                        //Обнуляем имя пользователя
                                        user.line = "";
                                        //Отправить всем
                                        sendToAll(message + "В чат вошёл: " + user.name);
                                        usersToString += user.name + "|";
                                        sendToAll(list + usersToString);
                                        //Отправляем текущему пользователю
                                        //user.messageFromClient.println("Для выхода набери \"stop\"");
                                    } //Если имя пользователя не пустое
                                    else {
                                        System.out.println(user.line);
                                        //Если пришло стоп
                                        if ("stop".equals(user.line)) {
                                            //Выводим имя потока и сообщение от пользователя
                                            LOG.log(Level.INFO, "{0} line = {1}", new Object[]{Thread.currentThread().getName(), user.line});
                                            //Отправляем сообщение клиенту, чдля завершения работы сервера
                                            user.messageFromClient.println(message + "by-by!");
                                            //Добавляем пользователя в список для удаления
                                            userDell.add(user);
                                            //Отправить всем сообщение
                                            sendToAll(message + user.name + " покинул нас!");
                                        } //Если не пришло stop
                                        else {
                                            //Отправить всем сообщение пользователя
                                            sendToAll(message + user.name + ": " + user.line.trim());
                                        }
                                        //Очищаем сообщение юзера
                                        user.line = "";
                                    }
                                } //Если символ != переносу строки
                                else {
                                    if (c != '\r') {
                                        //прибавляем символ к строке сообщение пользователя
                                        user.line += c;
                                    }
                                }
                            }
                        }
                        //Если лист пользователей пустой
                        if (users.isEmpty()) {
                            //Выходим из цыкла пользвателя
                            break;
                        }
                    }
                    //Если список пользователей для удаления непустой
                    if (userDell != null) {
                        for (ClientConnection user : userDell) {
                            //Удаляем этого пользователя из списка
                            this.remove(user);
                            sendToAll(list + usersToString);
                        }
                    }

                }
            } while (running);
        } catch (IOException ex) {

            LOG.log(Level.SEVERE, "Server error: {0}", ex);
        }
    }

}
