package jsp.springboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jsp.springboot.entity.Bank;

public interface BankRepository extends JpaRepository<Bank,Integer>{
	
	Optional<Bank> findByIfsc(String ifsc);
	
	Optional<Bank> findByContactNumber(Long contactNumber);
	

}
