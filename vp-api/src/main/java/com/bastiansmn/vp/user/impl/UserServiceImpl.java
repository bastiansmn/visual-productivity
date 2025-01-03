package com.bastiansmn.vp.user.impl;

import com.bastiansmn.vp.config.constants.S3Constants;
import com.bastiansmn.vp.config.properties.CorsProperties;
import com.bastiansmn.vp.config.properties.S3Properties;
import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.FunctionalRule;
import com.bastiansmn.vp.exception.TechnicalException;
import com.bastiansmn.vp.mail.MailConfirmService;
import com.bastiansmn.vp.pendingUserInvites.PendingInvitesDAO;
import com.bastiansmn.vp.pendingUserInvites.PendingInvitesRepository;
import com.bastiansmn.vp.project.ProjectRepository;
import com.bastiansmn.vp.role.RoleDAO;
import com.bastiansmn.vp.role.RoleService;
import com.bastiansmn.vp.socialAuth.UserProvider;
import com.bastiansmn.vp.user.UserDAO;
import com.bastiansmn.vp.user.UserPrincipal;
import com.bastiansmn.vp.user.UserRepository;
import com.bastiansmn.vp.user.UserService;
import com.bastiansmn.vp.user.dto.UserCreationDTO;
import com.bastiansmn.vp.user.dto.UserModificationDTO;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import jakarta.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final MailConfirmService mailConfirmService;
    private final PendingInvitesRepository pendingInvitesRepository;
    private final ProjectRepository projectRepository;
    private final MinioClient minioClient;
    private final S3Properties s3Properties;
    private final CorsProperties corsProperties;

    public UserDAO create(UserCreationDTO userDTO) throws FunctionalException, TechnicalException {
        // TODO Limiter ler nombre de requêtes par IP (car route publique)
        if (this.isSocialUser(userDTO.getEmail()))
            throw new FunctionalException(FunctionalRule.USER_0007);

        if (this.emailExists(userDTO.getEmail()))
            throw new FunctionalException(FunctionalRule.USER_0001);

        if (!this.validateMail(userDTO.getEmail()))
            throw new FunctionalException(FunctionalRule.USER_0003);

        if (userDTO.getProvider().equals(UserProvider.LOCAL) && !this.validatePassword(userDTO.getPassword()))
            throw new FunctionalException(FunctionalRule.USER_0004);

        Set<RoleDAO> defaultRoles = this.roleService.getDefaultRoles();

        String colors = "7d70ff,beb8ff,4db222,d6efc4,acd88c,22c2fb,91e1fd,fd6d2f,feb697,d93636,ec9b9b";
        var colorsArray = colors.split(",");
        String variants = "variant2W12,variant2W14,variant2W16,variant3W10,variant3W12,variant3W14,variant3W16,variant4W10,variant4W12,variant4W14,variant4W16,variant5W10,variant5W12,variant5W14,variant5W16,variant6W10,variant6W12,variant6W14,variant6W16,variant7W10,variant7W12,variant7W14,variant7W16,variant8W10,variant8W12,variant8W14,variant8W16,variant9W10,variant9W12,variant9W14,variant9W16";
        var variantsArray = variants.split(",");
        var random = new Random();
        var color = colorsArray[random.nextInt(colorsArray.length)];
        var variant = variantsArray[random.nextInt(variantsArray.length)];
        String avatar = "https://api.dicebear.com/5.x/thumbs/png?size=128" + "&background=" + color + "&eyes=" + variant;


        UserDAO user = UserDAO.builder()
                .email(userDTO.getEmail())
                .name(userDTO.getName())
                .lastname(userDTO.getLastname())
                .password(
                    userDTO.getProvider().equals(UserProvider.LOCAL)
                        ? passwordEncoder.encode(userDTO.getPassword())
                        : null
                )
                .avatar(avatar)
                .provider(userDTO.getProvider())
                .roles(defaultRoles)
                .createdDate(Date.from(Instant.now()))
                .mailConfirmations(List.of())
                .isEnabled(false)
                .isNotLocked(true)
                .build();

        log.info("Creating user: {}", user);

        var pendingInvites = this.pendingInvitesRepository.findAllByEmail(user.getEmail());

        var projects = pendingInvites.stream()
                .map(PendingInvitesDAO::getProject)
                .map(this.projectRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        user.setProjects(projects);

        // Clean pending invites
        projects.forEach(project -> {
            this.pendingInvitesRepository.deleteAllByEmailAndProject(user.getEmail(), project.getProjectId());
        });

        UserDAO createdUser = this.userRepository.save(user);
        this.mailConfirmService.createConfirmation(createdUser);
        return createdUser;
    }

    public UserDAO fetchByEmail(String email) throws FunctionalException {
        var user = this.userRepository.findByEmail(email);

        if (user.isEmpty())
            throw new FunctionalException(
                    FunctionalRule.USER_0004, HttpStatus.NOT_FOUND);

        log.info("Fetching user by email: {}", email);

        return user.get();
    }

    public List<UserDAO> fetchAll() {
        log.info("Fetching all users");
        return this.userRepository.findAll();
    }

    public void delete(Long user_id) throws FunctionalException {
        Optional<UserDAO> user = this.userRepository.findById(user_id);
        if (user.isEmpty())
            throw new FunctionalException(FunctionalRule.USER_0004, HttpStatus.NOT_FOUND);

        log.info("Deleting user with id={}", user.get().getUser_id());
        this.userRepository.delete(user.get());
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isEnabled(String email) throws FunctionalException {
        return this.fetchByEmail(email).isEnabled();
    }

    public boolean isNotLocked(String email) throws FunctionalException {
        return this.fetchByEmail(email).isNotLocked();
    }

    @Override
    public UserDAO updateAvatar(String email, MultipartFile avatar) throws FunctionalException {
        if (avatar == null || avatar.isEmpty())
            throw new FunctionalException(FunctionalRule.FILE_0001);

        // If avatar is not a jpeg or png, throw an error
        if (!Objects.equals(avatar.getContentType(), "image/jpeg") && !Objects.equals(avatar.getContentType(),
                "image/png"))
            throw new FunctionalException(FunctionalRule.FILE_0004);

        // If file is bigger than 5MB, throw an error
        if (avatar.getSize() > 5 * 1024 * 1024)
            throw new FunctionalException(FunctionalRule.FILE_0007);

        // If image is bigger than 1000x1000, throw an error
        try {
            BufferedImage image = ImageIO.read(avatar.getInputStream());
            if (image.getWidth() > 1000 || image.getHeight() > 1000)
                throw new FunctionalException(FunctionalRule.FILE_0008);
        } catch (IOException e) {
            throw new FunctionalException(FunctionalRule.FILE_0008);
        }

        var user = this.fetchByEmail(email);
        if (user.getAvatar() != null) {
            LOGGER.info("S3Properties: {}", this.s3Properties);
            try {
                this.minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(this.s3Properties.getUserBucket())
                                .object(this.getObjectFromUrl(user.getAvatar()))
                                .build()
                );
            } catch (Exception e) {
                LOGGER.error("Error while deleting avatar from S3", e);
                throw new FunctionalException(FunctionalRule.FILE_0002);
            }
        }

        String s3Filename = String.format("%s__%s", user.getEmail(), avatar.getOriginalFilename());
        String pathToAvatar = corsProperties.getCurrentOrigin() + "/api/v1/user/avatar/" + s3Filename;
        user.setAvatar(pathToAvatar);

        try {
            this.minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(this.s3Properties.getUserBucket())
                            .object(S3Constants.AVATAR_DIRECTORY + "/" + s3Filename)
                            .stream(avatar.getInputStream(), avatar.getSize(), -1)
                            .contentType(avatar.getContentType())
                            .build()
            );
            return user;
        } catch (Exception e) {
            throw new FunctionalException(FunctionalRule.FILE_0003);
        }
    }

    private String getObjectFromUrl(String avatar) {
        String[] split = avatar.split("/");
        return split[split.length - 2] + "/" + split[split.length - 1];
    }

    @Override
    public byte[] getAvatar(String avatarURL) throws FunctionalException {
        try {
            InputStream stream = this.minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(this.s3Properties.getUserBucket())
                            .object(S3Constants.AVATAR_DIRECTORY + "/" + avatarURL)
                            .build()
            );

            return stream.readAllBytes();
        } catch (Exception e) {
            throw new FunctionalException(FunctionalRule.FILE_0005);
        }
    }

    @Override
    public UserDAO update(UserModificationDTO user) throws FunctionalException {
        var userDao = this.fetchByEmail(user.getEmail());

        userDao.setName(user.getName());
        userDao.setLastname(user.getLastname());

        return this.userRepository.save(userDao);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug(email);
        UserDAO userDAO = userRepository
                .findByEmail(email)
                .orElseThrow();
        return new UserPrincipal(userDAO);
    }

    private boolean validatePassword(String password) {
        return password.length() > 8;
    }

    private boolean validateMail(String mail) {
        return mail.matches("^[a-zA-Z\\d._-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,6}$");
    }

    private boolean isSocialUser(String email) {
        Optional<UserDAO> user = this.userRepository.findByEmail(email);
        return user.filter(userDAO -> !userDAO.getProvider().equals(UserProvider.LOCAL)).isPresent();
    }

}
