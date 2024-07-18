package kz.dara.test.qs.service;

import kz.dara.test.qs.model.UserModel;
import kz.dara.test.qs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findByEmail(username);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("User not found!");
        }
    }

    public List<UserModel> getUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserModel findUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserModel editUser(String fullName, String email, String newPassword, Long id) {
        UserModel userModel = findUser(id);
        if (userModel != null) {
            userModel.setFull_name(fullName);
            userModel.setEmail(email);
            if (newPassword != null && !newPassword.isEmpty()) {
                userModel.setPassword(passwordEncoder.encode(newPassword));
            }
            return userRepository.save(userModel);
        }
        return null;
    }

    public UserModel addUser(UserModel userModel) {
        UserModel checkUser = userRepository.findByEmail(userModel.getEmail());
        if (checkUser == null) {
            userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
            return userRepository.save(userModel);
        }
        return null;
    }

    public boolean signUpUser(String email, String password, String fullName, String repeatPassword) {
        if (password.equals(repeatPassword)) {
            UserModel userModel = new UserModel();
            userModel.setEmail(email);
            userModel.setPassword(passwordEncoder.encode(password));
            userModel.setFull_name(fullName);
            UserModel newUser = addUser(userModel);
            return newUser == null;
        }
        return false;
    }

    public UserModel updatePassword(String newPassword, String oldPassword) {
        UserModel currentUser = getCurrentSessionUser();
        if (passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            currentUser.setPassword(passwordEncoder.encode(newPassword));
            return userRepository.save(currentUser);
        }
        return null;
    }

    public UserModel editPassword(String newPassword, Long id) {
        UserModel selectedUser = userRepository.findById(id).orElse(null);
        if (selectedUser != null) {
            selectedUser.setPassword(passwordEncoder.encode(newPassword));
            return userRepository.save(selectedUser);
        }
        return null;
    }

    public boolean checkPassword(String password) {
        UserModel currentUser = getCurrentSessionUser();
        return passwordEncoder.matches(password, currentUser.getPassword());
    }

    public UserModel updateProfile(String fullName) {
        UserModel currentUser = getCurrentSessionUser();
        if (!fullName.equals(currentUser.getFull_name())) {
            currentUser.setFull_name(fullName);
            return userRepository.save(currentUser);
        }
        return null;
    }

    public UserModel getCurrentSessionUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return (UserModel) authentication.getPrincipal();
        }
        return null;
    }
}
