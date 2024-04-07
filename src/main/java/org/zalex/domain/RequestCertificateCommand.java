package org.zalex.domain;

import org.zalex.adapter.inboud.dto.RequestCertificateDto;
import org.zalex.application.IdGenerator;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public record RequestCertificateCommand(
        CertificateId certificateId,
        Address addressTo,
        Purpose purpose,
        Instant issuedOn,
        EmployeeId employeeId) {

    public static RequestCertificateCommand from(final RequestCertificateDto dto, final IdGenerator idGenerator) throws DomainObjectCreationException {
        return new RequestCertificateCommand(
                new CertificateId(idGenerator.generate()),
                new Address(dto.address_to()),
                new Purpose(dto.purpose()),
                validateAndConvertDate(dto.issued_on()),
                new EmployeeId(dto.employee_id()));
    }

    private static Instant validateAndConvertDate(final LocalDate date) throws DomainObjectCreationException {
        if (!date.isAfter(LocalDate.now())) {
            throw new DomainObjectCreationException("Issued date cannot be in the past");
        }
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }
}
