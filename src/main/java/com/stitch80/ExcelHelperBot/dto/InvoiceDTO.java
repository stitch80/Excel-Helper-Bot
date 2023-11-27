package com.stitch80.ExcelHelperBot.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
@NoArgsConstructor
public class InvoiceDTO {

    private String year;
    private String invNo;
    private String invDate;
    private String customerName;
    private double amount;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private InvoiceStatus invoiceStatus = InvoiceStatus.CREATION;

    public boolean isCompleted() {
        return year != null && !year.isBlank()
                && invNo != null && !invNo.isBlank()
                && invDate != null && !invDate.isBlank()
                && customerName != null && !customerName.isBlank()
                && amount != 0;
    }

    public void checkIfInvoiceIsCompleted() {
        if (isCompleted()) {
            setStatus("COMPLETED");
        } else {
            setStatus("CREATION");
        }
    }

    public String constructInvoiceStatusMessage() {
        StringBuilder output = new StringBuilder("Invoice Data:\n");
        if (getYear() != null && !getYear().isBlank()
                && getInvNo() != null && !getInvNo().isBlank()) {
            output.append("Invoice number: " + getYear() + "/" + getInvNo() + "\n");
        } else if (getYear() != null && !getYear().isBlank()) {
            output.append("Invoice number: " + getYear() + "/ need to fill Invoice number\n");
        } else if (getInvNo() != null && !getInvNo().isBlank()) {
            output.append("Invoice number: need to fill invoice date /" + getInvNo() + "\n");
        }
        if (getInvDate() != null && !getInvDate().isBlank()) {
            output.append("Invoice date: " + getInvDate() + "\n");
        } else {
            output.append("Invoice date: need to fill invoice date\n");
        }
        if (getCustomerName() != null && !getCustomerName().isBlank()) {
            output.append("Customer name: " + getCustomerName() + "\n");
        } else {
            output.append("Customer name: need to fill customer name\n");
        }
        if (getAmount() != 0) {
            output.append("Amount: " + getAmount() + "\n");
        } else {
            output.append("Amount: need to fill invoice amount\n");
        }

        return output.toString();
    }

    public String getInvoiceStatus() {
        return invoiceStatus.toString();
    }


    public void setStatus(String status) {
        if (EnumUtils.isValidEnum(InvoiceStatus.class, status)) {
            invoiceStatus = EnumUtils.getEnum(InvoiceStatus.class, status);
        }
    }

    @Override
    public String toString() {
        return "InvoiceDTO{" +
                "year='" + year + '\'' +
                ", invNo=" + invNo +
                ", invDate='" + invDate + '\'' +
                ", customerName='" + customerName + '\'' +
                ", amount=" + amount +
                ", invoiceStatus=" + invoiceStatus +
                '}';
    }

    private enum InvoiceStatus {
        CREATION("Creation"),
        YEAR("Year of invoice"),
        INV_NO("Invoice number"),
        INV_DATE("Invoice Date"),
        CUSTOMER_NAME("Customer Name"),
        AMOUNT("Invoice Amount"),
        COMPLETED("Invoice Completed");

        private String status;

        InvoiceStatus(String status) {
            this.setStatus(status);
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}


