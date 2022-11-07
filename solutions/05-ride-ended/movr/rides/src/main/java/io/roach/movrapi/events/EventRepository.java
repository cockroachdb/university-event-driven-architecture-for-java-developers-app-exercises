package io.roach.movrapi.events;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository<EventEnvelope, UUID> {
}
