package test.testspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.testspring.domain.Trade;

public interface TradeRepository extends JpaRepository<Trade, Long> {


}
