package org.zalex.application;

import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.Pageable;
import org.zalex.adapter.outbound.db.CertificateDao;
import org.zalex.adapter.outbound.db.CertificateRepository;
import org.zalex.domain.Certificate;
import org.zalex.domain.PendingCertificate;
import org.zalex.domain.Purpose;
import org.zalex.domain.RequestCertificateCommand;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class CertificateService {

    private final CertificateRepository repository;

    public CertificateService(CertificateRepository repository) {
        this.repository = repository;
    }

    public void requestCertificate(final RequestCertificateCommand command) {
        final var certificate = new PendingCertificate(
                command.certificateId(),
                command.addressTo(),
                command.purpose(),
                command.issuedOn(),
                command.employeeId()
        );
        repository.save(CertificateDao.from(certificate));
    }

    public Stream<Certificate> getCertificates(final Pageable pageable, final Map<String, String> filter) {
        return repository.findAll(pageable)
                .filter(cert -> filterAddressTo(filter, cert))
                .filter(cert -> filterId(filter, cert))
                .filter(cert -> filterStatus(filter, cert))
                .stream()
                .map(CertificateDao::toCertificate);
    }

    private static boolean filterAddressTo(final Map<String, String> filter, final CertificateDao cert) {
        final var address = filter.get("address_to");
        if (address != null && !address.isBlank()) {
            return cert.getAddress().contains(address);
        }
        return true;
    }

    private static boolean filterId(final Map<String, String> filter, final CertificateDao cert) {
        final var refId = filter.get("reference_number");
        if (refId != null && !refId.isBlank()) {
            return cert.getId().toString().equals(refId);
        }
        return true;
    }

    private static boolean filterStatus(final Map<String, String> filter, final CertificateDao cert) {
        final var status = filter.get("status");
        if (status != null && !status.isBlank()) {
            return cert.getStatus().name().equals(status);
        }
        return true;
    }

    public Optional<Certificate> getCertificate(final String id) {
        return repository.findById(UUID.fromString(id)).map(CertificateDao::toCertificate);
    }

    public void updatePurpose(final UUID id, final Purpose purpose) throws ObjectNotFoundException {
        final var certificate = repository.findById(id).map(CertificateDao::toCertificate)
                .orElseThrow(() -> new ObjectNotFoundException(id, "certificate"));
        final var updated = certificate.updatePurpose(purpose);
        repository.save(CertificateDao.from(updated));

    }
}
