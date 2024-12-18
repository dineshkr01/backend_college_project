package com.example.charityManagementSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.charityManagementSystem.models.Admin;
import com.example.charityManagementSystem.models.Donor;
import com.example.charityManagementSystem.models.Needy;
import com.example.charityManagementSystem.models.UserAdminPrincipal;
import com.example.charityManagementSystem.models.UserDonorPrincipal;
import com.example.charityManagementSystem.models.UserNeedyPrincipal;
import com.example.charityManagementSystem.repository.AdminRepo;
import com.example.charityManagementSystem.repository.DonorRepo;
import com.example.charityManagementSystem.repository.NeedyRepo;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    AdminRepo userdatabase;

    @Autowired
    DonorRepo userdatabase2;

    @Autowired
    NeedyRepo userdatabase3;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Username in UserDetails is : " + username);

        Admin user_admin = null;
        Donor user_donor = null;
        Needy user_needy = null;
        if (username.charAt(0) == '1') {
            user_admin = userdatabase.findByUsername(username);
            if (user_admin == null) {
                System.out.println("Admin User Not Found !!");
                throw new UsernameNotFoundException("Admin User Not Found !!!");
            }
            return new UserAdminPrincipal(user_admin);
        }

        else if (username.charAt(0) == '2') {
            user_donor = userdatabase2.findByUsername(username);
            if (user_donor == null) {
                System.out.println("Donor User Not Found !!");
                throw new UsernameNotFoundException("Donor User Not Found !!!");
            }
            return new UserDonorPrincipal(user_donor);
        }

        else if (username.charAt(0) == '3') {
            user_needy = userdatabase3.findByUsername(username);
            if (user_needy == null) {
                System.out.println("Needy User Not Found !!");
                throw new UsernameNotFoundException("Needy User Not Found !!!");
            }
            return new UserNeedyPrincipal(user_needy);
        }

        else {
            System.out.println("User Type Not Found !!");
            throw new UsernameNotFoundException("User Type Not Found !!!");
        }
    }

}
