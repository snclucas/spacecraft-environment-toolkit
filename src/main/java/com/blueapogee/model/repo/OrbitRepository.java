package com.blueapogee.model.repo;


import com.blueapogee.model.Orbit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.*;

import java.util.*;


public interface OrbitRepository extends MongoRepository<Orbit, String> {

  @Query(value="{'id' : ?0, 'user' : ?1}")
  Orbit findByIdAndUser(String id, String userName);

  Page<Orbit> findAllByUser(String username, Pageable pageable);

  long deleteOrbitById(String id, String userName);

  List<Orbit> findOrbitByName(String name);

  @Query(value="{'name' : ?0, 'user' : ?1}")
  List<Orbit> findAllByNameAndUser(String name, String userName);
}
