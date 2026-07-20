package jsp.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jsp.springboot.entity.Address;

public interface AddressRepository extends JpaRepository<Address,Integer>{
	
	List<Address> findByCity(String city);
	
	List<Address> findByCityAndStreet(String city, String street);

}
