package com.eugeproger.telegrambot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class VacanciesBot extends TelegramLongPollingBot {

    private final String SHOW_MIDDLE_VACANCIES = "showMiddleVacations";
    private final String SHOW_SENIOR_VACANCIES = "showSeniorVacations";
    private final String ASKING_TO_CHOSE_VACANCY = "Please choose vacancy: ";
    private final String VACANCY_ID_EQUAL = "vacancyId=";

    public VacanciesBot() {
        super("6684595751:AAFKHN9Skgys0sPFU9dvcnrb4hrZeZo7Z-M");
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.getMessage() != null) {
                handleStartCommand(update);
            }
            if (update.getCallbackQuery() != null) {
                String callbackData = update.getCallbackQuery().getData();

                if ("showJuniorVacancies".equals(callbackData)) {
                    showJuniorVacancies(update);
                } else if (callbackData.startsWith("vacancyId=")) {
                    String id = callbackData.split("=")[1];
                    showVacanciesDescription(id, update);
                }

                if (SHOW_MIDDLE_VACANCIES.equals(callbackData)) {
                    showMiddleVacations(update);
                }
                if (SHOW_SENIOR_VACANCIES.equals(callbackData)) {
                    showSeniorVacancies(update);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't send message to user!", e);
        }
    }

    private void showVacanciesDescription(String id, Update update) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        sendMessage.setText("Vacancy description for vacancy with id = " + id);
        execute(sendMessage);
    }

    private void showJuniorVacancies(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Please choose vacancy: ");
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        sendMessage.setReplyMarkup(getJuniorVacanciesMenu());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void showMiddleVacations(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(ASKING_TO_CHOSE_VACANCY);
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        sendMessage.setReplyMarkup(getMiddleVacanciesMenu());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void showSeniorVacancies(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(ASKING_TO_CHOSE_VACANCY);
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        sendMessage.setReplyMarkup(getSeniorVacancies());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private ReplyKeyboard getJuniorVacanciesMenu() {
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton maVacancy = new InlineKeyboardButton();
        maVacancy.setText("Junior Java developer at MA");
        maVacancy.setCallbackData("vacancyId=1");
        row.add(maVacancy);

        InlineKeyboardButton googleVacancy = new InlineKeyboardButton();
        googleVacancy.setText("Junior Dev at Google");
        googleVacancy.setCallbackData("vacancyId=2");
        row.add(googleVacancy);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(List.of(row));
        return keyboard;
    }

    private ReplyKeyboard getMiddleVacanciesMenu() {
        List<InlineKeyboardButton> raw = new ArrayList<>();

        InlineKeyboardButton kodilla = new InlineKeyboardButton();
        kodilla.setText("Mid Java Dev at Kodilla");
        kodilla.setCallbackData(VACANCY_ID_EQUAL + 3);
        raw.add(kodilla);

        InlineKeyboardButton lidl = new InlineKeyboardButton();
        lidl.setText("Mid SQL Dev at Lidl");
        lidl.setCallbackData(VACANCY_ID_EQUAL + 4);
        raw.add(lidl);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(List.of(raw));
        return keyboardMarkup;
    }

    private ReplyKeyboard getSeniorVacancies() {
        List<InlineKeyboardButton> raw = new ArrayList<>();

        InlineKeyboardButton ing = new InlineKeyboardButton();
        ing.setText("Senior C++ Dev at ING Bank");
        ing.setCallbackData(VACANCY_ID_EQUAL + 5);
        raw.add(ing);

        InlineKeyboardButton apple = new InlineKeyboardButton();
        apple.setText("Senior SWIFT Dev at Apple");
        apple.setCallbackData(VACANCY_ID_EQUAL + 6);
        raw.add(apple);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(List.of(raw));
        return keyboardMarkup;
    }

    private void handleStartCommand(Update update) {
        String text = update.getMessage().getText();
        System.out.println("Received text is " + text);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText("Welcome to vacancies bot! Please, choose your title");
        sendMessage.setReplyMarkup(getStartMessage());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private ReplyKeyboard getStartMessage() {
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton junior = new InlineKeyboardButton();
        junior.setText("Junior");
        junior.setCallbackData("showJuniorVacancies");
        row.add(junior);

        InlineKeyboardButton middle = new InlineKeyboardButton();
        middle.setText("Middle");
        middle.setCallbackData("showMiddleVacations");
        row.add(middle);

        InlineKeyboardButton senior = new InlineKeyboardButton();
        senior.setText("Senior");
        senior.setCallbackData("showSeniorVacations");
        row.add(senior);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(List.of(row));
        return keyboard;
    }

    @Override
    public String getBotUsername() {
        return "eugeproger_vacancise_bot";
    }
}
