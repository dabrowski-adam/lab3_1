package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

public class ProductDataBuilder {

    private Id productId = Id.generate();
    private Money price = new Money(0);
    private String name = "Product";
    private ProductType type = ProductType.STANDARD;
    private Date snapshotDate = new Date();

    public ProductDataBuilder setProductId(Id productId) {
        this.productId = productId;
        return this;
    }

    public ProductDataBuilder setPrice(Money price) {
        this.price = price;
        return this;
    }

    public ProductDataBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ProductDataBuilder setType(ProductType type) {
        this.type = type;
        return this;
    }

    public ProductDataBuilder setSnapshotDate(Date snapshotDate) {
        this.snapshotDate = snapshotDate;
        return this;
    }

    public ProductData createProductData() {
        return new ProductData(productId, price, name, type, snapshotDate);
    }
}
