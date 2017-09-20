package com.example.file_parser;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {
    @RequestMapping(value = "/analysis", method = RequestMethod.POST, params = {"countTheWords"})
    public @ResponseBody
    String countTheWords(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                String path = FileParserApplication.convert(file).getAbsolutePath();
                int count = 0;
                String str = "";
                for (Map.Entry<String, Integer> entry : FileParserApplication.dictionary(path)) {
                    if (count == 10)
                        break;
                    str += entry.getKey() + "<br>";
                    count++;
                }
                return "Top 10:<br> " + str;
            } catch (Exception e) {
                return "Вам не удалось загрузить => ";
            }
        } else {
            return "Вам не удалось загрузить потому что файл пустой.";
        }
    }

    @RequestMapping(value = "/analysis", method = RequestMethod.POST, params = {"checkBrackets"})
    public @ResponseBody
    String checkBrackets(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                String path = FileParserApplication.convert(file).getAbsolutePath();
                if (FileParserApplication.isBrackets(path))
                    return "correct";
                else
                    return "incorrect";
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            return "Вам не удалось загрузить потому что файл пустой.";

        return "";
    }
}