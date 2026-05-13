package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.Municipality;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.Province;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.BadRequestException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.login_signup.MunicipalitiesRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.login_signup.ProvincesRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

@Service
@Slf4j
public class ImportsService {

    private static final Map<String, String> PROVINCE_MAP = Map.ofEntries(
            Map.entry("Valle d'Aosta/Vallée d'Aoste", "Aosta"),
            Map.entry("Bolzano/Bozen", "Bolzano"),
            Map.entry("Forlì-Cesena", "Forli-Cesena"),
            Map.entry("Monza e della Brianza", "Monza-Brianza"),
            Map.entry("Reggio nell'Emilia", "Reggio-Emilia"),
            Map.entry("Reggio di Calabria", "Reggio-Calabria"),
            Map.entry("Pesaro e Urbino", "Pesaro-Urbino"),
            Map.entry("Vibo Valentia", "Vibo-Valentia"),
            Map.entry("Ascoli Piceno", "Ascoli-Piceno"),
            Map.entry("La Spezia", "La-Spezia"),
            Map.entry("Massa-Carrara", "Massa-Carrara")
    );

    private final MunicipalitiesRepository municipalitiesRepository;
    private final ProvincesRepository provincesRepository;

    public ImportsService(MunicipalitiesRepository municipalitiesRepository, ProvincesRepository provincesRepository) {
        this.municipalitiesRepository = municipalitiesRepository;
        this.provincesRepository = provincesRepository;
    }

    @Transactional
    public void importProvince(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("This file is empty. No province was found!");
        }
        try (Scanner scanner = new Scanner(file.getInputStream(), StandardCharsets.UTF_8)) {
            if (scanner.hasNextLine()) scanner.nextLine();

            while (scanner.hasNextLine()) {
                String[] columns = scanner.nextLine().split(";");
                if (columns.length < 2) continue;

                String acronym = columns[0].trim();
                String provinceName = columns[1].trim();

                if (provincesRepository.findByAcronym(acronym).isEmpty()) {
                    Province p = new Province();
                    p.setAcronym(acronym);
                    p.setProvinceName(provinceName);
                    provincesRepository.save(p);
                }
            }
        } catch (IOException e) {
            throw new BadRequestException("An error occurred while reading the file!");
        }
    }

    @Transactional
    public void importMunicipality(MultipartFile file) {
        if (file.isEmpty()) throw new BadRequestException("This file is empty. No municipality was found!");

        try (Scanner scanner = new Scanner(file.getInputStream(), StandardCharsets.UTF_8)) {
            if (scanner.hasNextLine()) {
                String header = scanner.nextLine();
                log.info("Header CSV: " + header);
            }

            int saved = 0;
            int skipped = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) continue;

                String[] columns = line.split(";");
                log.info("Colonne trovate: " + columns.length + " → " + Arrays.toString(columns));
                if (columns.length < 4) {
                    skipped++;
                    continue;
                }

                String MunicipalityName = columns[2].trim();
                String nomeProvinciaCSV = columns[3].trim();
                String nomeProvinciaCorretto = PROVINCE_MAP.getOrDefault(nomeProvinciaCSV, nomeProvinciaCSV);

                Province p = provincesRepository.findByProvinceName(nomeProvinciaCorretto).orElse(null);

                if (p == null) {
                    log.warn("Province not found: " + nomeProvinciaCSV + " → " + nomeProvinciaCorretto);
                    skipped++;
                    continue;
                }

                if (municipalitiesRepository.findByMunicipalityNameIgnoreCase(MunicipalityName).isEmpty()) {
                    Municipality m = new Municipality();
                    m.setMunicipalityName(MunicipalityName);
                    m.setProvince(p);
                    municipalitiesRepository.save(m);
                    saved++;
                }
            }

            log.info("Import completato → salvati: " + saved + ", saltati: " + skipped);

        } catch (IOException e) {
            throw new BadRequestException("An error occurred while reading the file!");
        }
    }
}
