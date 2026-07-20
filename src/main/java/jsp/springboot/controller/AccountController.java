package jsp.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.Account;
import jsp.springboot.entity.AccountType;
import jsp.springboot.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	
	@PostMapping("/{bankId}/accounts")
	public ResponseEntity<ResponseStructure<Account>> saveAccount(
			@PathVariable Integer bankId,
			@RequestBody Account account){

		return accountService.saveAccount(bankId, account);
	}
	
	@GetMapping("/{accountId}")
	public ResponseEntity<ResponseStructure<Account>> getAccountById(
	        @PathVariable Integer accountId) {

	    return accountService.getAccountById(accountId);
	}
	
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Account>>> getAllAccounts() {

	    return accountService.getAllAccounts();
	}
	
	@PatchMapping("/{accountId}")
	public ResponseEntity<ResponseStructure<Account>> updateAccount(
	        @PathVariable Integer accountId,
	        @RequestBody Account account) {

	    return accountService.updateAccount(accountId, account);
	}
	
	@DeleteMapping("/{accountId}")
	public ResponseEntity<ResponseStructure<Account>> deleteAccount(
	        @PathVariable Integer accountId) {

	    return accountService.deleteAccount(accountId);
	}
	
	@PutMapping("/{accountId}/deposit/{amount}")
	public ResponseEntity<ResponseStructure<Account>> deposit(
	        @PathVariable Integer accountId,
	        @PathVariable double amount) {

	    return accountService.deposit(accountId, amount);
	}
	
	@PutMapping("/{accountId}/withdraw/{amount}")
	public ResponseEntity<ResponseStructure<Account>> withdraw(
	        @PathVariable Integer accountId,
	        @PathVariable double amount){

	    return accountService.withdraw(accountId, amount);
	}
	
	@PutMapping("/{senderId}/transfer/{receiverId}/{amount}")
	public ResponseEntity<ResponseStructure<Account>> transfer(
	        @PathVariable Integer senderId,
	        @PathVariable Integer receiverId,
	        @PathVariable double amount){

	    return accountService.transfer(senderId, receiverId, amount);
	}
	
	@GetMapping("/bank/{bankId}")
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountsByBank(
	        @PathVariable Integer bankId) {

	    return accountService.getAccountsByBank(bankId);
	}
	
	@GetMapping("/type/{accountType}")
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountsByType(
			@PathVariable AccountType accountType) {

		return accountService.getAccountsByType(accountType);
	}
	
	@GetMapping("/balance/{balance}")
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountsGreaterThanBalance(
			@PathVariable double balance) {

		return accountService.getAccountsGreaterThanBalance(balance);
	}
	
	@GetMapping("/pagination")
	public ResponseEntity<ResponseStructure<Page<Account>>> getAccountsByPaginationAndSorting(

	        @RequestParam int pageNumber,
	        @RequestParam int pageSize,
	        @RequestParam String field) {

	    return accountService.getAccountsByPaginationAndSorting(pageNumber, pageSize, field);
	}
}
