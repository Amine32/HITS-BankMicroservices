package ru.tsu.hits.monitor_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.monitor_service.model.LogEntry;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
}

