package com.emprovise.service.security.service;


import com.emprovise.service.security.model.LoggedInUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class AuthorizationUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<String> listOfBusinessActivities = getBusinessActivitiesByUser(username);

        List<GrantedAuthority> listOfAuthorities = new ArrayList<>();
        if (listOfBusinessActivities.size() > 0) {
            for (String businessActivity : listOfBusinessActivities) {
                listOfAuthorities.add(new SimpleGrantedAuthority(businessActivity));
            }
        }
        return getLoggedInUserDetails(username, listOfAuthorities);
    }

    private User getLoggedInUserDetails(String username, List<GrantedAuthority> listOfAuthorities) {
        LoggedInUserDetails userDetails = new LoggedInUserDetails(username, "N/A", listOfAuthorities);
        userDetails.setAccountNumber(UUID.randomUUID().toString());
        userDetails.setCountry("USA");
        return userDetails;
    }

    private List<String> getBusinessActivitiesByUser(String username) {

        if("ADMIN".equalsIgnoreCase(username)) {
            return Arrays.asList("READ_STOCKS", "READ_WEATHER", "ADMIN");
        } else {
            return Arrays.asList("READ_STOCKS", "READ_WEATHER");
        }
    }
}
