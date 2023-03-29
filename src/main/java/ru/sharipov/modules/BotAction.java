package ru.sharipov.modules;

import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class BotAction {

    public static SendMessage sendWeatherMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }

    public static String getWeather(String city, String weatherApi) {
        String result = "";
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + weatherApi);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
            JSONObject json = new JSONObject(builder.toString());
            String description = json.getJSONArray("weather").getJSONObject(0).getString("description");
            double temperature = json.getJSONObject("main").getDouble("temp");
            result = String.format("%s. Temperature: %.0f°C", description, temperature - 273.15);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
