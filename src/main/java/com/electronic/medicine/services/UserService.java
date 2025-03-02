package com.electronic.medicine.services;

import com.electronic.medicine.DTO.RegistrationUserDto;
import com.electronic.medicine.entity.Role;
import com.electronic.medicine.entity.User;
import com.electronic.medicine.exception.MedicineBadCredential;
import com.electronic.medicine.exception.MedicineNotFound;
import com.electronic.medicine.exception.MedicineServerErrorException;
import com.electronic.medicine.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.util.Optionals;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    //TODO СЕРВЕР ССЫЛКА
    private final static String HOST = "http://localhost:/api/activate";

    private final int passwordLength = 6;

    private final UserRepository userRepository;

    private final RoleService roleService;

    private PasswordEncoder passwordEncoder;

    private final MailSender mailSender;


    public List<User> getAllUsers() {
        Role userRole = roleService.getByTitle("USER");
        return userRepository.findAllByRoles(userRole);
    }

    public User getByEmail(String email) {
        Optional<User> user = userRepository.getByEmail(email);
        return user.orElse(null);
    }

    public User getByActivationCode(String code) {
        Optional<User> exist = userRepository.getByActivationCode(code);
        if (exist.isEmpty()) {
            throw new MedicineBadCredential("Неправильный код активации, проверьте код на почте и попробуйте еще раз");
        } else {
            return exist.get();
        }
    }

    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new MedicineNotFound("Пользователь c указанным id не найден");
        } else {
            return user.get();
        }
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getByEmail(username);
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getTitle())).collect(Collectors.toList()));
    }


    public void createNewUsers(RegistrationUserDto registrationUserDto) {
        checkEqualsPassword(registrationUserDto);
        checkEmail(registrationUserDto.getEmail());
        checkPasswordLength(registrationUserDto.getPassword());
        checkExistUsers(registrationUserDto);

        User user = new User();
        user.setEmail(registrationUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setRoles(Set.of(roleService.getByTitle("USER")));
        user.setActivationCode(UUID.randomUUID().toString());
        log.debug("Пользователь логин:{} пароль:{} создан.", registrationUserDto.getEmail(), registrationUserDto.getPassword());
        sendActivationCodeToNewUser(user);
        saveUser(user);
    }

    public void sendActivationCodeToNewUser(User user) {
        try {
            StringBuilder message = new StringBuilder();
            message.append("<h4>Добро пожаловать ").append(user.getEmail()).append(". Ваш код активации ").append(user.getActivationCode()).append("</h4>");
            message.append("<span>Вы также можете посетить страницу активации по адресу <a href=").append(HOST).append(">Ваша ссылка</a> </span>");

            mailSender.sendActivateCodeMessage(user.getEmail(), "Код активации для продолжения регистрации на " +
                    "портале Electronic Medicine", message.toString());
        } catch (Exception e) {
            throw new MedicineServerErrorException("Указанный вами email адрес не существует.");
        }
    }

    public void sendSuccessActivation(User user) {
        try {
            StringBuilder message = new StringBuilder();
            message.append("<h4>Поздравляем! Вы успешно активировали учётную запись.</h4>");

            mailSender.sendActivateCodeMessage(user.getEmail(), "Успешное подтверждение аккаунта", message.toString());
        } catch (Exception e) {
            throw new MedicineServerErrorException("Указанный вами email адрес не существует.");
        }
    }


    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    private void checkExistUsers(RegistrationUserDto registrationUserDto) {
        User existUser = getByEmail(registrationUserDto.getEmail());
        if (existUser != null) {
            log.debug("Пользователь {} уже существует.", registrationUserDto.getEmail());
            throw new MedicineBadCredential("Пользователь " + registrationUserDto.getEmail() + " уже существует.");
        }
    }

    private void checkEqualsPassword(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            log.debug("Пароли нового пользователя не совпали");
            throw new MedicineBadCredential("Введённые вами пароли не совпадают.");
        }
    }

    private void checkEmail(String email) {
        Pattern pattern = Pattern.compile("^\\S+@\\S+.\\S{2,6}$");
        Matcher matcher = pattern.matcher(email);
        if (!matcher.find()) {
            log.debug("Указанный емейл не прошёл валидацию");
            throw new MedicineBadCredential("Неверный email формат. Проверьте написанный вами email.");
        }
    }

    private void checkPasswordLength(String password) {
        if (password.length() < passwordLength) {
            log.debug("Указанный пароль менее " + passwordLength + " символов.");
            throw new MedicineBadCredential("Пароль не должен быть короче " + passwordLength + " символов.");
        }
    }


    @Autowired
    public void setPasswordEncoder(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }



}
