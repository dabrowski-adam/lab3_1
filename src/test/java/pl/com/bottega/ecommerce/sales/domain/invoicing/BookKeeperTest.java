package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Test;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

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

        ClientData clientData = new ClientData(Id.generate(), "");
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);

        ProductData productData = new ProductData(Id.generate(), new Money(0), "", ProductType.STANDARD, new Date());
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

        ClientData clientData = new ClientData(Id.generate(), "");
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);

        ProductData productData = new ProductData(Id.generate(), new Money(0), "", ProductType.STANDARD, new Date());
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
    public void issuanceShouldAssignCorrectGros() {
        InvoiceFactory invoiceFactory = new InvoiceFactory();
        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);

        ClientData clientData = new ClientData(Id.generate(), "");
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);

        ProductData productData = new ProductData(Id.generate(), new Money(0), "", ProductType.STANDARD, new Date());
        Money money = new Money(100);
        RequestItem requestItem = new RequestItem(productData, 1, money);
        invoiceRequest.add(requestItem);

        TaxPolicy taxPolicy = mock(TaxPolicy.class);
        Tax tax = new Tax(new Money(50), "");
        when(taxPolicy.calculateTax(any(), any())).thenReturn(tax);

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertThat(invoice.getGros(), is(money.add(tax.getAmount())));
    }

    @Test
    public void issuanceShouldCalculateTaxForEveryItemInRequest() {
        InvoiceFactory invoiceFactory = new InvoiceFactory();
        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);

        ClientData clientData = new ClientData(Id.generate(), "");
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);

        ProductData productData = new ProductData(Id.generate(), new Money(0), "", ProductType.STANDARD, new Date());
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

    @Test
    public void issuanceShouldCalculateTaxBasedOnProductData() {
        InvoiceFactory invoiceFactory = new InvoiceFactory();
        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);

        ClientData clientData = new ClientData(Id.generate(), "");
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);

        ProductData productData = new ProductData(Id.generate(), new Money(0), "", ProductType.STANDARD, new Date());
        Money money = new Money(0);
        RequestItem requestItem = new RequestItem(productData, 1, money);
        invoiceRequest.add(requestItem);

        TaxPolicy taxPolicy = mock(TaxPolicy.class);
        Tax tax = new Tax(new Money(0), "");
        when(taxPolicy.calculateTax(any(), any())).thenReturn(tax);

        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy).calculateTax(requestItem.getProductData().getType(), requestItem.getTotalCost());
    }

    @Test
    public void issuanceShouldCreateOneInvoice() {
        InvoiceFactory invoiceFactory = Mockito.spy(new InvoiceFactory());
        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);

        ClientData clientData = new ClientData(Id.generate(), "");
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);

        ProductData productData = new ProductData(Id.generate(), new Money(0), "", ProductType.STANDARD, new Date());
        Money money = new Money(0);
        RequestItem requestItem = new RequestItem(productData, 1, money);
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);

        TaxPolicy taxPolicy = mock(TaxPolicy.class);
        Tax tax = new Tax(new Money(0), "");
        when(taxPolicy.calculateTax(any(), any())).thenReturn(tax);

        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(invoiceFactory, times(1)).create(any());
    }
}
