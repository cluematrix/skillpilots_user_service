package com.skilluser.user.serviceImpl;

import com.skilluser.user.model.Role;
import com.skilluser.user.model.User;
import com.skilluser.user.repository.RoleRepository;
import com.skilluser.user.repository.UserRepository;
import com.skilluser.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found "+ id));
    }


    @Override
    public List<User> findUsersByRoleAndDepartment(Long roleId, Long departmentId) {
        return userRepository.findUsersByRoleAndDepartment(roleId,departmentId);
    }

    @Override
    public List<User> findUsersByRoles_IdAndCollegeId(Long roleId, Long collegeId) {
        return userRepository.findUsersByRoles_IdAndCollegeId(roleId,collegeId);
    }

    @Override
    public List<User> findHodByDepartment(Long roleId, Long departmentId) {
        return userRepository.findHodByDepartment(roleId,departmentId);
    }
}
