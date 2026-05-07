package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.ImportsService;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.ProvincesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/import")
public class ImportsController {

    private final ImportsService importsService;
    private final ProvincesService provincesService;

    public ImportsController(ImportsService importsService, ProvincesService provincesService) {
        this.importsService = importsService;
        this.provincesService = provincesService;
    }

    @PostMapping("/provinces")
    public ResponseEntity<String> uploadProvinces(
            @RequestParam("file") MultipartFile file) {

        importsService.importProvince(file);
        return ResponseEntity.ok("Import completed successfully");
    }

    @PostMapping("/municipalities")
    public ResponseEntity<String> uploadMunicipalities(
            @RequestParam("file") MultipartFile file) {

        importsService.importMunicipality(file);
        return ResponseEntity.ok("Import completed successfully");
    }
}
