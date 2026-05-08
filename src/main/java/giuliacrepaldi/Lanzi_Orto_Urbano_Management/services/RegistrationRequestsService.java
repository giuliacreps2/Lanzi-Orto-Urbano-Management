package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.RegistrationRequest;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.RegistrationRequestsDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.RegistrationRequestsRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistrationRequestsService {

    private final RegistrationRequestsRepository registrationRequestsRepository;

    public RegistrationRequestsService(RegistrationRequestsRepository registrationRequestsRepository) {
        this.registrationRequestsRepository = registrationRequestsRepository;
    }

    public RegistrationRequest saveRegReq(RegistrationRequestsDTO body) {
        return this.registrationRequestsRepository.save(new RegistrationRequest());
    }
}
