package com.mybootapp.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mybootapp.main.model.InwardRegister;

public interface InwardRegisterRepository extends JpaRepository<InwardRegister, Integer>{
	
	// checking and returning the inward record where the product available quantity is more than quantityPurchased
	@Query("select ir from InwardRegister ir where ir.product.id=?1 AND ir.quantity >= ?2")
	InwardRegister checkQuantity(int productId, int quantityPuchased);
	
	// Returning the inward records where supplierId matches given supplierId
	@Query("select ir from InwardRegister ir where ir.supplier.id=?1")
	List<InwardRegister> getBySupplier(int supplierId);

}
