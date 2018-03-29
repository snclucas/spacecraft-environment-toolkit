package com.blueapogee.model.repo;


import com.blueapogee.model.Orbit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface OrbitRepository extends MongoRepository<Orbit, String> {

  Page<Orbit> findAllByName(String username, Pageable pageable);

  long deleteOrbitById(String id, String userName);

  Orbit getOrbitByNameAndUser(String name, String userName);
}
