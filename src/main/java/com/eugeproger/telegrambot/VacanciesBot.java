package com.eugeproger.telegrambot;

import com.eugeproger.telegrambot.dto.VacancyDto;
import com.eugeproger.telegrambot.service.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private VacancyService vacancyService;
    private final String SHOW_JUNIOR_VACANCIES = "showJuniorVacancies";
    private final String SHOW_MIDDLE_VACANCIES = "showMiddleVacations";
    private final String SHOW_SENIOR_VACANCIES = "showSeniorVacations";
    private final String ASKING_TO_CHOSE_VACANCY = "Please choose vacancy: ";
    private final String VACANCY_ID_EQUAL = "vacancyId=";
    private final String BACK_TO_VACANCIES = "backToVacancies";
    private final String BACK_TO_START_MENU = "backToStartMenu";

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

                if (SHOW_JUNIOR_VACANCIES.equals(callbackData)) {
                    showJuniorVacancies(update);
                }
                if (SHOW_MIDDLE_VACANCIES.equals(callbackData)) {
                    showMiddleVacations(update);
                }
                if (SHOW_SENIOR_VACANCIES.equals(callbackData)) {
                    showSeniorVacancies(update);
                }
                if (callbackData.startsWith(VACANCY_ID_EQUAL)) {
                    String id = callbackData.split("=")[1];
                    showVacanciesDescription(id, update);
                }
                if (BACK_TO_VACANCIES.equals(callbackData)) {
                    // add handle
                }
                if (BACK_TO_START_MENU.equals(callbackData)) {
                    // add handle
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't send message to user!", e);
        }
    }

    @Override
    public String getBotUsername() {
        return "eugeproger_vacancise_bot";
    }

    private void handleStartCommand(Update update) {
        String text = update.getMessage().getText();
        System.out.println("Received text is " + text);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText("Welcome to vacancies bot! Please, choose your title");
        sendMessage.setReplyMarkup(getStartMenu());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private ReplyKeyboard getStartMenu() {
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton junior = new InlineKeyboardButton();
        junior.setText("Junior");
        junior.setCallbackData(SHOW_JUNIOR_VACANCIES);
        row.add(junior);

        InlineKeyboardButton middle = new InlineKeyboardButton();
        middle.setText("Middle");
        middle.setCallbackData(SHOW_MIDDLE_VACANCIES);
        row.add(middle);

        InlineKeyboardButton senior = new InlineKeyboardButton();
        senior.setText("Senior");
        senior.setCallbackData(SHOW_SENIOR_VACANCIES);
        row.add(senior);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(List.of(row));
        return keyboard;
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

    private ReplyKeyboard getJuniorVacanciesMenu() {
        List<InlineKeyboardButton> raw = new ArrayList<>();
        List<VacancyDto> vacancies = vacancyService.getJuniorVacancies();

        for (VacancyDto vacancy : vacancies) {
            InlineKeyboardButton vacancyButton = new InlineKeyboardButton();
            vacancyButton.setText(vacancy.getTitle());
            vacancyButton.setCallbackData(VACANCY_ID_EQUAL+vacancy.getId());
            raw.add(vacancyButton);
        }

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(List.of(raw));
        return keyboardMarkup;
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

    private ReplyKeyboard getMiddleVacanciesMenu() {
        List<InlineKeyboardButton> raw = new ArrayList<>();

        InlineKeyboardButton kodilla = new InlineKeyboardButton();
        kodilla.setText("Mid Java Dev at Kodilla");
        kodilla.setCallbackData(VACANCY_ID_EQUAL+3);
        raw.add(kodilla);

        InlineKeyboardButton lidl = new InlineKeyboardButton();
        lidl.setText("Mid SQL Dev at Lidl");
        lidl.setCallbackData(VACANCY_ID_EQUAL+4);
        raw.add(lidl);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(List.of(raw));
        return keyboardMarkup;
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

    private ReplyKeyboard getSeniorVacancies() {
        List<InlineKeyboardButton> raw = new ArrayList<>();

        InlineKeyboardButton ing = new InlineKeyboardButton();
        ing.setText("Senior C++ Dev at ING Bank");
        ing.setCallbackData(VACANCY_ID_EQUAL+5);
        raw.add(ing);

        InlineKeyboardButton apple = new InlineKeyboardButton();
        apple.setText("Senior SWIFT Dev at Apple");
        apple.setCallbackData(VACANCY_ID_EQUAL+6);
        raw.add(apple);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(List.of(raw));
        return keyboardMarkup;
    }

    private void showVacanciesDescription(String id, Update update) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        VacancyDto vacancy = vacancyService.get(id);
        String description = vacancy.getShortDescription();
        sendMessage.setText(description);
        sendMessage.setReplyMarkup(getBackToVacanciesMenu());
        execute(sendMessage);
    }

    private ReplyKeyboard getBackToVacanciesMenu() {
        List<InlineKeyboardButton> raw = new ArrayList<>();
        InlineKeyboardButton backToVacanciesButton = new InlineKeyboardButton();
        backToVacanciesButton.setText("Back to vacancies");
        backToVacanciesButton.setCallbackData(BACK_TO_VACANCIES);
        raw.add(backToVacanciesButton);

        InlineKeyboardButton backToStartMenuButton = new InlineKeyboardButton();
        backToStartMenuButton.setText("Back to start menu");
        backToStartMenuButton.setCallbackData(BACK_TO_START_MENU);
        raw.add(backToVacanciesButton);

        return new InlineKeyboardMarkup(List.of(raw));
    }
}
