package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.RegistrationRequest;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup.RegistrationRequestsDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.login_signup.RegistrationRequestsRepository;
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
