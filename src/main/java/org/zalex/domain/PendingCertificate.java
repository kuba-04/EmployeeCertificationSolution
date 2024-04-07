package org.zalex.domain;

import java.time.Instant;
import java.util.Objects;

public class PendingCertificate implements Certificate {
    private final CertificateId certificateId;
    private final Address addressTo;
    private Purpose purpose;
    private final Instant issuedOn;
    private final EmployeeId employeeId;


    public PendingCertificate(final CertificateId certificateId, final Address addressTo, final Purpose purpose, final Instant issuedOn, final EmployeeId employeeId) {
        this.certificateId = certificateId;
        this.addressTo = addressTo;
        this.purpose = purpose;
        this.issuedOn = issuedOn;
        this.employeeId = employeeId;
    }

    public CertificateId getCertificateId() {
        return certificateId;
    }

    public Address getAddressTo() {
        return addressTo;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public Instant getIssuedOn() {
        return issuedOn;
    }

    public EmployeeId getEmployeeId() {
        return employeeId;
    }

    @Override
    public Certificate updatePurpose(final Purpose purpose) {
        this.purpose = purpose;
        return new PendingCertificate(certificateId, addressTo, purpose, issuedOn, employeeId);
    }

    public Status getStatus() {
        return Status.PENDING;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final PendingCertificate that)) return false;
        return Objects.equals(certificateId, that.certificateId) && Objects.equals(addressTo, that.addressTo) && Objects.equals(purpose, that.purpose) && Objects.equals(issuedOn, that.issuedOn) && Objects.equals(employeeId, that.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certificateId, addressTo, purpose, issuedOn, employeeId);
    }
}

