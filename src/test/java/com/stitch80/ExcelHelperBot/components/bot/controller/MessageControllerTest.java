package com.stitch80.ExcelHelperBot.components.bot.controller;

import com.stitch80.ExcelHelperBot.components.Invoices;
import com.stitch80.ExcelHelperBot.components.bot.ExcelHelperBot;
import com.stitch80.ExcelHelperBot.components.bot.senders.DocumentSender;
import com.stitch80.ExcelHelperBot.components.bot.senders.InlineKeyboardSender;
import com.stitch80.ExcelHelperBot.components.bot.senders.ReplyKeyboardSender;
import com.stitch80.ExcelHelperBot.components.bot.senders.TextSender;
import com.stitch80.ExcelHelperBot.dto.Invoice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class MessageControllerTest {

    public static final long USER_ID = 1001L;
    //    @Captor
    ArgumentCaptor<String> textToSendCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<ExcelHelperBot> botCaptor = ArgumentCaptor.forClass(ExcelHelperBot.class);
    private Invoices invoices;
    private Invoices invoicesMock;
    private MessageController messageController;
    private TextSender textSender;
    private ReplyKeyboardSender replyKeyboardSender;
    private InlineKeyboardSender inlineKeyboardSender;
    private DocumentSender documentSender;
    private ExcelHelperBot excelHelperBot;
    private Message message;
    private Update update;
    private User user;

    @BeforeEach
    public void setup() {
        invoices = new Invoices();
        textSender = mock(TextSender.class);
        update = mock(Update.class);
        replyKeyboardSender = mock(ReplyKeyboardSender.class);
        inlineKeyboardSender = mock(InlineKeyboardSender.class);
        documentSender = mock(DocumentSender.class);
        excelHelperBot = mock(ExcelHelperBot.class);
        messageController = new MessageController(
                invoices,
                textSender,
                replyKeyboardSender,
                inlineKeyboardSender,
                documentSender
        );

        message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);

        user = mock(User.class);
        when(update.getMessage().getFrom()).thenReturn(user);

        invoicesMock = mock(Invoices.class);

    }


    @Test
    public void testCreateInvoice() {

        createNewInvoice();

        Invoice invoice = invoices.getUserInvoice(USER_ID);

        assertNotNull(invoice);
        assertEquals("CREATION", invoices.getInvoiceStatus(USER_ID));

    }

    private void createNewInvoice() {
        sendTextToMessageController("Create invoice");
    }

    private void createNewInvoiceAndTapTheButtonToFillInvoiceFields(String invoiceField) {
        createNewInvoice();

        sendTextToMessageController(invoiceField);
    }

    private void sendTextToMessageController(String invoiceField) {
        when(message.getText()).thenReturn(invoiceField);
        when(user.getId()).thenReturn(USER_ID);
        messageController.processMessage(update, excelHelperBot);
    }

    //region <Test invoice numbers>
    @Test
    public void switchInvoiceStatusToSetInvoiceNumber() {
        createNewInvoiceAndTapTheButtonToFillInvoiceFields("Invoice Number");

        assertEquals("INV_NO", invoices.getInvoiceStatus(USER_ID));
    }


    @Test
    void invoiceNumberEnteredOneDigitNumber() {
        createNewInvoiceAndTapTheButtonToFillInvoiceFields("Invoice Number");

        sendTextToMessageController("1");

        assertEquals("001", invoices.getUserInvoice(USER_ID).getInvNo());
    }

    @Test
    void invoiceNumberEnteredTwoDigitNumber() {
        createNewInvoiceAndTapTheButtonToFillInvoiceFields("Invoice Number");

        sendTextToMessageController("22");

        assertEquals("022", invoices.getUserInvoice(USER_ID).getInvNo());
    }

    @Test
    void invoiceNumberEnteredThreeDigitNumber() {
        createNewInvoiceAndTapTheButtonToFillInvoiceFields("Invoice Number");

        sendTextToMessageController("333");

        assertEquals("333", invoices.getUserInvoice(USER_ID).getInvNo());
    }

    @Test
    void invoiceNumberEnteredNonNumbers() {
        createNewInvoiceAndTapTheButtonToFillInvoiceFields("Invoice Number");

        sendTextToMessageController("big");


        String textToSend = """
                Invoice number must consist of numbers
                Try again!""";

        verify(textSender, times(2)).sendText(
                userIdCaptor.capture(),
                textToSendCaptor.capture(),
                botCaptor.capture());
        List<String> sentMessages = textToSendCaptor.getAllValues();
        assertEquals(textToSend, sentMessages.get(1));

        verify(invoicesMock, never()).setAmount("big", USER_ID);
    }
    //endregion

    //region <Test customer name>
    @Test
    public void switchInvoiceStatusToSetCustomerName() {
        createNewInvoiceAndTapTheButtonToFillInvoiceFields("Customer Name");

        assertEquals("CUSTOMER_NAME", invoices.getInvoiceStatus(USER_ID));
    }

    @Test
    void invoiceCustomerNameEntered() {
        createNewInvoiceAndTapTheButtonToFillInvoiceFields("Customer Name");

        sendTextToMessageController("Lilly Potter");

        assertEquals("Lilly Potter", invoices.getUserInvoice(USER_ID).getCustomerName());
    }
    //endregion

    //region <Test amount>
    @Test
    public void switchInvoiceStatusToSetAmount() {
        createNewInvoiceAndTapTheButtonToFillInvoiceFields("Amount");

        assertEquals("AMOUNT", invoices.getInvoiceStatus(USER_ID));
    }

    @Test
    void invoiceAmountEnteredPositiveNumber() {
        createNewInvoiceAndTapTheButtonToFillInvoiceFields("Amount");

        sendTextToMessageController("25000");

        assertEquals(25000, invoices.getUserInvoice(USER_ID).getAmount());
    }

    @Test
    void invoiceAmountEnteredNegativeNumber() {
        createNewInvoiceAndTapTheButtonToFillInvoiceFields("Amount");

        sendTextToMessageController("-25000");


        String textToSend = """
                The amount should be a positive number
                Try again!""";

        verify(textSender, times(2)).sendText(
                userIdCaptor.capture(),
                textToSendCaptor.capture(),
                botCaptor.capture());
        List<String> sentMessages = textToSendCaptor.getAllValues();
        assertEquals(textToSend, sentMessages.get(1));

        verify(invoicesMock, never()).setAmount("-25000", USER_ID);
    }

    @Test
    void invoiceAmountEnteredNonNumbers() {
        createNewInvoiceAndTapTheButtonToFillInvoiceFields("Amount");

        sendTextToMessageController("money");


        String textToSend = """
                The amount should be a number
                Try again!""";

        verify(textSender, times(2)).sendText(
                userIdCaptor.capture(),
                textToSendCaptor.capture(),
                botCaptor.capture());
        List<String> sentMessages = textToSendCaptor.getAllValues();
        assertEquals(textToSend, sentMessages.get(1));

        verify(invoicesMock, never()).setAmount("money", USER_ID);
    }
    //endregion

    @Test
    void invoiceDateReceiveInlineMenu() {
        createNewInvoiceAndTapTheButtonToFillInvoiceFields("Invoice Date");
        verify(inlineKeyboardSender).sendMonthMenuKeyboard(user, excelHelperBot, LocalDate.now());
    }

    @Test
    void invoiceGetCompletedDocument() {
        createNewInvoice();

        String date = "2023-12-12";
        invoices.setInvNo("001", USER_ID);
        invoices.setYear(date.substring(0, 4), USER_ID);
        invoices.setInvDate(date, USER_ID);
        invoices.setCustomerName("Lilly Potter", USER_ID);
        invoices.setAmount("25000", USER_ID);
        invoices.checkIfInvoiceIsCompleted(USER_ID);

        sendTextToMessageController("Get Invoice");

        verify(documentSender).sendInvoiceDocument(user, invoices, excelHelperBot);
        verify(replyKeyboardSender).sendMainMenu(user, excelHelperBot);

    }

    @Test
    void invoiceTryToGetIncompleteDocument() {
        createNewInvoice();

        sendTextToMessageController("Get Invoice");

        String textToSend = """
                Invoice is not completed
                Please fill all the fields
                """;

        verify(textSender).sendText(
                userIdCaptor.capture(),
                textToSendCaptor.capture(),
                botCaptor.capture());
        List<String> sentMessages = textToSendCaptor.getAllValues();
        assertEquals(textToSend, sentMessages.get(0));

        verify(documentSender, never()).sendInvoiceDocument(user, invoices, excelHelperBot);

    }

}
