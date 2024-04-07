package org.zalex.adapter.inboud;

import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.zalex.adapter.inboud.dto.*;
import org.zalex.application.CertificateService;
import org.zalex.application.IdGenerator;
import org.zalex.domain.DomainObjectCreationException;
import org.zalex.domain.Purpose;
import org.zalex.domain.RequestCertificateCommand;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequestMapping(value = "/api/certificates")
@RestController
public class CertificateController {

    private static final List<String> ALLOWED_SORT_FIELDS = List.of("issued_on", "status");
    private final CertificateService service;
    private final IdGenerator idGenerator;

    public CertificateController(final CertificateService service, final IdGenerator idGenerator) {
        this.service = service;
        this.idGenerator = idGenerator;
    }

    @PostMapping
    ResponseEntity<?> requestCertificate(@RequestBody RequestCertificateDto dto) {
        final RequestCertificateCommand command;
        try {
            command = RequestCertificateCommand.from(dto, idGenerator);
            service.requestCertificate(command);
            return ResponseEntity.of(Optional.of(new CertificateIdDto(command.certificateId().id())));
        } catch (DomainObjectCreationException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    @GetMapping
    List<CertificateDto> getCertificates(
            @RequestParam(required = false, name = "page", defaultValue = "0") Integer page,
            @RequestParam(required = false, name = "size", defaultValue = "10") Integer size,
            @RequestParam(required = false, name = "sort_field", defaultValue = "issued_on") String sortField,
            @RequestParam(required = false, name = "sort_direction", defaultValue = "DESC") Sort.Direction sortDirection,
            @RequestParam(required = false, name = "reference_number", defaultValue = "") String referenceNumber,
            @RequestParam(required = false, name = "address_to", defaultValue = "") String addressTo,
            @RequestParam(required = false, name = "status", defaultValue = "") String status) {
        validateSortParameter(sortField);
        final var sort = Sort.by(sortDirection, sortField.split("_")[0]);
        final var filters = Map.of("reference_number", referenceNumber, "address_to", addressTo, "status", status);
        return service.getCertificates(PageRequest.of(page, size, sort), filters).map(CertificateDto::from).toList();
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getOneCertificate(@PathVariable(name = "id") String id) {
        final var cert = service.getCertificate(id).map(CertificateDto::from);
        if (cert.isPresent()) {
            return ResponseEntity.of(cert);
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not found"));
        }
    }

    @PatchMapping("/{id}")
    ResponseEntity<?> updatePurpose(
            @PathVariable(name = "id") String id,
            @RequestBody UpdatePurposeDto dto) {
        try {
            service.updatePurpose(UUID.fromString(id), new Purpose(dto.purpose()));
            return ResponseEntity.ok().build();
        } catch (DomainObjectCreationException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }
    }

    private void validateSortParameter(final String sortField) {
        if (!ALLOWED_SORT_FIELDS.contains(sortField)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid sort field");
        }
    }

}
