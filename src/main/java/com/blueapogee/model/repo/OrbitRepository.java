package com.blueapogee.model.repo;


import com.blueapogee.model.Orbit;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrbitRepository extends MongoRepository<Orbit, String> {

  long deleteOrbitById(String id);
}
