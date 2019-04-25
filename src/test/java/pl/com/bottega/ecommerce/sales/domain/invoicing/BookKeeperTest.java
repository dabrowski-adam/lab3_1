package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class BookKeeperTest {

    @Test
    public void issuanceShouldReturnInvoiceWithOnePosition() {
        InvoiceFactory invoiceFactory = new InvoiceFactory();
        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);

        ClientData clientData = mock(ClientData.class);
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);

        ProductData productData = mock(ProductData.class);
        Money money = new Money(0);
        RequestItem requestItem = new RequestItem(productData, 1, money);
        invoiceRequest.add(requestItem);

        TaxPolicy taxPolicy = mock(TaxPolicy.class);
        Tax tax = new Tax(new Money(0), "");
        when(taxPolicy.calculateTax(any(), any())).thenReturn(tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertThat(invoice.getItems(), hasSize(1));
    }

    @Test
    public void issuanceShouldAssignCorrectNet() {
        InvoiceFactory invoiceFactory = new InvoiceFactory();
        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);

        ClientData clientData = mock(ClientData.class);
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);

        ProductData productData = mock(ProductData.class);
        Money money = new Money(100);
        RequestItem requestItem = new RequestItem(productData, 1, money);
        invoiceRequest.add(requestItem);

        TaxPolicy taxPolicy = mock(TaxPolicy.class);
        Tax tax = new Tax(new Money(0), "");
        when(taxPolicy.calculateTax(any(), any())).thenReturn(tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertThat(invoice.getNet(), is(money));
    }

    @Test
    public void issuanceShouldCalculateTaxForEveryItemInRequest() {
        InvoiceFactory invoiceFactory = new InvoiceFactory();
        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);

        ClientData clientData = mock(ClientData.class);
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);

        ProductData productData = mock(ProductData.class);
        Money money = new Money(0);
        RequestItem requestItem = new RequestItem(productData, 1, money);
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);

        TaxPolicy taxPolicy = mock(TaxPolicy.class);
        Tax tax = new Tax(new Money(0), "");
        when(taxPolicy.calculateTax(any(), any())).thenReturn(tax);

        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(2)).calculateTax(any(), any());
    }
}
