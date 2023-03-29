package ru.sharipov;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.sharipov.modules.BotAction;


import java.io.FileInputStream;
import java.util.Properties;
import java.util.Scanner;

public class Bot extends TelegramLongPollingBot {

    private String weatherApi;
    private String botName;
    private String botToken;


    public Bot() {
        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/application.properties");
            properties.load(fis);
            botName = properties.getProperty("bot.name");
            botToken = properties.getProperty("bot.token");
            weatherApi = properties.getProperty("weather.api");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText() && message.getText().startsWith("/start")) {
                String chatId = String.valueOf(message.getChatId());
                SendMessage botMessage = new SendMessage(chatId, "Hello! I am a weather Bot. Just wrote City and i tell you about weather at this time.");
                try {
                    execute(botMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                String city = message.getText();
                String weather = BotAction.getWeather(city, weatherApi);
                String chatId = String.valueOf(message.getChatId());
                SendMessage sendMessage = new SendMessage(chatId, weather);
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}

