package kata.preproject.task3_1_3.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import kata.preproject.task3_1_3.dao.RoleRepository;
import kata.preproject.task3_1_3.dao.UserRepository;
import kata.preproject.task3_1_3.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService  {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public boolean saveUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Transactional(readOnly = true)
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public User getUserById(long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUser(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

}