package ru.sharipov;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.util.Properties;

import ru.sharipov.modules.BotAction;

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
                SendMessage botMessage = new SendMessage(
                        chatId, "Hello! I am a weather Bot. \n" +
                        "Just wrote City and i tell you about weather at this time.");
                exTry(botMessage);
            } else if (message.hasText() && message.getText().startsWith("/weather")) {
                SendMessage sendMessage = BotAction.sendWeatherMessage(
                        message.getChatId(), "Enter a city name: ");
                exTry(sendMessage);
            } else if (message.hasText()) {
                String city = message.getText();
                String weather = BotAction.getWeather(city, weatherApi);
                String chatId = String.valueOf(message.getChatId());
                SendMessage sendMessage = new SendMessage(chatId, weather);
                exTry(sendMessage);
            }
        }
    }

    public void exTry(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
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

