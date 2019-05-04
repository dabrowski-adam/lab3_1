package pl.com.bottega.ecommerce.sales.domain.reservation;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientDataBuilder;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

import java.util.Date;

public class ReservationBuilder {

    private Id aggregateId = Id.generate();
    private Reservation.ReservationStatus status = Reservation.ReservationStatus.OPENED;
    private ClientData clientData = new ClientDataBuilder().createClientData();
    private Date createDate = new Date();

    public ReservationBuilder setAggregateId(Id aggregateId) {
        this.aggregateId = aggregateId;
        return this;
    }

    public ReservationBuilder setStatus(Reservation.ReservationStatus status) {
        this.status = status;
        return this;
    }

    public ReservationBuilder setClientData(ClientData clientData) {
        this.clientData = clientData;
        return this;
    }

    public ReservationBuilder setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    public Reservation createReservation() {
        return new Reservation(aggregateId, status, clientData, createDate);
    }
}
