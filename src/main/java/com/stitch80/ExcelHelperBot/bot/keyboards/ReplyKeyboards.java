package com.stitch80.ExcelHelperBot.bot.keyboards;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class ReplyKeyboards {

    private HashMap<String, ReplyKeyboardMarkup> keyboards;

    public ReplyKeyboards(HashMap<String, ReplyKeyboardMarkup> keyboards) {
        this.keyboards = new HashMap<>();
        constructMainMenuKeyboard();
        constructInvoiceDetailsKeyboard();
    }

    private void constructMainMenuKeyboard() {
        List<KeyboardButton> firstRow = new ArrayList<>();
        var createInvoiceButton = KeyboardButton.builder()
                .text("Create invoice")
                .build();
        firstRow.add(createInvoiceButton);
        var createInvoiceKeyboard = ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(firstRow))
//                .isPersistent(true)
                .resizeKeyboard(true)
                .build();
        keyboards.put("main", createInvoiceKeyboard);
    }

    private void constructInvoiceDetailsKeyboard() {
        List<KeyboardButton> firstRow = new ArrayList<>();
        KeyboardButton setYear = KeyboardButton.builder().text("Year").build();
        KeyboardButton setInvoiceNumber = KeyboardButton.builder().text("Invoice Number").build();
        KeyboardButton setInvoiceDate = KeyboardButton.builder().text("Invoice Date").build();
        firstRow.add(setYear);
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

        keyboards.put("invdetails", invoiceDetailsKeyboard);

    }

    public ReplyKeyboardMarkup getKeyboard(String name) {
        return keyboards.get(name);
    }


}
