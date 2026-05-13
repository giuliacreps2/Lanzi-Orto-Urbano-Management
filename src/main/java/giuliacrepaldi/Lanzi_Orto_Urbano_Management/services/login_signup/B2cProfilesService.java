package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.B2cProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup.B2cProfileDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.login_signup.B2cProfilesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class B2cProfilesService {

    private final B2cProfilesRepository b2cProfilesRepository;

    public B2cProfilesService(B2cProfilesRepository b2cProfilesRepository) {
        this.b2cProfilesRepository = b2cProfilesRepository;
    }

    //REQUESTS
    public B2cProfile findById(UUID b2cProfileId) {
        return this.b2cProfilesRepository.findById(b2cProfileId).orElseThrow(() -> new NotFoundException("Profile not found"));
    }

    public B2cProfile findByUserId(UUID userId) {
        return this.b2cProfilesRepository.findByUser_UserId(userId).orElseThrow(() -> new NotFoundException("Profile not found"));
    }

    public Page<B2cProfile> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy));
        return this.b2cProfilesRepository.findAll(pageRequest);
    }

    //UPDATE
    public B2cProfile findByIdAndUpdateB2cProfile(UUID b2cProfileId, B2cProfileDTO body) {
        B2cProfile found = this.findById(b2cProfileId);

//        if (!found.getB2cProfileId().equals(body.))
//            throw new NotFoundException("B2cProfile not found");

        found.setName(body.name());
        found.setSurname(body.surname());
        found.setPhoneNumber(body.phoneNumber());
        found.setAvatar("https://ui-avatars.com/api?name=" + body.name() + "+" + body.surname());

        B2cProfile updated = this.b2cProfilesRepository.save(found);

        log.info("B2cProfile {} updated", updated.getB2cProfileId());

        return updated;
    }

    //UPDATE BIRTH DATE BY ADMIN
    public B2cProfile adminUpdateB2cProfile(UUID b2cProfileId, B2cProfileDTO body) {
        B2cProfile found = this.findById(b2cProfileId);


        found.setName(body.name());
        found.setSurname(body.surname());
        found.setPhoneNumber(body.phoneNumber());
        found.setBirthDate(body.birthDate());
        found.setAvatar("https://ui-avatars.com/api?name=" + body.name() + "+" + body.surname());

        return this.b2cProfilesRepository.save(found);
    }
}
