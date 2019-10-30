import java.io.*;

public class ConsoleHelper {
    public static final String GREETING = "Приветствуем!\nПрошу Вас выбрать дальнейшие действия.";
    public static final String GOODBYE = "Окончание программы. Удачного Вам дня!";
    public static final String IO_ERROR = "Ошибка ввода. Попробуйте снова.";
    public static final String MAKE_CHOICE_TOP =
            "-----------------------------------------------------------\n" +
                    "Для получения отчета формы №001 введите в консоль 1\n" +
                    "Для выхода из программы введите в консоль 0\n" +
                    "-----------------------------------------------------------";
    public static final String CHOICE_DISLOCATION =
            "-----------------------------------------------------------\n" +
                    "Укажите в консоль полный путь дирректории куда необходимо поместить файла отчета\n" +
                    "-----------------------------------------------------------";
    public static final String SUCCESS = "Отчет создан в указанной Вами директории";
    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readMessage() throws IOException {
        BufferedReader bufferedReader = null;
        String message = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while (message==null) message = bufferedReader.readLine();
            return message;
        } catch (Exception e) {
            System.out.println(IO_ERROR);
            ConsoleHelper.readMessage();
        }
        return null;
    }
}