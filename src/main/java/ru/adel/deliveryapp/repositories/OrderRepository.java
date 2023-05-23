package ru.adel.deliveryapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.adel.deliveryapp.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
