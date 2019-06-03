package Parse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Класс для работы с файлами
 */
public class FileIO {

    /**
     * @param fileName имя файла для чтения
     * @return содержимое файла одной строкой
     * @throws IOException файл не найден или не может быть считан
     */
    public static String GetText(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
        return null;
    }

    /**
     * @param fileName имя файла для чтения
     * @return массив из строк файла
     * @throws IOException файл не найден или не может быть считан
     */
    public static String[] GetAllLines(String fileName) throws IOException {
        String str = GetText(fileName);
        return str.split(System.lineSeparator());
    }

    /**
     * @param fileName имя файла для чтения, файле должны быть строки вида: key value
     * @return словарь, содержащийся в файле
     * @throws IOException файл не найден или не может быть считан
     */
    public static HashMap<String, String> GetHashMap(String fileName) throws IOException {
        String[] lines = GetAllLines(fileName);
        HashMap<String, String> map = new HashMap<>();
        for (String line : lines) {
            String[] items = line.split(" ");
            if (items.length == 2)
                map.put(items[0], items[1]);
        }
        return map;
    }

}
