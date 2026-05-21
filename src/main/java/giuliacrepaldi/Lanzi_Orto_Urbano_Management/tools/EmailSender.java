package giuliacrepaldi.Lanzi_Orto_Urbano_Management.tools;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.B2bProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.RegistrationRequest;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.RegisterB2bProfileDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class EmailSender {

    @Value("${resend.api-key}")
    private String apiKey;

    public void sendRegistrationEmail(RegistrationRequest req) {
        try {
            Resend resend = new Resend(apiKey);

            String verifyUrl = "http://localhost:3000/verify?token=" + req.getVerificationToken();

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("info@lanziortourbano.it")
                    .to(req.getEmail())
                    .subject("Conferma la tua registrazione")
                    .html("""
                            <h2>Benvenuto su Lanzi Orto Urbano</h2>
                            <p>Clicca il link qui sotto per verificare la tua mail e 
                            attivare l'account:</p>
                            <a href="%s"  style="background:#2d6a4f;color:white;padding:12px 24px;border-radius:8px;text-decoration:none;">
                            Verifica email
                            </a>
                            <p>Il link scade tra 24 ore.</p>
                            """.formatted(verifyUrl))
                    .build();

            CreateEmailResponse response = resend.emails().send(params);
            log.info("Email send successful", req.getEmail(), response.getId());

        } catch (ResendException e) {
            log.error("Email send failed for {}: {}", req.getEmail(), e.getMessage());
        }
    }


    //MAIL DOPO REGISTRAZIONE
    public void sendB2bPendingEmail(String toEmail, String contactName) {
        try {
            Resend resend = new Resend(apiKey);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("info@lanziortourbano.it")
                    .to(toEmail)
                    .subject("Richiesta ricevuta - In attesa di verifica")
                    .html("""
                            <h2>Ciao %s</h2>
                            <p>Abbiamo ricevuto la tua richiesta di registrazione come partner B2B</p>
                            <p>Il nostro team verificherà i tuoi dati aziendali entro <strong>12 ore</strong>.</p>
                            <p>Riceverai una email di conferma non appena il tuo account sarà attivato.</p>
                            """.formatted(contactName))
                    .build();

            resend.emails().send(params);
            log.info("B2B pending email sent to {}", toEmail);
        } catch (ResendException e) {
            log.error("B2B pending email failed for {}: {}", toEmail, e.getMessage());
        }
    }

    //MAIL PER VERIFICA B2B ADMIN
    public void notifyAdminForApproval(UUID userId, B2bProfile b2bProfile) {
        try {
            Resend resend = new Resend(apiKey);

            String approvalLink = "http://localhost:3001/auth/b2b/" + userId + "/approve";

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("info@lanziortourbano.it")
                    .to("info@lanziortourbano.it")
                    .subject("Nuova richiesta B2B da approvare")
                    .html("""
                            <h2>Nuova richiesta B2B</h2>
                                    <table>
                                                                                                                                   <tr><td><b>Azienda:</b></td><td>%s</td></tr>
                                                                                                                                    <tr><td><b>Contatto:</b></td><td>%s %s</td></tr>
                                                                                                                                    <tr><td><b>Email:</b></td><td>%s</td></tr>
                                                                                                                                    <tr><td><b>P.IVA:</b></td><td>%s</td></tr>
                                                                                                                                    <tr><td><b>Cod. Fiscale:</b></td><td>%s</td></tr>
                                                                                                                                    <tr><td><b>Tipo attività:</b></td><td>%s</td></tr>
                                                                                                                                </table>
                                                                                                                                <br>
                                                                                                                                <a href="%s" style="background-color:#15803d;color:white;padding:10px 20px;text-decoration:none;border-radius:5px;font-weight:bold;">
                                                                                                                                    APPROVA ACCOUNT B2B
                                                                                                                                </a>
                            """.formatted(
                            b2bProfile.getCompanyName(),
                            b2bProfile.getContactName(),
                            b2bProfile.getContactSurname(),
                            b2bProfile.getContactEmail(),
                            b2bProfile.getVatNumber(),
                            b2bProfile.getFiscalCode(),
                            b2bProfile.getTypeActivity(),
                            approvalLink
                    ))
                    .build();

            resend.emails().send(params);
            log.info("Admin notified for B2B approval: {}", b2bProfile.getContactEmail());
        } catch (ResendException e) {
            log.error("Admin notification failed: {}", e.getMessage());
        }
    }

    //MAIL VERIFICA UTENTE
    public void sendApprovalEmail(String toEmail, String contactName) {
        try {
            Resend resend = new Resend(apiKey);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("info@lanziortourbano.it")
                    .to(toEmail)
                    .subject("Account B2B approvato!")
                    .html("""
                            <h2>Ciao %s, il tuo account è stato approvato!</h2>
                            <p>Puoi ora accedere alla tua area riservata e iniziare a ordinare.</p>
                            <a href="http://localhost:3000/login" style="background:#2d6a4f;color:white;padding:12px 24px;border-radius:8px;text-decoration:none;">
                                Accedi ora
                            </a>
                            """.formatted(contactName))
                    .build();

            resend.emails().send(params);
        } catch (ResendException e) {
            log.error("Approval email failed for {}: {}", toEmail, e.getMessage());
        }
    }

    //MAIL RIFIUTO UTENTE
    public void sendRejectionEmail(String toEmail, String contactName, String reason) {
        try {
            Resend resend = new Resend(apiKey);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("info@lanziortourbano.it")
                    .to(toEmail)
                    .subject("Richiesta B2B non approvata")
                    .html("""
                            <h2>Ciao %s,</h2>
                                                <p>Purtroppo la tua richiesta di registrazione B2B non è stata approvata.</p>
                                                <p><b>Motivo:</b> %s</p>
                                                <p>Per maggiori informazioni contattaci a info@lanziortourbano.it</p>
                            """.formatted(contactName, reason != null ? reason : "Non specificato"))
                    .build();

            resend.emails().send(params);

        } catch (Exception e) {
            log.info("Rejection email failed for {}: {}", toEmail, e.getMessage());
        }
    }

    public void notifyAdminForApprovalFromRegistration(RegisterB2bProfileDTO body, String token) {
        try {
            Resend resend = new Resend(apiKey);

            String approvalLink = "http://localhost:3000/verify-b2b?token=" + token;

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("info@lanziortourbano.it")
                    .to("info@lanziortourbano.it")
                    .subject("Nuova richiesta B2B da approvare")
                    .html("""
                                                         <h2>Nuova richiesta B2B in attesa di verifica</h2>
                                                                            <table>
                                                                                <tr><td><b>Azienda:</b></td><td>%s</td></tr>
                                                                                <tr><td><b>Contatto:</b></td><td>%s %s</td></tr>
                                                                                <tr><td><b>Email:</b></td><td>%s</td></tr>
                                                                                <tr><td><b>P.IVA:</b></td><td>%s</td></tr>
                                                                                <tr><td><b>Cod. Fiscale:</b></td><td>%s</td></tr>
                                                                                <tr><td><b>Tipo attività:</b></td><td>%s</td></tr>
                                                                            </table>
                                                                            <br>
                                                                           <a href="http://localhost:3000/verify-b2b?token=%s"
                                                                               style="background-color: #15803d; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-weight: bold;">
                                                                               APPROVA ACCOUNT B2B
                                                                            </a>
                            """.formatted(
                            body.companyName(),
                            body.contactName(),
                            body.contactSurname(),
                            body.contactEmail(),
                            body.vatNumber(),
                            body.fiscalCode(),
                            body.typeActivity(),
                            token
                    ))
                    .build();

            resend.emails().send(params);
            log.info("Admin notified for B2B approval: {}", body.contactEmail());
        } catch (ResendException e) {
            log.error("Admin notification failed: {}", e.getMessage());
        }
    }

}
