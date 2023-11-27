package com.bajidan.userdemo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.Date;

@RestController
@RequestMapping("/userDemo")
public class UserDemoController {
    File messageFile = new File("src/main/java/com/bajidan/userdemo/message.txt");
    File logFile = new File("src/main/java/com/bajidan/userdemo/log.txt");

    @GetMapping("getMessage")
    public String getAllMessage() {
       return readFile(messageFile);
    }

    @GetMapping("messageCount")
    public String getAMessageCount() {
        int count = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(logFile))){

            while ((bufferedReader.readLine()) != null) {
               count++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Number of Messages: " + count;
    }

    @GetMapping("log")
    public String getLog(){
        return readFile(logFile);
    }

    @PostMapping("/postMessage")
    public ResponseEntity<String> postMessage(@RequestBody String message) {
        String date = "Date: " + new Date();
        StringBuilder sb = new StringBuilder(date);

        try (BufferedWriter bWriterToMessageFile = new BufferedWriter(new FileWriter(messageFile, true));
            BufferedWriter bWriterToLogFile = new BufferedWriter(new FileWriter(logFile, true))){
            sb.append("\n").append(message);
            bWriterToMessageFile.write(sb.toString());
            bWriterToLogFile.write("New message created\n");


            return ResponseEntity.ok("message sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String readFile(File file) {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))){
            String x;
            while ((x = bufferedReader.readLine()) != null) {
                sb.append(x);
                sb.append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
