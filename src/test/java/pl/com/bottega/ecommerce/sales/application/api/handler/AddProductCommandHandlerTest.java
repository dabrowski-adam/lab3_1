package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientDataBuilder;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.system.application.SystemContext;
import pl.com.bottega.ecommerce.system.application.SystemUser;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AddProductCommandHandlerTest {

    private Reservation reservation;
    private ReservationRepository reservationRepository;
    Product product;
    private ProductRepository productRepository;
    Product suggestedProduct;
    private SuggestionService suggestionService;
    private ClientRepository clientRepository;
    private SystemContext systemContext;


    @Before
    public void setup() {
        reservationRepository = mock(ReservationRepository.class);
        ClientData clientData = new ClientDataBuilder().createClientData();
        reservation = new Reservation(Id.generate(), Reservation.ReservationStatus.OPENED, clientData, new Date());
        when(reservationRepository.load(any())).thenReturn(reservation);

        productRepository = mock(ProductRepository.class);
        product = new Product(Id.generate(), new Money(0), "", ProductType.STANDARD);
        when(productRepository.load(any())).thenReturn(product);

        suggestionService = mock(SuggestionService.class);
        suggestedProduct = new Product(Id.generate(), new Money(0), "", ProductType.STANDARD);
        when(suggestionService.suggestEquivalent(any(), any())).thenReturn(suggestedProduct);

        clientRepository = mock(ClientRepository.class);
        Client client = new Client();
        when(clientRepository.load(any())).thenReturn(client);

        systemContext = mock(SystemContext.class);
        SystemUser systemUser = new SystemUser(Id.generate());
        when(systemContext.getSystemUser()).thenReturn(systemUser);
    }

    @Test
    public void handleShouldLoadReservationForOrder() {
        AddProductCommandHandler addProductCommandHandler = new AddProductCommandHandler(reservationRepository,
                productRepository, suggestionService, clientRepository, systemContext);

        AddProductCommand command = new AddProductCommand(Id.generate(), Id.generate(), 1);
        addProductCommandHandler.handle(command);

        verify(reservationRepository).load(command.getOrderId());
    }

    @Test
    public void handleShouldLoadProductRepositoryForOrder() {
        AddProductCommandHandler addProductCommandHandler = new AddProductCommandHandler(reservationRepository,
                productRepository, suggestionService, clientRepository, systemContext);

        AddProductCommand command = new AddProductCommand(Id.generate(), Id.generate(), 1);
        addProductCommandHandler.handle(command);

        verify(productRepository).load(command.getProductId());
    }

    @Test
    public void handleShouldNotLoadSuggestionWhenProductAvailable() {
        AddProductCommandHandler addProductCommandHandler = new AddProductCommandHandler(reservationRepository,
                productRepository, suggestionService, clientRepository, systemContext);

        AddProductCommand command = new AddProductCommand(Id.generate(), Id.generate(), 1);
        addProductCommandHandler.handle(command);

        verify(suggestionService, never()).suggestEquivalent(any(), any());
    }

    @Test
    public void handleShouldSaveReservation() {
        AddProductCommandHandler addProductCommandHandler = new AddProductCommandHandler(reservationRepository,
                productRepository, suggestionService, clientRepository, systemContext);

        AddProductCommand command = new AddProductCommand(Id.generate(), Id.generate(), 1);
        addProductCommandHandler.handle(command);

        verify(reservationRepository).save(any());
    }

    @Test
    public void handleShouldAddProductToReservationWhenAvailable() {
        AddProductCommandHandler addProductCommandHandler = new AddProductCommandHandler(reservationRepository,
                productRepository, suggestionService, clientRepository, systemContext);

        AddProductCommand command = new AddProductCommand(Id.generate(), Id.generate(), 1);
        addProductCommandHandler.handle(command);

        assertThat(reservation.contains(product), is(true));
    }

    @Test
    public void handleShouldNotAddProductToReservationWhenUnavailable() {
        AddProductCommandHandler addProductCommandHandler = new AddProductCommandHandler(reservationRepository,
                productRepository, suggestionService, clientRepository, systemContext);

        when(productRepository.load(any(Id.class))).thenReturn(product);

        AddProductCommand command = new AddProductCommand(Id.generate(), Id.generate(), 1);
        addProductCommandHandler.handle(command);

        assertThat(reservation.contains(suggestedProduct), is(false));
    }
}
