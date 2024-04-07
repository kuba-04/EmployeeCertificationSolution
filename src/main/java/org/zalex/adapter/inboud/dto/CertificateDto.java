package org.zalex.adapter.inboud.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.zalex.domain.Certificate;
import org.zalex.domain.PendingCertificate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

public record CertificateDto(
        @JsonProperty(value = "id")
        UUID certificateId,
        @JsonProperty(value = "address_to")
        String addressTo,
        @JsonProperty(value = "purpose")
        String purpose,
        @JsonProperty(value = "issued_on")
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/M/yyyy")
        LocalDate issuedOn,
        @JsonProperty(value = "employee_id")
        String employeeId
) {
    public static CertificateDto from(Certificate certificate) {
        return new CertificateDto(
                certificate.getCertificateId().id(),
                certificate.getAddressTo().value(),
                certificate.getPurpose().value(),
                LocalDate.ofInstant(certificate.getIssuedOn(), ZoneId.systemDefault()),
                certificate.getEmployeeId().id());
    }
}
