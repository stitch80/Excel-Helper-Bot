package com.stitch80.excelhelperbot.components.bot.controller;

import com.stitch80.excelhelperbot.components.bot.ExcelHelperBot;
import com.stitch80.excelhelperbot.components.bot.senders.InlineKeyboardSender;
import com.stitch80.excelhelperbot.components.bot.senders.ReplyKeyboardSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

class CommandControllerTest {

    private ReplyKeyboardSender replyKeyboardSender;
    private InlineKeyboardSender inlineKeyboardSender;
    private Update update;
    private Message message;
    private User user;
    private CommandController controller;
    private ExcelHelperBot excelHelperBot;

    @BeforeEach
    void setup() {
        replyKeyboardSender = mock(ReplyKeyboardSender.class);
        inlineKeyboardSender = mock(InlineKeyboardSender.class);
        update = mock(Update.class);
        message = mock(Message.class);
        user = mock(User.class);
        excelHelperBot = mock(ExcelHelperBot.class);
        controller = new CommandController(replyKeyboardSender, inlineKeyboardSender);
    }

    @Test
    void startBotCommand() {
        when(update.getMessage()).thenReturn(message);
        when(message.getFrom()).thenReturn(user);
        when(message.getText()).thenReturn("/start");

        controller.processCommand(update, excelHelperBot);

        verify(replyKeyboardSender).sendStartMenu(user, excelHelperBot);
        verify(inlineKeyboardSender, never()).sendMonthMenuKeyboard(user, excelHelperBot, LocalDate.now());
    }

    @Test
    void testBotCommand() {
        when(update.getMessage()).thenReturn(message);
        when(message.getFrom()).thenReturn(user);
        when(message.getText()).thenReturn("/test");

        controller.processCommand(update, excelHelperBot);

        verify(replyKeyboardSender, never()).sendStartMenu(user, excelHelperBot);
        verify(inlineKeyboardSender).sendMonthMenuKeyboard(user, excelHelperBot, LocalDate.now());
    }

}