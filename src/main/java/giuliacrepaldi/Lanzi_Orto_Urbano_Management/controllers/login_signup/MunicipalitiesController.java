package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.Municipality;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup.MunicipalitiesService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/municipalities")
public class MunicipalitiesController {

    private final MunicipalitiesService municipalitiesService;

    public MunicipalitiesController(MunicipalitiesService municipalitiesService) {
        this.municipalitiesService = municipalitiesService;
    }

    @GetMapping
    public Page<Municipality> findAllMunicipalities(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "name") String sortBy) {
        return this.municipalitiesService.findAll(page, size, sortBy);
    }

    @GetMapping("/search")
    public List<Municipality> searchByMunicipalityName(@RequestParam String municipalityName) {
        return this.municipalitiesService.findByMunicipalityName(municipalityName);
    }

}
