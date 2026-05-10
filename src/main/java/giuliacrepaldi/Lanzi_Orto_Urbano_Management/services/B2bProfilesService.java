package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.B2bProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.BadRequestException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.B2bProfileDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.B2bProfilesRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.utilities.B2bProfileSpecs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class B2bProfilesService {

    private final B2bProfilesRepository b2bProfilesRepository;

    public B2bProfilesService(B2bProfilesRepository b2bProfilesRepository) {
        this.b2bProfilesRepository = b2bProfilesRepository;
    }

    //REQUESTS
    public B2bProfile findById(UUID b2bProfileId) {
        return this.b2bProfilesRepository.findById(b2bProfileId).orElseThrow(() -> new NotFoundException("B2b profile not found"));
    }

    public B2bProfile findByUserId(UUID userId) {
        return this.b2bProfilesRepository.findByUser_UserId(userId).orElseThrow(() -> new NotFoundException("Profile not found for this b2bUser"));
    }

    public Page<B2bProfile> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy));
        return this.b2bProfilesRepository.findAll(pageRequest);
    }

    public Page<B2bProfile> search(String vatNumber, String fiscalCode, String contactPhone,
                                   String contactName, String provinceName, String contactSurname, String contactEmail,
                                   String companyName, Pageable pageable) {
        Specification<B2bProfile> spec = (root, query, builder) -> builder.conjunction();

        spec = spec.and(B2bProfileSpecs.vatNumberStartsWith(vatNumber));
        spec = spec.and(B2bProfileSpecs.fiscalCodeStartsWith(fiscalCode));
        spec = spec.and(B2bProfileSpecs.hasContactName(contactName));
        spec = spec.and(B2bProfileSpecs.hasContactSurname(contactSurname));
        spec = spec.and(B2bProfileSpecs.contactPhoneStartsWith(contactPhone));
        spec = spec.and(B2bProfileSpecs.hasCompanyName(companyName));
        spec = spec.and(B2bProfileSpecs.livesInProvinces(provinceName));
        spec = spec.and(B2bProfileSpecs.hasContactEmail(contactEmail));

        return this.b2bProfilesRepository.findAll(spec, pageable);
    }


    //UPDATE
    public B2bProfile findByIdAndUpdate(UUID b2bProfileId, B2bProfileDTO body) {
        B2bProfile found = this.b2bProfilesRepository.findById(b2bProfileId).orElseThrow(() -> new NotFoundException(b2bProfileId));

        if (!found.getContactEmail().equals(body.contactEmail())) {
            if (this.b2bProfilesRepository.existsByContactEmail(body.contactEmail()))
                throw new BadRequestException("Email:" + body.contactEmail() + "already exists");
        }

        found.setCompanyName(body.companyName());
        found.setContactPhone(body.contactPhone());
        found.setContactEmail(body.contactEmail());
        found.setContactName(body.contactName());
        found.setContactSurname(body.contactSurname());
        found.setLegalAddress(body.legalAddress());

        B2bProfile updatedB2bProfile = this.b2bProfilesRepository.save(found);

        log.info("User with id " + b2bProfileId + " updated successfully");

        return updatedB2bProfile;
    }

    //UPDATE VAT NUMBER AND FISCAL CODE BY ADMIN
    public B2bProfile findByIdAndUpdateB2bProfile(UUID b2bProfileId, B2bProfileDTO body) {
        B2bProfile found = this.findById(b2bProfileId);

        found.setCompanyName(body.companyName());
        found.setContactPhone(body.contactPhone());
        found.setContactEmail(body.contactEmail());
        found.setContactName(body.contactName());
        found.setContactSurname(body.contactSurname());
        found.setVatNumber(body.vatNumber());
        found.setFiscalCode(body.fiscalCode());
        found.setTypeActivity(body.typeActivity());

        return this.b2bProfilesRepository.save(found);
    }


    //public Page<B2bProfile> search(String contactName, String contactSurname, String vatNumber, String contactPhone, String provinceName, String companyName, Pageable pageable) {
    //}
}
