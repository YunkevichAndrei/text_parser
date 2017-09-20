package com.example.file_parser;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.*;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class FileParserApplication {

	@Bean
	//объем загружаемого файла
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize("1028KB");
		factory.setMaxRequestSize("1028KB");
		return factory.createMultipartConfig();
	}
	//проверка правильности расстановки скобок
	public static boolean isBrackets(String path) {
		Stack<Character> stack = new Stack<Character>();
		String string = FileWorker.readFile(path);
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == '(' || string.charAt(i) == '{' || string.charAt(i) == '[')
				stack.push(string.charAt(i));
			else if (string.charAt(i) == ')' || string.charAt(i) == '}' || string.charAt(i) == ']')
				if(stack.empty())
					return false;
				else if(((string.charAt(i) == ')' && stack.peek()=='(') || (string.charAt(i) == '}' && stack.peek()=='{')
						|| (string.charAt(i) == ']' && stack.peek()=='[')))
					stack.pop();
				else
					return false;
		}
		if(!stack.empty())
			return false;
		return true;
	}
	//количества повторяющихся слов в тексте
	public static List<Map.Entry<String, Integer>> dictionary(String path){
		String text = FileWorker.readFile(path);    //чтение файла
		String excess = FileWorker.readFile("excess.txt");
		text = text.toLowerCase();                  //преобразование текста к нижнему регистру
		excess = excess.toLowerCase();
		String[] words = text.split("[^a-zA-Zа-яА-Я-]+");  //разбиение текста на массив слов регулярным выражением
		String[] excessWords = excess.split("[^a-zA-Zа-яА-Я-]+");

		ArrayList<String> wordsList = new ArrayList<String>(Arrays.asList(words)); //преобразование массива в список
		ArrayList<String> excessList = new ArrayList<String>(Arrays.asList(excessWords));
		wordsList.removeAll(excessList);    //удаление из списка слов местоимений, союзов и предлогов

		Map<String, Integer> map = new HashMap<String, Integer>();

		for (String word : wordsList) {
			if (!map.containsKey(word))
				map.put(word, 1);
			else
				map.put(word, map.get(word) + 1);
		}
		Set<Map.Entry<String, Integer>> set = map.entrySet();
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(set);
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {      //сортировка
			public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
				return b.getValue().compareTo(a.getValue());
			}
		});
		//System.out.println(list);
		return list;
	}
	//метод преобразующий MultipartFile в File
	public static File convert(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}
	public static void main(String[] args) {
		SpringApplication.run(FileParserApplication.class, args);
	}
}
