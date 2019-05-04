package pl.com.bottega.ecommerce.sales.application.api.command;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

public class AddProductCommandBuilder {

    private Id orderId = Id.generate();
    private Id productId = Id.generate();
    private int quantity = 1;

    public AddProductCommandBuilder setOrderId(Id orderId) {
        this.orderId = orderId;
        return this;
    }

    public AddProductCommandBuilder setProductId(Id productId) {
        this.productId = productId;
        return this;
    }

    public AddProductCommandBuilder setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public AddProductCommand createAddProductCommand() {
        return new AddProductCommand(orderId, productId, quantity);
    }
}
