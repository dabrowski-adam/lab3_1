package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientDataBuilder;

import java.util.ArrayList;
import java.util.List;

public class InvoiceRequestBuilder {

    private ClientData client = new ClientDataBuilder().createClientData();
    private List<RequestItem> items = new ArrayList<>();

    public InvoiceRequestBuilder setClient(ClientData client) {
        this.client = client;
        return this;
    }

    public InvoiceRequestBuilder setItems(List<RequestItem> items) {
        this.items = items;
        return this;
    }

    public InvoiceRequest createInvoiceRequest() {
        InvoiceRequest invoiceRequest = new InvoiceRequest(client);
        for (RequestItem item : items) {
            invoiceRequest.add(item);
        }
        return invoiceRequest;
    }
}
