package org.zalex.adapter.outbound.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.zalex.domain.*;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "certificates")
public class CertificateDao {

    @Id
    private UUID id;
    @Column(name = "address")
    private String address;
    @Column(name = "purpose")
    private String purpose;
    @Column(name = "issued")
    private Instant issued;
    @Column(name = "eid")
    private String eid;
    @Column(name = "status")
    private String status;

    public CertificateDao() {
    }

    public CertificateDao(final UUID id, final String address, final String purpose, final Instant issued, final String eid, final String status) {
        this.id = id;
        this.address = address;
        this.purpose = purpose;
        this.issued = issued;
        this.eid = eid;
        this.status = status;
    }

    public static CertificateDao from(final Certificate certificate) {
        return new CertificateDao(
                certificate.getCertificateId().id(),
                certificate.getAddressTo().value(),
                certificate.getPurpose().value(),
                certificate.getIssuedOn(),
                certificate.getEmployeeId().id(),
                certificate.getStatus().name()
        );
    }

    public Certificate toCertificate() {
        final Purpose p;
        try {
            p = new Purpose(this.purpose);
        } catch (DomainObjectCreationException e) {
            throw new IllegalStateException("A serious problem occurred: db data is corrupted", e);
        }
        if (getStatus() == Status.PENDING) {
            return new PendingCertificate(
                    new CertificateId(this.id),
                    new Address(this.address),
                    p,
                    this.issued,
                    new EmployeeId(this.eid)
            );
        } else {
            //TODO: not in the requirements yet
            throw new IllegalStateException("Not implemented");
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public Status getStatus() {
        return Status.valueOf(status);
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(final String purpose) {
        this.purpose = purpose;
    }

    public Instant getIssued() {
        return issued;
    }

    public void setIssued(final Instant issued) {
        this.issued = issued;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(final String eid) {
        this.eid = eid;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final CertificateDao that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(address, that.address) && Objects.equals(purpose, that.purpose) && Objects.equals(issued, that.issued) && Objects.equals(eid, that.eid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, purpose, issued, eid);
    }


}
