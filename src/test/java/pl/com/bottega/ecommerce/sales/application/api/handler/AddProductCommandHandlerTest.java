package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommandBuilder;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationBuilder;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AddProductCommandHandlerTest {

    private Reservation reservation;
    private ReservationRepository reservationRepository;
    private Product product;
    private ProductRepository productRepository;
    private Product suggestedProduct;
    private SuggestionService suggestionService;
    private ClientRepository clientRepository;
    private AddProductCommandHandler addProductCommandHandler;

    @Before
    public void setup() {
        reservationRepository = mock(ReservationRepository.class);
        reservation = new ReservationBuilder().createReservation();
        when(reservationRepository.load(any())).thenReturn(reservation);

        productRepository = mock(ProductRepository.class);
        product = new ProductBuilder().createProduct();
        when(productRepository.load(any())).thenReturn(product);

        suggestionService = mock(SuggestionService.class);
        suggestedProduct = new ProductBuilder().createProduct();
        when(suggestionService.suggestEquivalent(any(), any())).thenReturn(suggestedProduct);

        clientRepository = mock(ClientRepository.class);
        Client client = new Client();
        when(clientRepository.load(any())).thenReturn(client);

        addProductCommandHandler = new AddProductCommandHandlerBuilder()
                .setReservationRepository(reservationRepository)
                .setProductRepository(productRepository)
                .setSuggestionService(suggestionService)
                .setClientRepository(clientRepository)
                .createAddProductCommandHandler();
    }

    @Test
    public void handleShouldLoadReservationForOrder() {
        AddProductCommand command = new AddProductCommandBuilder().createAddProductCommand();
        addProductCommandHandler.handle(command);

        verify(reservationRepository).load(command.getOrderId());
    }

    @Test
    public void handleShouldLoadProductRepositoryForOrder() {
        AddProductCommand command = new AddProductCommandBuilder().createAddProductCommand();
        addProductCommandHandler.handle(command);

        verify(productRepository).load(command.getProductId());
    }

    @Test
    public void handleShouldNotLoadSuggestionWhenProductAvailable() {
        AddProductCommand command = new AddProductCommandBuilder().createAddProductCommand();
        addProductCommandHandler.handle(command);

        verify(suggestionService, never()).suggestEquivalent(any(), any());
    }

    @Test
    public void handleShouldSaveReservation() {
        AddProductCommand command = new AddProductCommandBuilder().createAddProductCommand();
        addProductCommandHandler.handle(command);

        verify(reservationRepository).save(any());
    }

    @Test
    public void handleShouldAddProductToReservationWhenAvailable() {
        AddProductCommand command = new AddProductCommandBuilder().createAddProductCommand();
        addProductCommandHandler.handle(command);

        assertThat(reservation.contains(product), is(true));
    }

    @Test
    public void handleShouldNotAddProductToReservationWhenUnavailable() {
        when(productRepository.load(any(Id.class))).thenReturn(product);

        AddProductCommand command = new AddProductCommandBuilder().createAddProductCommand();
        addProductCommandHandler.handle(command);

        assertThat(reservation.contains(suggestedProduct), is(false));
    }
}
