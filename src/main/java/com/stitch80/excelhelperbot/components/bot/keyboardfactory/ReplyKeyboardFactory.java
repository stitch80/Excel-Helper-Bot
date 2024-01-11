package com.stitch80.excelhelperbot.components.bot.keyboardfactory;

import com.stitch80.excelhelperbot.components.bot.keyboards.reply.InvoiceDetailMenuKeyboard;
import com.stitch80.excelhelperbot.components.bot.keyboards.reply.MainMenuKeyboard;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class ReplyKeyboardFactory {


    private final MainMenuKeyboard mainMenuKeyboard;
    private final InvoiceDetailMenuKeyboard invoiceDetailMenuKeyboard;

    public ReplyKeyboardFactory(MainMenuKeyboard mainMenuKeyboard,
                                InvoiceDetailMenuKeyboard invoiceDetailMenuKeyboard) {
        this.mainMenuKeyboard = mainMenuKeyboard;
        this.invoiceDetailMenuKeyboard = invoiceDetailMenuKeyboard;
    }


    public ReplyKeyboardMarkup constructMainMenu() {
        return mainMenuKeyboard.constructMainMenuKeyboard();
    }

    public ReplyKeyboardMarkup constructInvoiceDetailsMenu() {
        return invoiceDetailMenuKeyboard.constructInvoiceDetailsKeyboard();
    }

}
