package com.stitch80.ExcelHelperBot.components.bot.keyboards.reply;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class MainMenuKeyboard {

    public ReplyKeyboardMarkup constructMainMenuKeyboard() {
        List<KeyboardButton> firstRow = new ArrayList<>();
        KeyboardButton createInvoiceButton = KeyboardButton.builder()
                .text("Create invoice")
                .build();
        firstRow.add(createInvoiceButton);

        return ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(firstRow))
                .resizeKeyboard(true)
                .build();
    }


}
