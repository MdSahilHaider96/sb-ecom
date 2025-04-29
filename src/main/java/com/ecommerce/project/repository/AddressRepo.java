package com.ecommerce.project.repository;

import com.ecommerce.project.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface AddressRepo extends JpaRepository<Address, Long> {

}
