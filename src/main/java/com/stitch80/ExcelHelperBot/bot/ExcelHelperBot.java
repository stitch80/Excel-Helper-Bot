package com.stitch80.ExcelHelperBot.bot;

import com.stitch80.ExcelHelperBot.bot.controller.CallbackController;
import com.stitch80.ExcelHelperBot.bot.controller.CommandController;
import com.stitch80.ExcelHelperBot.bot.controller.MessageController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class ExcelHelperBot extends TelegramLongPollingBot {

    private MessageController messageController;
    private CallbackController callbackController;
    private CommandController commandController;


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
            }
            messageController.processMessage(update, this);
        }
    }


//    private void processCallback(Update update) {
//        CallbackQuery cq = update.getCallbackQuery();
//        try {
//            buttonTap(
//                    cq.getFrom().getId(),
//                    cq.getId(),
//                    cq.getData(),
//                    cq.getMessage().getMessageId()
//            );
//        } catch (TelegramApiException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    private void buttonTap(Long id, String queryId, String data, int msgId) throws TelegramApiException {
//
//        EditMessageText newTxt = EditMessageText.builder()
//                .chatId(id.toString())
//                .messageId(msgId).parseMode("HTML").text("").build();
//
//        EditMessageReplyMarkup newKb = EditMessageReplyMarkup.builder()
//                .chatId(id.toString()).messageId(msgId).build();
//
//        if (data.equals("next")) {
//            newTxt.setText("<b>Menu 2</b>");
//            newKb.setReplyMarkup(keyboardM2);
//        } else if (data.equals("back")) {
//            newTxt.setText("<b>Menu 1</b>");
//            newKb.setReplyMarkup(keyboardM1);
//        }
//
//        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
//                .callbackQueryId(queryId).build();
//
//        execute(close);
//        execute(newTxt);
//        execute(newKb);
//    }

//    private void sendInlineMenu(Long userId, String text, InlineKeyboardMarkup kb) {
//        SendMessage sendMessage = SendMessage.builder()
//                .chatId(userId.toString())
//                .parseMode("HTML").text(text)
//                .replyMarkup(kb)
//                .build();
//
//
//        try {
//            execute(sendMessage);
//        } catch (TelegramApiException e) {
//            log.error("Keyboard cannot be sent");
//        }
//    }


//    private void copyMessage(Long userId, Integer msgId) {
//        CopyMessage copyMessage = CopyMessage.builder()
//                .fromChatId(userId.toString())
//                .chatId(userId.toString())
//                .messageId(msgId)
//                .build();
//
//        try {
//            execute(copyMessage);
//        } catch (TelegramApiException e) {
//            log.error("The message wasn't copied");
//        }
//    }

    @Override
    public String getBotUsername() {
        return "Excel Helper Bot";
    }
}
