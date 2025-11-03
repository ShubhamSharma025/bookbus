package com.example.busbooking.repository;

 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.busbooking.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // All queries will be handled in the service layer for simplicity
    List<CartItem> findByUserId(Long userId);
void deleteByUserId(Long userId);
} 