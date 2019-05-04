package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductBuilder {

    private Id aggregateId = Id.generate();
    private Money price = new Money(0);
    private String name = "Product";
    private ProductType productType = ProductType.STANDARD;

    public ProductBuilder setAggregateId(Id aggregateId) {
        this.aggregateId = aggregateId;
        return this;
    }

    public ProductBuilder setPrice(Money price) {
        this.price = price;
        return this;
    }

    public ProductBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder setProductType(ProductType productType) {
        this.productType = productType;
        return this;
    }

    public Product createProduct() {
        return new Product(aggregateId, price, name, productType);
    }
}
