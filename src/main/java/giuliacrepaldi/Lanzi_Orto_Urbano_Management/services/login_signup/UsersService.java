package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.BadRequestException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup.UserDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.login_signup.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder bcrypt;
//    private final EmailSender emailSender;


    public UsersService(UsersRepository usersRepository, PasswordEncoder bcrypt) {
        this.usersRepository = usersRepository;
        this.bcrypt = bcrypt;
    }

    //creazione metodi
    //CREATE
    public User saveUser(UserDTO body) {

        //Controllo l'email
        if (this.usersRepository.existsByEmail(body.email()))
            throw new BadRequestException("Email" + body.email() + "already Exists");

        //Salvo
        User newUser = new User(body.email(), this.bcrypt.encode(body.password()));
        User savedUser = usersRepository.save(newUser);

        //this.emailSender.sendRegistrationEmail(savedUser);

        log.info("User with this id:" + savedUser.getUserId() + "has been saved successfully");
        return savedUser;
    }

    //REQUESTS
    public User findById(UUID userId) {
        return this.usersRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with this id:" + userId + "not found"));
    }


    public User findByEmail(String email) {
        return this.usersRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with this email" + email + "not found"));
    }


    public Page<User> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.usersRepository.findAll(pageable);
    }


    //UPDATE
    public User findByIdAndUpdate(UUID userId, UserDTO body) {
        //Controllo se l'utente esiste
        if (!this.usersRepository.existsById(userId))
            throw new NotFoundException("User with this id:" + userId + "not found");
        User found = this.findById(userId);

        if (!found.getEmail().equals(body.email())) {
            if (this.usersRepository.existsByEmail(body.email()))
                throw new BadRequestException("Email" + body.email() + "already exists");
        }

        found.setPassword(this.bcrypt.encode(body.password()));
        found.setEmail(body.email());

        User updatedUser = this.usersRepository.save(found);

        log.info("User with this id:" + updatedUser.getUserId() + "has been updated successfully");

        return updatedUser;
    }

    //DELETE
    public void findByIdAndDelete(UUID userId) {
        if (!this.usersRepository.existsById(userId))
            throw new NotFoundException("User with this id:" + userId + "not found");
        User found = this.findById(userId);

        log.info("User with this id:" + found.getUserId() + "has been deleted successfully");
        this.usersRepository.delete(found);
    }

}
