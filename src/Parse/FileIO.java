package Parse;

import java.io.*;
import java.util.ArrayList;
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


    public static void WriteLine(String fileName, String line, boolean newLine) throws IOException {
        File fout = new File(fileName);
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write(line);
        if (newLine)
            bw.newLine();
        bw.close();
    }

    public static void WriteAllLines(String fileName, ArrayList<String> lines, boolean newLine) throws IOException {
        File fout = new File(fileName);
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        for (int i = 0; i < lines.size(); i++) {
            bw.write(lines.get(i));
            if (newLine)
                bw.newLine();
        }

        bw.close();
    }

}
