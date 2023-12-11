package com.stitch80.ExcelHelperBot.components.bot.controller;

import com.stitch80.ExcelHelperBot.components.Invoices;
import com.stitch80.ExcelHelperBot.components.bot.ExcelHelperBot;
import com.stitch80.ExcelHelperBot.components.bot.keyboards.inline.MonthMenuKeyboard;
import com.stitch80.ExcelHelperBot.components.bot.keyboards.inline.QuarterCenturyMenuKeyboard;
import com.stitch80.ExcelHelperBot.components.bot.keyboards.inline.YearMenuKeyboard;
import com.stitch80.ExcelHelperBot.components.bot.senders.InlineKeyboardSender;
import com.stitch80.ExcelHelperBot.components.bot.senders.ReplyKeyboardSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

class CallbackControllerTest {

    private Update update;
    private CallbackQuery callbackQuery;
    private ExcelHelperBot excelHelperBot;
    private CallbackController controller;
    private InlineKeyboardSender inlineKeyboardSender;
    private ReplyKeyboardSender replyKeyboardSender;
    private Invoices invoices;
    private User user;

    @BeforeEach
    void setup() {
        update = mock(Update.class);
        callbackQuery = mock(CallbackQuery.class);
        excelHelperBot = mock(ExcelHelperBot.class);
        inlineKeyboardSender = mock(InlineKeyboardSender.class);
        replyKeyboardSender = mock(ReplyKeyboardSender.class);
        invoices = mock(Invoices.class);
        user = mock(User.class);
        controller = new CallbackController(
                inlineKeyboardSender,
                replyKeyboardSender,
                invoices);

        when(update.getCallbackQuery()).thenReturn(callbackQuery);

    }

    @Test
    void switchMonthMenu() {
        when(callbackQuery.getData()).thenReturn(MonthMenuKeyboard.CONTROL_MONTH + LocalDate.now());
        controller.processCallBack(update, excelHelperBot);


        verify(inlineKeyboardSender).switchMonthMenu(callbackQuery, excelHelperBot, LocalDate.now());

        verify(inlineKeyboardSender, never()).sendYearMenuKeyboard(callbackQuery, excelHelperBot, LocalDate.now());

        verify(inlineKeyboardSender, never()).sendYearRangeMenuKeyboard(callbackQuery, excelHelperBot, LocalDate.now());
        verify(inlineKeyboardSender, never()).switchYearRangeMenu(callbackQuery, excelHelperBot, LocalDate.of(2023, 1, 1));


        verify(inlineKeyboardSender, never()).clearMenu(callbackQuery, excelHelperBot);
        verify(inlineKeyboardSender, never()).deleteMessage(callbackQuery, excelHelperBot);
        verify(invoices, never()).setInvDate(LocalDate.now().toString(), user.getId());
        verify(invoices, never()).setYear(LocalDate.now().toString().substring(0, 4), user.getId());
        verify(replyKeyboardSender, never()).sendInvoiceStatusAndInvoiceMenu(user, excelHelperBot);
    }

    //region <Year Menu>
    @Test
    void sendYearMenu() {
        when(callbackQuery.getData()).thenReturn(YearMenuKeyboard.CONTROL_YEAR + "start_2023");
        controller.processCallBack(update, excelHelperBot);


        verify(inlineKeyboardSender, never()).switchMonthMenu(callbackQuery, excelHelperBot, LocalDate.now());

        verify(inlineKeyboardSender).clearMenu(callbackQuery, excelHelperBot);
        verify(inlineKeyboardSender).deleteMessage(callbackQuery, excelHelperBot);
        verify(inlineKeyboardSender).sendYearMenuKeyboard(callbackQuery, excelHelperBot, LocalDate.of(2023, 1, 1));
        verify(inlineKeyboardSender, never()).switchYearMenu(callbackQuery, excelHelperBot, LocalDate.now());

        verify(inlineKeyboardSender, never()).sendYearRangeMenuKeyboard(callbackQuery, excelHelperBot, LocalDate.now());
        verify(inlineKeyboardSender, never()).switchYearRangeMenu(callbackQuery, excelHelperBot, LocalDate.of(2023, 1, 1));

        verify(invoices, never()).setInvDate(LocalDate.now().toString(), user.getId());
        verify(invoices, never()).setYear(LocalDate.now().toString().substring(0, 4), user.getId());
        verify(replyKeyboardSender, never()).sendInvoiceStatusAndInvoiceMenu(user, excelHelperBot);
    }

    @Test
    void switchYearMenu() {
        when(callbackQuery.getData()).thenReturn(YearMenuKeyboard.CONTROL_YEAR + "2023");
        controller.processCallBack(update, excelHelperBot);


        verify(inlineKeyboardSender, never()).switchMonthMenu(callbackQuery, excelHelperBot, LocalDate.now());

        verify(inlineKeyboardSender).switchYearMenu(callbackQuery, excelHelperBot, LocalDate.of(2023, 1, 1));
        verify(inlineKeyboardSender, never()).clearMenu(callbackQuery, excelHelperBot);
        verify(inlineKeyboardSender, never()).deleteMessage(callbackQuery, excelHelperBot);
        verify(inlineKeyboardSender, never()).sendYearMenuKeyboard(callbackQuery, excelHelperBot, LocalDate.of(2023, 1, 1));

        verify(inlineKeyboardSender, never()).sendYearRangeMenuKeyboard(callbackQuery, excelHelperBot, LocalDate.now());
        verify(inlineKeyboardSender, never()).switchYearRangeMenu(callbackQuery, excelHelperBot, LocalDate.of(2023, 1, 1));

        verify(invoices, never()).setInvDate(LocalDate.now().toString(), user.getId());
        verify(invoices, never()).setYear(LocalDate.now().toString().substring(0, 4), user.getId());
        verify(replyKeyboardSender, never()).sendInvoiceStatusAndInvoiceMenu(user, excelHelperBot);
    }
    //endregion

    //region <Year Range Menu>
    @Test
    void sendYearRangeMenu() {
        when(callbackQuery.getData()).thenReturn(QuarterCenturyMenuKeyboard.CONTROL_QUARTER_CENTURY + "start_2023");
        controller.processCallBack(update, excelHelperBot);


        verify(inlineKeyboardSender, never()).switchMonthMenu(callbackQuery, excelHelperBot, LocalDate.now());

        verify(inlineKeyboardSender, never()).switchYearMenu(callbackQuery, excelHelperBot, LocalDate.of(2023, 1, 1));
        verify(inlineKeyboardSender, never()).sendYearMenuKeyboard(callbackQuery, excelHelperBot, LocalDate.of(2023, 1, 1));

        verify(inlineKeyboardSender).clearMenu(callbackQuery, excelHelperBot);
        verify(inlineKeyboardSender).deleteMessage(callbackQuery, excelHelperBot);
        verify(inlineKeyboardSender).sendYearRangeMenuKeyboard(callbackQuery, excelHelperBot, LocalDate.of(2023, 1, 1));
        verify(inlineKeyboardSender, never()).switchYearRangeMenu(callbackQuery, excelHelperBot, LocalDate.of(2023, 1, 1));

        verify(invoices, never()).setInvDate(LocalDate.now().toString(), user.getId());
        verify(invoices, never()).setYear(LocalDate.now().toString().substring(0, 4), user.getId());
        verify(replyKeyboardSender, never()).sendInvoiceStatusAndInvoiceMenu(user, excelHelperBot);
    }

    @Test
    void switchYearRangeMenu() {
        when(callbackQuery.getData()).thenReturn(QuarterCenturyMenuKeyboard.CONTROL_QUARTER_CENTURY + "2023");
        controller.processCallBack(update, excelHelperBot);


        verify(inlineKeyboardSender, never()).switchMonthMenu(callbackQuery, excelHelperBot, LocalDate.now());

        verify(inlineKeyboardSender, never()).switchYearMenu(callbackQuery, excelHelperBot, LocalDate.of(2023, 1, 1));
        verify(inlineKeyboardSender, never()).sendYearMenuKeyboard(callbackQuery, excelHelperBot, LocalDate.of(2023, 1, 1));

        verify(inlineKeyboardSender, never()).clearMenu(callbackQuery, excelHelperBot);
        verify(inlineKeyboardSender, never()).deleteMessage(callbackQuery, excelHelperBot);
        verify(inlineKeyboardSender, never()).sendYearRangeMenuKeyboard(callbackQuery, excelHelperBot, LocalDate.of(2023, 1, 1));

        verify(inlineKeyboardSender).switchYearRangeMenu(callbackQuery, excelHelperBot, LocalDate.of(2023, 1, 1));

        verify(invoices, never()).setInvDate(LocalDate.now().toString(), user.getId());
        verify(invoices, never()).setYear(LocalDate.now().toString().substring(0, 4), user.getId());
        verify(replyKeyboardSender, never()).sendInvoiceStatusAndInvoiceMenu(user, excelHelperBot);
    }
    //endregion

    @Test
    void saveInvoiceDate() {
        when(callbackQuery.getData()).thenReturn(LocalDate.now().toString());
        when(callbackQuery.getFrom()).thenReturn(user);
        controller.processCallBack(update, excelHelperBot);


        verify(inlineKeyboardSender, never()).switchMonthMenu(callbackQuery, excelHelperBot, LocalDate.now());

        verify(inlineKeyboardSender, never()).switchYearMenu(callbackQuery, excelHelperBot, LocalDate.of(2023, 1, 1));
        verify(inlineKeyboardSender, never()).sendYearMenuKeyboard(callbackQuery, excelHelperBot, LocalDate.of(2023, 1, 1));

        verify(inlineKeyboardSender, never()).sendYearRangeMenuKeyboard(callbackQuery, excelHelperBot, LocalDate.of(2023, 1, 1));

        verify(inlineKeyboardSender, never()).switchYearRangeMenu(callbackQuery, excelHelperBot, LocalDate.of(2023, 1, 1));

        verify(inlineKeyboardSender).clearMenu(callbackQuery, excelHelperBot);
        verify(inlineKeyboardSender).deleteMessage(callbackQuery, excelHelperBot);
        verify(invoices).setInvDate(LocalDate.now().toString(), user.getId());
        verify(invoices).setYear(LocalDate.now().toString().substring(0, 4), user.getId());
        verify(replyKeyboardSender).sendInvoiceStatusAndInvoiceMenu(user, excelHelperBot);
    }

}