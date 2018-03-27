package com.blueapogee.model.repo;

import com.blueapogee.model.HtplUserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDetailsRepository extends MongoRepository<HtplUserDetails, String> {

    HtplUserDetails findByUsername(String userName);

    HtplUserDetails findByToken(String token);

}