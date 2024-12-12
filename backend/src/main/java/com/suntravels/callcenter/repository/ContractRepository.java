package com.suntravels.callcenter.repository;

import com.suntravels.callcenter.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for accessing and manipulating Contract entities in the database.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface ContractRepository extends JpaRepository<Contract,Integer> {

    @Query("SELECT c FROM Contract c WHERE c.hotelName LIKE %:hotelName%")
    List<Contract> findByHotelName(@Param("hotelName") String hotelName);


    @Query("SELECT c FROM Contract c WHERE c.startDate <= :checkInDate AND c.endDate >= :checkOutDate")
    List<Contract> findContractsByDateRange(@Param("checkInDate") LocalDate checkInDate,
                                            @Param("checkOutDate") LocalDate checkOutDate);



}
