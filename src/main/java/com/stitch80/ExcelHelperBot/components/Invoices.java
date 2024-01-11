package com.stitch80.ExcelHelperBot.components;

import com.stitch80.ExcelHelperBot.dto.Invoice;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


@Component
@Getter
//@Setter
@NoArgsConstructor
public class Invoices {

    private final Map<Long, Invoice> invoicesInProcess = new HashMap<>();

    private final Pattern DATE_PATTERN = Pattern.compile(
            "^((2000|2400|2800|(19|2[0-9])(0[48]|[2468][048]|[13579][26]))-02-29)$"
                    + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
                    + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$"
                    + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$");

    public boolean userInvoiceIsCompleted(Long userId) {
        return invoicesInProcess.get(userId).isCompleted();
    }

    public void checkIfInvoiceIsCompleted(Long userId) {
        invoicesInProcess.get(userId).checkIfInvoiceIsCompleted();
    }

    public String constructInvoiceStatusMessage(Long userId) {
        return invoicesInProcess.get(userId).constructInvoiceStatusMessage();
    }

    public void createNewInvoice(Long userId) {
        invoicesInProcess.put(userId, new Invoice());
    }

    public Invoice getUserInvoice(Long userId) {
        return invoicesInProcess.get(userId);
    }

    public String getInvoiceStatus(Long userId) {
        return invoicesInProcess.get(userId).getInvoiceStatus();
    }


    public void setStatus(Invoice.InvoiceStatus status, Long userId) {
        invoicesInProcess.get(userId).setStatus(status);
    }

    public void setInvNo(String invNo, Long userId) {
        String checkedInvNo;
        if (invNo.length() > 3 || invNo.isBlank()) {
            throw new RuntimeException("Invoice length must be 3 symbols length");
        } else {
            for (Character c : invNo.toCharArray()) {
                if (!Character.isDigit(c)) {
                    throw new NumberFormatException("Invoice number must consist of numbers");
                }
            }
            if (invNo.length() < 3) {
                checkedInvNo = String.format("%03d", Integer.parseInt(invNo));
            } else {
                checkedInvNo = invNo;
            }
        }
        invoicesInProcess.get(userId).setInvNo(checkedInvNo);
    }

    public void setCustomerName(String customerName, Long userId) {
        invoicesInProcess.get(userId).setCustomerName(customerName);
    }

    public void setAmount(String amount, Long userId) {
        double checkedAmount;
        try {
            checkedAmount = Double.parseDouble(amount);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException("The amount should be a number");
        }
        if (checkedAmount <= 0) {
            throw new RuntimeException("The amount should be a positive number");
        }
        invoicesInProcess.get(userId).setAmount(checkedAmount);
    }

    public void setInvDate(String invDate, Long userId) {
        if (!invDate.matches(DATE_PATTERN.toString())) {
            throw new RuntimeException("The date should be in format YYYY-MM-DD");
        }
        invoicesInProcess.get(userId).setInvDate(invDate);
    }

    public void setYear(String year, Long userId) {
        if (year.length() != 4 || year.isBlank()) {
            throw new RuntimeException("Invoice year length must be 4 symbols length");
        } else {
            for (Character c : year.toCharArray()) {
                if (!Character.isDigit(c)) {
                    throw new NumberFormatException("Invoice year must consist of numbers");
                }
            }
        }

        invoicesInProcess.get(userId).setYear(year);
    }

}


