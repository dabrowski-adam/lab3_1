package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class RequestItemBuilder {

    private ProductData productData;
    private int quantity;
    private Money totalCost;

    public RequestItemBuilder() {
        ProductDataBuilder productDataBuilder = new ProductDataBuilder();
        productData = productDataBuilder.createProductData();
        quantity = 1;
        totalCost = new Money(0);
    }

    public RequestItemBuilder setProductData(ProductData productData) {
        this.productData = productData;
        return this;
    }

    public RequestItemBuilder setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public RequestItemBuilder setTotalCost(Money totalCost) {
        this.totalCost = totalCost;
        return this;
    }

    public RequestItem createRequestItem() {
        return new RequestItem(productData, quantity, totalCost);
    }
}
