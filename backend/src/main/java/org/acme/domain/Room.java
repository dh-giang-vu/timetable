package org.acme.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

/**
 * Represents a room.
 *
 * @author Jet Edge
 */
@Entity
public class Room extends PanacheEntity {
    @PlanningId
    public String roomCode;

    public String buildingId;

    public int capacity;

    public boolean isLab;

    @JsonIgnoreProperties("room")
    @OneToMany(mappedBy = "room", orphanRemoval = false)
    public List<Unit> unit;

    @JsonIgnoreProperties("rooms")
    @ManyToMany(mappedBy = "rooms", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonManagedReference
    public List<Timetable> timetables = new ArrayList<Timetable>();

    public Room() {
    }

    /**
     * Creates a room with its ID and capacity.
     *
     * @param id       The room’s id.
     * @param capacity The room's capacity.
     * @param isLab    Whether the room is a laboratory.
     */
    public Room(String id, int capacity, boolean isLab) {
        this.roomCode = id;
        this.capacity = capacity;
        this.isLab = isLab;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String id) {
        this.roomCode = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isLab() {
        return isLab;
    }

    public void setLab(boolean lab) {
        isLab = lab;
    }
}