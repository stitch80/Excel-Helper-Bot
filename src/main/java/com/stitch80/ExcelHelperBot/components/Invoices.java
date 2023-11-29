package com.stitch80.ExcelHelperBot.components;

import com.stitch80.ExcelHelperBot.dto.Invoice;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
@Getter
@Setter
@NoArgsConstructor
public class Invoices {

    private Map<Long, Invoice> invoicesInProcess = new HashMap<>();

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


    public void setStatus(String status, Long userId) {
        invoicesInProcess.get(userId).setStatus(status);
//        }
    }

    public void setInvNo(String invNo, Long userId) {
        invoicesInProcess.get(userId).setInvNo(invNo);
    }

    public void setCustomerName(String customerName, Long userId) {
        invoicesInProcess.get(userId).setCustomerName(customerName);
    }

    public void setAmount(double amount, Long userId) {
        invoicesInProcess.get(userId).setAmount(amount);
    }

    public void setInvDate(String invDate, Long userId) {
        invoicesInProcess.get(userId).setInvDate(invDate);
    }

    public void setYear(String year, Long userId) {
        invoicesInProcess.get(userId).setYear(year);
    }

}


