package org.example.spring.lab.settlement;

import org.example.spring.lab.settlement.model.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
}
