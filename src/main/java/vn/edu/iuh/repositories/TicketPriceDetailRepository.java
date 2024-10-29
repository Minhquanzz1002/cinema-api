package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.TicketPriceDetail;

@Repository
public interface TicketPriceDetailRepository extends JpaRepository<TicketPriceDetail, Integer> {
}
