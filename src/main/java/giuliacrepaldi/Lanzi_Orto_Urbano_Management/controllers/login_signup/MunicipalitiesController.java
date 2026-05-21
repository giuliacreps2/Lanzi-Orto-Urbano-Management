package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.Municipality;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup.MunicipalitiesService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/municipalities")
public class MunicipalitiesController {

    private final MunicipalitiesService municipalitiesService;
    private final MunicipalitiesRepository municipalitiesRepository;

    public MunicipalitiesController(MunicipalitiesService municipalitiesService, MunicipalitiesRepository municipalitiesRepository) {
        this.municipalitiesService = municipalitiesService;
        this.municipalitiesRepository = municipalitiesRepository;
    }

    @GetMapping
    public Page<Municipality> findAllMunicipalities(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "name") String sortBy) {
        return this.municipalitiesService.findAll(page, size, sortBy);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Municipality> searchByMunicipalityName(@RequestParam String municipalityName) {
        return this.municipalitiesService.findByMunicipalityName(municipalityName);
    }

//    @GetMapping("/search")
//    @ResponseStatus(HttpStatus.OK)
//    public List<Municipality> findAllMunicipalities() {
//        return this.municipalitiesRepository.findAll();
//    }

}
