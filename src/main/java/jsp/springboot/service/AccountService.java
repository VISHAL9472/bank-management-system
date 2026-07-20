package jsp.springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.Account;
import jsp.springboot.entity.AccountType;
import jsp.springboot.entity.Bank;
import jsp.springboot.exception.DuplicateAccountNumberException;
import jsp.springboot.exception.IdNotFoundException;
import jsp.springboot.exception.InsufficientBalanceException;
import jsp.springboot.exception.MinimumBalanceException;
import jsp.springboot.exception.NoRecordAvailableException;
import jsp.springboot.repository.AccountRepository;
import jsp.springboot.repository.BankRepository;

@Service
public class AccountService {
	
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private BankRepository bankRepository;
	
	public ResponseEntity<ResponseStructure<Account>> saveAccount(Integer bankId, Account account){
		
		ResponseStructure<Account> res=new ResponseStructure<>();
		
		Optional<Bank> bankOpt = bankRepository.findById(bankId);
		
		if(bankOpt.isPresent()) {
			Optional<Account> accountOpt = accountRepository.findByAccountNumber(account.getAccountNumber());
			
			if(accountOpt.isPresent()) {
				throw new DuplicateAccountNumberException("Account number already exists");
			}
			Bank bank = bankOpt.get();
			if(account.getAccountType() == AccountType.SAVINGS &&
			        account.getBalance() < 1000) {

			    throw new MinimumBalanceException(
			            "Minimum balance for Savings Account is Rs.1000");
			}

			if(account.getAccountType() == AccountType.CURRENT &&
			        account.getBalance() < 5000) {

			    throw new MinimumBalanceException(
			            "Minimum balance for Current Account is Rs.5000");
			}
			account.setBank(bank);
			Account savedAccount = accountRepository.save(account);
			res.setStatusCode(HttpStatus.CREATED.value());

			res.setMessage("Account Saved Successfully");

			res.setData(savedAccount);
			
			return new ResponseEntity<ResponseStructure<Account>>(res,HttpStatus.CREATED);
		}
			throw new IdNotFoundException("Bank Id Not Found");
		
	}
	
	public ResponseEntity<ResponseStructure<Account>> getAccountById(Integer accountId){

		ResponseStructure<Account> res = new ResponseStructure<>();

		Optional<Account> opt = accountRepository.findById(accountId);
		
		if(opt.isPresent()) {
			Account account = opt.get();

			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Account Found Successfully");
			res.setData(account);

			return new ResponseEntity<ResponseStructure<Account>>(res,HttpStatus.OK);

		}
			throw new IdNotFoundException("Account Id Not Found");

	}
	
	public ResponseEntity<ResponseStructure<List<Account>>> getAllAccounts() {

	    ResponseStructure<List<Account>> res = new ResponseStructure<>();

	    List<Account> accounts = accountRepository.findAll();

	    if(accounts.isEmpty()) {
	        throw new NoRecordAvailableException("No Account Available");
	    }

	    res.setStatusCode(HttpStatus.OK.value());
	    res.setMessage("All Accounts Fetched Successfully");
	    res.setData(accounts);

	    return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	public ResponseEntity<ResponseStructure<Account>> updateAccount(Integer accountId, Account account) {

	    ResponseStructure<Account> res = new ResponseStructure<>();

	    Optional<Account> opt = accountRepository.findById(accountId);

	    if (opt.isPresent()) {

	        Account dbAccount = opt.get();

	        dbAccount.setAccountHolderName(account.getAccountHolderName());
	        dbAccount.setAccountType(account.getAccountType());

	        Account updatedAccount = accountRepository.save(dbAccount);

	        res.setStatusCode(HttpStatus.OK.value());
	        res.setMessage("Account Updated Successfully");
	        res.setData(updatedAccount);

	        return new ResponseEntity<>(res, HttpStatus.OK);
	    }

	    throw new IdNotFoundException("Account Id Not Found");
	}
	
	public ResponseEntity<ResponseStructure<Account>> deleteAccount(Integer accountId) {

	    ResponseStructure<Account> res = new ResponseStructure<>();

	    Optional<Account> opt = accountRepository.findById(accountId);

	    if (opt.isPresent()) {

	        Account account = opt.get();

	        accountRepository.delete(account);

	        res.setStatusCode(HttpStatus.OK.value());
	        res.setMessage("Account Deleted Successfully");
	        res.setData(account);

	        return new ResponseEntity<>(res, HttpStatus.OK);

	    } else {
	        throw new IdNotFoundException("Account Id Not Found");
	    }
	}
	
	public ResponseEntity<ResponseStructure<Account>> deposit(Integer accountId, double amount) {

	    ResponseStructure<Account> res = new ResponseStructure<>();

	    Optional<Account> opt = accountRepository.findById(accountId);

	    if (opt.isPresent()) {

	        Account account = opt.get();

	        double balance = account.getBalance();

	        balance = balance + amount;

	        account.setBalance(balance);

	        Account updatedAccount = accountRepository.save(account);

	        res.setStatusCode(HttpStatus.OK.value());
	        res.setMessage("Amount Deposited Successfully");
	        res.setData(updatedAccount);

	        return new ResponseEntity<>(res, HttpStatus.OK);

	    } else {
	        throw new IdNotFoundException("Account Id Not Found");
	    }
	}
	
	public ResponseEntity<ResponseStructure<Account>> withdraw(Integer accountId,double amount){

	    ResponseStructure<Account> res = new ResponseStructure<>();

	    Optional<Account> opt = accountRepository.findById(accountId);

	    if(opt.isPresent()) {

	        Account account = opt.get();

	        if(account.getBalance() >= amount) {
	        	if(account.getAccountType() == AccountType.SAVINGS &&
	        	        account.getBalance() - amount < 1000) {

	        	    throw new MinimumBalanceException(
	        	            "Minimum balance of Rs.1000 should be maintained");
	        	}

	        	if(account.getAccountType() == AccountType.CURRENT &&
	        	        account.getBalance() - amount < 5000) {

	        	    throw new MinimumBalanceException(
	        	            "Minimum balance of Rs.5000 should be maintained");
	        	}

	            account.setBalance(account.getBalance() - amount);

	            Account updatedAccount = accountRepository.save(account);

	            res.setStatusCode(HttpStatus.OK.value());
	            res.setMessage("Amount Withdraw Successfully");
	            res.setData(updatedAccount);

	            return new ResponseEntity<>(res,HttpStatus.OK);
	        }
	        else {
	            throw new InsufficientBalanceException("Insufficient Balance");
	        }

	    }
	    else {
	        throw new IdNotFoundException("Account Id Not Found");
	    }
	}
	
	@Transactional
	public ResponseEntity<ResponseStructure<Account>> transfer(
	        Integer senderId,
	        Integer receiverId,
	        double amount) {

	    ResponseStructure<Account> res = new ResponseStructure<>();

	    Optional<Account> senderOpt = accountRepository.findById(senderId);

	    if (senderOpt.isEmpty()) {
	        throw new IdNotFoundException("Sender Account Id Not Found");
	    }

	    Optional<Account> receiverOpt = accountRepository.findById(receiverId);

	    if (receiverOpt.isEmpty()) {
	        throw new IdNotFoundException("Receiver Account Id Not Found");
	    }

	    Account sender = senderOpt.get();
	    Account receiver = receiverOpt.get();

	    if (sender.getBalance() < amount) {
	        throw new InsufficientBalanceException("Insufficient Balance");
	    }

	    sender.setBalance(sender.getBalance() - amount);
	    receiver.setBalance(receiver.getBalance() + amount);

	    accountRepository.save(sender);
	    accountRepository.save(receiver);

	    res.setStatusCode(HttpStatus.OK.value());
	    res.setMessage("Money Transferred Successfully");
	    res.setData(sender);

	    return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountsByBank(Integer bankId) {

	    ResponseStructure<List<Account>> res = new ResponseStructure<>();

	    Optional<Bank> bankOpt = bankRepository.findById(bankId);

	    if (bankOpt.isPresent()) {

	        List<Account> accounts = accountRepository.findByBankBankId(bankId);

	        if (accounts.isEmpty()) {
	            throw new NoRecordAvailableException("No Account Found For This Bank");
	        }

	        res.setStatusCode(HttpStatus.OK.value());
	        res.setMessage("Accounts Fetched Successfully");
	        res.setData(accounts);

	        return new ResponseEntity<>(res, HttpStatus.OK);
	    }

	    throw new IdNotFoundException("Bank Id Not Found");
	}
	
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountsByType(AccountType accountType) {

		ResponseStructure<List<Account>> res = new ResponseStructure<>();

		List<Account> accounts = accountRepository.findByAccountType(accountType);

		if (!accounts.isEmpty()) {

			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Accounts Fetched Successfully");
			res.setData(accounts);

			return new ResponseEntity<>(res, HttpStatus.OK);
		}

		throw new NoRecordAvailableException("No Account Found With Given Account Type");
	}
	
	public ResponseEntity<ResponseStructure<List<Account>>> getAccountsGreaterThanBalance(double balance) {

		ResponseStructure<List<Account>> res = new ResponseStructure<>();

		List<Account> accounts = accountRepository.findByBalanceGreaterThan(balance);

		if (!accounts.isEmpty()) {

			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Accounts Fetched Successfully");
			res.setData(accounts);

			return new ResponseEntity<>(res, HttpStatus.OK);
		}

		throw new NoRecordAvailableException("No Account Found With Balance Greater Than " + balance);
	}
	
	public ResponseEntity<ResponseStructure<Page<Account>>> getAccountsByPaginationAndSorting(
	        int pageNumber,
	        int pageSize,
	        String field) {

	    ResponseStructure<Page<Account>> res = new ResponseStructure<>();

	    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(field));

	    Page<Account> page = accountRepository.findAll(pageable);

	    if (page.hasContent()) {

	        res.setStatusCode(HttpStatus.OK.value());
	        res.setMessage("Accounts Fetched Successfully");
	        res.setData(page);

	        return new ResponseEntity<>(res, HttpStatus.OK);
	    }

	    throw new NoRecordAvailableException("No Records Available");
	}
}
