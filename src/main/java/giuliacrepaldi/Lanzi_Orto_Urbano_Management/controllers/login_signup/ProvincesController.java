package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.Province;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup.ProvincesService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/provinces")
public class ProvincesController {

    private final ProvincesService provincesService;

    public ProvincesController(ProvincesService provincesService) {
        this.provincesService = provincesService;
    }


    @GetMapping
    public Page<Province> findAllProvinces(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "name") String sortBy) {
        return this.provincesService.findAll(page, size, sortBy);
    }
}