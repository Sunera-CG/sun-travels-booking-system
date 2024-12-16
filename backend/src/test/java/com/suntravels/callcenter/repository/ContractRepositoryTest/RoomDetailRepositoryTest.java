package com.suntravels.callcenter.repository.ContractRepositoryTest;

import com.suntravels.callcenter.model.RoomDetail;
import com.suntravels.callcenter.repository.RoomDetailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the RoomDetailRepository class.
 * <p>
 * This test class uses @DataJpaTest to provide an in-memory database for testing repository operations
 * related to room details.
 */
@DataJpaTest
public class RoomDetailRepositoryTest {

    @Autowired
    private RoomDetailRepository roomDetailRepository;

    private RoomDetail roomDetail;

    /**
     * Sets up the test environment by creating and saving a room detail.
     * This method is executed before each test case.
     */
    @BeforeEach
    public void setUp() {
        roomDetail = new RoomDetail(null, "Deluxe", 200.0, 10, 2);
        roomDetailRepository.save(roomDetail);
    }

    /**
     * Tests saving a room detail.
     * <p>
     * This test verifies that a room detail is saved and its ID is automatically generated.
     */
    @Test
    public void testSaveRoomDetail() {
        assertNotNull(roomDetail.getRoomDetailId());
        assertEquals("Deluxe", roomDetail.getRoomType());
    }

    /**
     * Tests finding a room detail by its ID.
     * <p>
     * This test ensures that the room detail can be retrieved from the repository using its ID.
     */
    @Test
    public void testFindByRoomDetailId() {
        RoomDetail foundRoom = roomDetailRepository.findById(roomDetail.getRoomDetailId()).orElse(null);
        assertNotNull(foundRoom);
        assertEquals("Deluxe", foundRoom.getRoomType());
    }

    /**
     * Tests updating a room detail.
     * <p>
     * This test ensures that the room detail can be updated and the changes are persisted.
     */
    @Test
    public void testUpdateRoomDetail() {
        roomDetail.setRoomType("Suite");
        roomDetailRepository.save(roomDetail);

        RoomDetail updatedRoom = roomDetailRepository.findById(roomDetail.getRoomDetailId()).orElse(null);
        assertNotNull(updatedRoom);
        assertEquals("Suite", updatedRoom.getRoomType());
    }

    /**
     * Tests deleting a room detail.
     * <p>
     * This test ensures that a room detail is deleted correctly and can no longer be found.
     */
    @Test
    public void testDeleteRoomDetail() {
        roomDetailRepository.deleteById(roomDetail.getRoomDetailId());
        RoomDetail deletedRoom = roomDetailRepository.findById(roomDetail.getRoomDetailId()).orElse(null);
        assertNull(deletedRoom);
    }
}
