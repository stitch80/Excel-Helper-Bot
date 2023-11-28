package com.stitch80.ExcelHelperBot.bot.keyboards.reply;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class InvoiceDetailMenuKeyboard {

    public ReplyKeyboardMarkup constructInvoiceDetailsKeyboard() {
        List<KeyboardButton> firstRow = new ArrayList<>();
        KeyboardButton setInvoiceNumber = KeyboardButton.builder().text("Invoice Number").build();
        KeyboardButton setInvoiceDate = KeyboardButton.builder().text("Invoice Date").build();
        firstRow.add(setInvoiceNumber);
        firstRow.add(setInvoiceDate);

        List<KeyboardButton> secondRow = new ArrayList<>();
        KeyboardButton setCustomerName = KeyboardButton.builder().text("Customer Name").build();
        KeyboardButton setAmount = KeyboardButton.builder().text("Amount").build();
        secondRow.add(setCustomerName);
        secondRow.add(setAmount);

        List<KeyboardButton> thirdRow = new ArrayList<>();
        KeyboardButton getInvoice = KeyboardButton.builder().text("Get Invoice").build();
        thirdRow.add(getInvoice);

        ReplyKeyboardMarkup invoiceDetailsKeyboard = ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(firstRow))
                .keyboardRow(new KeyboardRow(secondRow))
                .keyboardRow(new KeyboardRow(thirdRow))
                .resizeKeyboard(true)
                .build();

        return invoiceDetailsKeyboard;
    }
}
