package com.stitch80.excelhelperbot.dto;

import com.stitch80.excelhelperbot.components.Invoices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvoicesTest {
    public static final long USER_ID = 1001L;
    private Invoices invoices;

    @BeforeEach
    void setup() {
        invoices = new Invoices();
        invoices.createNewInvoice(USER_ID);

    }


    @Test
    void invoiceCreated() {
        assertNotNull(invoices.getUserInvoice(USER_ID));
    }

    //region <InvoiceNumber>
    @Test
    void setInvoiceNumberOneDigit() {
        invoices.setInvNo("1", USER_ID);

        assertEquals("001", invoices.getUserInvoice(USER_ID).getInvNo());
    }

    @Test
    void setInvoiceNumberTwoDigits() {
        invoices.setInvNo("22", USER_ID);

        assertEquals("022", invoices.getUserInvoice(USER_ID).getInvNo());
    }

    @Test
    void setInvoiceNumberThreeDigits() {
        invoices.setInvNo("333", USER_ID);

        assertEquals("333", invoices.getUserInvoice(USER_ID).getInvNo());
    }

    @Test
    void invoiceNumberShouldNotBeEmpty() {
        assertThrows(RuntimeException.class, () -> invoices.setInvNo("", USER_ID));
    }

    @Test
    void invoiceNumberShouldNotBeMoreThan3Symbols() {
        assertThrows(RuntimeException.class, () -> invoices.setInvNo("4444", USER_ID));
    }

    @Test
    void invoiceNumberShouldConsistOfNumbers() {
        assertThrows(NumberFormatException.class, () -> invoices.setInvNo("qwe", USER_ID));
    }

    //endregion

    //region <Invoice Year>
    @Test
    void invoiceYearCorrect() {
        invoices.setYear("2023", USER_ID);
        assertEquals("2023", invoices.getUserInvoice(USER_ID).getYear());
    }

    @Test
    void invoiceYearShouldNotBeLessThan4Digits() {
        assertThrows(RuntimeException.class, () -> invoices.setYear("203", USER_ID));
    }

    @Test
    void invoiceYearShouldNotBeMoreThan4Digits() {
        assertThrows(RuntimeException.class, () -> invoices.setYear("20223", USER_ID));
    }

    @Test
    void invoiceYearShouldConsistOfNumbers() {
        assertThrows(NumberFormatException.class, () -> invoices.setYear("Year", USER_ID));
    }
    //endregion

    //region <Customer Name>
    @Test
    void invoiceCustomerNameCorrect() {
        invoices.setCustomerName("John Doe", USER_ID);
        assertEquals("John Doe", invoices.getUserInvoice(USER_ID).getCustomerName());
    }
    //endregion

    //region <Amount>
    @Test
    void invoiceAmountCorrect() {
        invoices.setAmount("10000", USER_ID);
        assertEquals(10000, invoices.getUserInvoice(USER_ID).getAmount());
    }

    @Test
    void invoiceAmountShouldBePositive() {
        assertThrows(RuntimeException.class, () -> invoices.setAmount("-1000", USER_ID));
    }
    //endregion

    //region <Invoice Date>
    @Test
    void invoiceDateCorrect() {
        invoices.setInvDate("2023-12-12", USER_ID);
        assertEquals("2023-12-12", invoices.getUserInvoice(USER_ID).getInvDate());
    }

    @Test
    void invoiceDateMeetsThaDateMask() {
        assertDoesNotThrow(() -> invoices.setInvDate("2023-12-12", USER_ID));
    }

    @Test
    void invoiceDateDoesNotMeetDateMask() {
        assertThrows(RuntimeException.class, () -> invoices.setInvDate("12-12-2023", USER_ID));
    }

    //endregion

    //region <Invoice Completeness>
    @Test
    void invoiceIsCompleted() {
        String date = "2023-12-12";
        invoices.setInvNo("001", USER_ID);
        invoices.setYear(date.substring(0, 4), USER_ID);
        invoices.setInvDate(date, USER_ID);
        invoices.setCustomerName("Lilly Potter", USER_ID);
        invoices.setAmount("25000", USER_ID);

        assertTrue(invoices.getUserInvoice(USER_ID).isCompleted());
    }

    @Test
    void invoiceIsNotCompleted() {
        assertFalse(invoices.getUserInvoice(USER_ID).isCompleted());
    }

    @Test
    void invoiceIsSetToCompleted() {
        String date = "2023-12-12";
        invoices.setInvNo("001", USER_ID);
        invoices.setYear(date.substring(0, 4), USER_ID);
        invoices.setInvDate(date, USER_ID);
        invoices.setCustomerName("Lilly Potter", USER_ID);
        invoices.setAmount("25000", USER_ID);

        invoices.checkIfInvoiceIsCompleted(USER_ID);

        assertEquals(Invoice.InvoiceStatus.COMPLETED.toString(), invoices.getInvoiceStatus(USER_ID));
    }

    @Test
    void invoiceIsNotSetToCompleted() {
        invoices.checkIfInvoiceIsCompleted(USER_ID);

        assertNotEquals(Invoice.InvoiceStatus.COMPLETED.toString(), invoices.getInvoiceStatus(USER_ID));
        assertEquals(Invoice.InvoiceStatus.CREATION.toString(), invoices.getInvoiceStatus(USER_ID));
    }
    //endregion

    //region <Invoice Status Message>
    @Test
    void checkInvoiceStatusMessageCompleted() {
        String date = "2023-12-12";
        invoices.setInvNo("001", USER_ID);
        invoices.setYear(date.substring(0, 4), USER_ID);
        invoices.setInvDate(date, USER_ID);
        invoices.setCustomerName("Lilly Potter", USER_ID);
        invoices.setAmount("25000", USER_ID);

        String invoiceStatusMessage = """
                Invoice Data:
                Invoice number: 2023/001
                Invoice date: 2023-12-12
                Customer name: Lilly Potter
                Amount: 25000.0
                """;

        assertEquals(invoiceStatusMessage, invoices.constructInvoiceStatusMessage(USER_ID));
    }

    @Test
    void checkInvoiceStatusMessageNeedToFillInvoiceNumber() {
        String date = "2023-12-12";
        invoices.setYear(date.substring(0, 4), USER_ID);
        invoices.setInvDate(date, USER_ID);
        invoices.setCustomerName("Lilly Potter", USER_ID);
        invoices.setAmount("25000", USER_ID);

        String invoiceStatusMessage = """
                Invoice Data:
                Invoice number: 2023/ need to fill Invoice number
                Invoice date: 2023-12-12
                Customer name: Lilly Potter
                Amount: 25000.0
                """;

        assertEquals(invoiceStatusMessage, invoices.constructInvoiceStatusMessage(USER_ID));
    }

    @Test
    void checkInvoiceStatusMessageNeedToFillDate() {
        invoices.setInvNo("001", USER_ID);
        invoices.setCustomerName("Lilly Potter", USER_ID);
        invoices.setAmount("25000", USER_ID);

        String invoiceStatusMessage = """
                Invoice Data:
                Invoice number: need to fill invoice date /001
                Invoice date: need to fill invoice date
                Customer name: Lilly Potter
                Amount: 25000.0
                """;

        assertEquals(invoiceStatusMessage, invoices.constructInvoiceStatusMessage(USER_ID));
    }

    @Test
    void checkInvoiceStatusMessageNeedToFillInvoiceNumberAndDate() {
        invoices.setCustomerName("Lilly Potter", USER_ID);
        invoices.setAmount("25000", USER_ID);

        String invoiceStatusMessage = """
                Invoice Data:
                Invoice number: need to fill invoice date / need to fill Invoice number
                Invoice date: need to fill invoice date
                Customer name: Lilly Potter
                Amount: 25000.0
                """;

        assertEquals(invoiceStatusMessage, invoices.constructInvoiceStatusMessage(USER_ID));
    }

    @Test
    void checkInvoiceStatusMessageNeedToFillCustomerName() {
        String date = "2023-12-12";
        invoices.setInvNo("001", USER_ID);
        invoices.setYear(date.substring(0, 4), USER_ID);
        invoices.setInvDate(date, USER_ID);
        invoices.setAmount("25000", USER_ID);

        String invoiceStatusMessage = """
                Invoice Data:
                Invoice number: 2023/001
                Invoice date: 2023-12-12
                Customer name: need to fill customer name
                Amount: 25000.0
                """;

        assertEquals(invoiceStatusMessage, invoices.constructInvoiceStatusMessage(USER_ID));
    }

    @Test
    void checkInvoiceStatusMessageNeedToFillAmount() {
        String date = "2023-12-12";
        invoices.setInvNo("001", USER_ID);
        invoices.setYear(date.substring(0, 4), USER_ID);
        invoices.setInvDate(date, USER_ID);
        invoices.setCustomerName("Lilly Potter", USER_ID);

        String invoiceStatusMessage = """
                Invoice Data:
                Invoice number: 2023/001
                Invoice date: 2023-12-12
                Customer name: Lilly Potter
                Amount: need to fill invoice amount
                """;

        assertEquals(invoiceStatusMessage, invoices.constructInvoiceStatusMessage(USER_ID));
    }
    //endregion
}