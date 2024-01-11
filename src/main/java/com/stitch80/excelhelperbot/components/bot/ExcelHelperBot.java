package com.stitch80.excelhelperbot.components.bot;

import com.stitch80.excelhelperbot.components.bot.controller.CallbackController;
import com.stitch80.excelhelperbot.components.bot.controller.CommandController;
import com.stitch80.excelhelperbot.components.bot.controller.MessageController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class ExcelHelperBot extends TelegramLongPollingBot {

    private final MessageController messageController;
    private final CallbackController callbackController;
    private final CommandController commandController;


    public ExcelHelperBot(
            @Value("${bot.token}") String botToken,
            MessageController messageController,
            CallbackController callbackController,
            CommandController commandController
    ) {
        super(botToken);
        this.messageController = messageController;
        this.callbackController = callbackController;
        this.commandController = commandController;
    }


    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasCallbackQuery()) {
            callbackController.processCallBack(update, this);
        } else if (update.hasMessage()) {
            if (update.getMessage().isCommand()) {
                commandController.processCommand(update, this);
            } else {
                messageController.processMessage(update, this);
            }
        }
    }


    @Override
    public String getBotUsername() {
        return "Excel Helper Bot";
    }
}
