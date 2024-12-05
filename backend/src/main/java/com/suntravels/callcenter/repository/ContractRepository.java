package com.suntravels.callcenter.repository;

import com.suntravels.callcenter.dto.AvailableContract;
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
//    @Query("SELECT new com.suntravels.callcenter.dto.AvaibleContract(r.roomDetailId, r.maxAdults, r.numberOfRooms, r.pricePerPerson, r.roomType) " +
//            "FROM Contract c " +
//            "JOIN c.roomDetails r " +
//            "WHERE c.startDate <= :checkoutDate " +
//            "AND c.endDate >= :checkinDate " +
//            "AND r.numberOfRooms >= :requiredRooms " +
//            "AND r.maxAdults >= :requiredAdults")
//    List<Contract> findAvailablContract(
//            @Param("checkinDate") LocalDate checkinDate,
//            @Param("checkoutDate") LocalDate checkoutDate,
//            @Param("requiredRooms") int requiredRooms,
//            @Param("requiredAdults") int requiredAdults);

}
