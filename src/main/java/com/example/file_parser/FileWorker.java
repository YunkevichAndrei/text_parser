package com.example.file_parser;

import java.io.*;

public class FileWorker {
    //чтение из файла
    public static String  readFile(String fileName) {
        StringBuilder out = new StringBuilder();
        char buf[] = new char[1024];
        try {
            InputStream inputStream = new FileInputStream(fileName);
            Reader reader = new InputStreamReader(inputStream,"Cp1251");

            for (int i = reader.read(buf); i >= 0; i = reader.read(buf)) {
                out.append(buf, 0, i);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Вы указали неверный путь к файлу");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = out.toString();
        return result;
    }
}
