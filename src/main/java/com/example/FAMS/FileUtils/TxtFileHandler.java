package com.example.FAMS.FileUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;

public class TxtFileHandler {

    private static final String filePath = "src/main/java/com/example/FAMS/Files/MailFormat.txt";

    public static String readMailFormatFile(String password) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        StringBuilder result = new StringBuilder();
        String line;
        while((line = br.readLine()) != null){
            result.append(line + "\n");
        }
        String[] arr = result.toString().split("@@@@@@@@######");
        result = new StringBuilder();

        return result.append(arr[0]).append(password).append(arr[1]).toString();
    }

}
