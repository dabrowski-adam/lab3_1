package pl.com.bottega.ecommerce.sales.domain.invoicing;

public class BookKeeperBuilder {

    private InvoiceFactory invoiceFactory = new InvoiceFactory();

    public BookKeeperBuilder setInvoiceFactory(InvoiceFactory invoiceFactory) {
        this.invoiceFactory = invoiceFactory;
        return this;
    }

    public BookKeeper createBookKeeper() {
        return new BookKeeper(invoiceFactory);
    }
}
