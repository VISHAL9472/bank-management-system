package jsp.springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jsp.springboot.entity.Account;
import jsp.springboot.entity.AccountType;

public interface AccountRepository extends  JpaRepository<Account,Integer>{
	Optional<Account> findByAccountNumber(Long accountNumber);
	
	 List<Account> findByBankBankId(Integer bankId);
	 
	 List<Account> findByAccountType(AccountType accountType);
	 
	 List<Account> findByBalanceGreaterThan(double balance);

}
