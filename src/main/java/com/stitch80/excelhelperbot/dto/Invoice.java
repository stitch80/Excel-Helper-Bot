package com.stitch80.excelhelperbot.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Invoice {


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
//            setStatus("COMPLETED");
            setStatus(InvoiceStatus.COMPLETED);
        } else {
//            setStatus("CREATION");
            setStatus(InvoiceStatus.CREATION);
        }
    }

    public String constructInvoiceStatusMessage() {
        StringBuilder output = new StringBuilder("Invoice Data:\n");
        if (getYear() != null && !getYear().isBlank()
                && getInvNo() != null && !getInvNo().isBlank()) {
            output
                    .append("Invoice number: ")
                    .append(getYear())
                    .append("/")
                    .append(getInvNo())
                    .append("\n");
        } else if (getYear() != null && !getYear().isBlank()) {
            output
                    .append("Invoice number: ")
                    .append(getYear())
                    .append("/ need to fill Invoice number\n");
        } else if (getInvNo() != null && !getInvNo().isBlank()) {
            output
                    .append("Invoice number: need to fill invoice date /")
                    .append(getInvNo())
                    .append("\n");
        } else {
            output
                    .append("Invoice number: need to fill invoice date / need to fill Invoice number\n");
        }
        if (getInvDate() != null && !getInvDate().isBlank()) {
            output
                    .append("Invoice date: ")
                    .append(getInvDate())
                    .append("\n");
        } else {
            output.append("Invoice date: need to fill invoice date\n");
        }
        if (getCustomerName() != null && !getCustomerName().isBlank()) {
            output
                    .append("Customer name: ")
                    .append(getCustomerName())
                    .append("\n");
        } else {
            output.append("Customer name: need to fill customer name\n");
        }
        if (getAmount() != 0) {
            output
                    .append("Amount: ")
                    .append(getAmount())
                    .append("\n");
        } else {
            output.append("Amount: need to fill invoice amount\n");
        }

        return output.toString();
    }

    public String getInvoiceStatus() {
        return invoiceStatus.toString();
    }


//    public void setStatus(String status) {
//        if (EnumUtils.isValidEnum(InvoiceStatus.class, status)) {
//            invoiceStatus = EnumUtils.getEnum(InvoiceStatus.class, status);
//        }
//    }

    public void setStatus(InvoiceStatus status) {
        invoiceStatus = status;
    }


//    @Override
//    public String toString() {
//        return "InvoiceDTO{" +
//                "year='" + year + '\'' +
//                ", invNo=" + invNo +
//                ", invDate='" + invDate + '\'' +
//                ", customerName='" + customerName + '\'' +
//                ", amount=" + amount +
//                ", invoiceStatus=" + invoiceStatus +
//                '}';
//    }

    //    @Getter
    public enum InvoiceStatus {
        CREATION,
        //        YEAR,
        INV_NO,
        //        INV_DATE,
        CUSTOMER_NAME,
        AMOUNT,
        COMPLETED
    }
}



