package com.stitch80.excelhelperbot.components.bot.senders;

import com.stitch80.excelhelperbot.components.bot.ExcelHelperBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
public class TextSender {

    public void sendText(Long userId, String textToSend, ExcelHelperBot excelHelperBot) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(userId.toString())
                .text(textToSend)
                .build();

        try {
            excelHelperBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("The message wasn't sent");
        }

    }
}
