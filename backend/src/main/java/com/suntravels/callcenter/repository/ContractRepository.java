package com.suntravels.callcenter.repository;

import com.suntravels.callcenter.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing and manipulating Contract entities in the database.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface ContractRepository extends JpaRepository<Contract,Integer> {
}
