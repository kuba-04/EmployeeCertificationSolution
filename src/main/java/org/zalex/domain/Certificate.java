package org.zalex.domain;

import java.time.Instant;

public interface Certificate {
    CertificateId getCertificateId();

    Address getAddressTo();

    Purpose getPurpose();

    Instant getIssuedOn();

    EmployeeId getEmployeeId();
    Status getStatus();

    Certificate updatePurpose(Purpose purpose);
}
