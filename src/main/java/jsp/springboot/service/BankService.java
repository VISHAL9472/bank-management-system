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

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.Account;
import jsp.springboot.entity.Bank;
import jsp.springboot.exception.DuplicateContactNumberException;
import jsp.springboot.exception.DuplicateIfscException;
import jsp.springboot.exception.IdNotFoundException;
import jsp.springboot.exception.NoRecordAvailableException;
import jsp.springboot.repository.AccountRepository;
import jsp.springboot.repository.BankRepository;


@Service
public class BankService{
	
	@Autowired
	private BankRepository bankRepository;
	
	public ResponseEntity<ResponseStructure<Bank>> saveBank(Bank bank ){
		ResponseStructure<Bank> res = new ResponseStructure<Bank>();
		
		Optional<Bank> opt = bankRepository.findByIfsc(bank.getIfsc());

		if (opt.isPresent()) {
			throw new DuplicateIfscException("IFSC Already Exists");
		}
		
		Optional<Bank> opt1 = bankRepository.findByContactNumber(bank.getContactNumber());

		if (opt1.isPresent()) {
		    throw new DuplicateContactNumberException("Contact Number Already Exists");
		}

		
		Bank savedBank=bankRepository.save(bank);
		res.setStatusCode(HttpStatus.CREATED.value());
		res.setMessage("Bank saved successfully");
		res.setData(savedBank);
		
		return new ResponseEntity<ResponseStructure<Bank>>(res,HttpStatus.CREATED);
	}
	
	public ResponseEntity<ResponseStructure<List<Bank>>> getAllBanks() {

		ResponseStructure<List<Bank>> res = new ResponseStructure<>();

		List<Bank> banks = bankRepository.findAll();

		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("All Banks Fetched Successfully");
		res.setData(banks);

		return new ResponseEntity<ResponseStructure<List<Bank>>>(res, HttpStatus.OK);

	}
	
	public ResponseEntity<ResponseStructure<Bank>> getBankById(Integer id){

		Optional<Bank> opt = bankRepository.findById(id);

		if(opt.isPresent()) {

			ResponseStructure<Bank> res = new ResponseStructure<>();

			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Bank Found Successfully");
			res.setData(opt.get());

			return new ResponseEntity<ResponseStructure<Bank>>(res,HttpStatus.OK);
		}

		throw new IdNotFoundException("Bank Id Not Found");
	}
	
	public ResponseEntity<ResponseStructure<Bank>> updateBank(Integer id, Bank bank) {

		Optional<Bank> opt = bankRepository.findById(id);

		if(opt.isPresent()) {

			bank.setBankId(id);

			Bank updatedBank = bankRepository.save(bank);

			ResponseStructure<Bank> res = new ResponseStructure<>();

			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Bank Updated Successfully");
			res.setData(updatedBank);

			return new ResponseEntity<ResponseStructure<Bank>>(res,HttpStatus.OK);

		}

		throw new IdNotFoundException("Bank Id Not Found");
	}
	
	public ResponseEntity<ResponseStructure<Bank>> deleteBank(Integer id) {

		Optional<Bank> opt = bankRepository.findById(id);

		if (opt.isPresent()) {

			Bank bank = opt.get();

			bankRepository.delete(bank);

			ResponseStructure<Bank> res = new ResponseStructure<>();

			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Bank Deleted Successfully");
			res.setData(bank);

			return new ResponseEntity<ResponseStructure<Bank>>(res, HttpStatus.OK);
		}

		throw new IdNotFoundException("Bank Id Not Found");
	}
	
	public ResponseEntity<ResponseStructure<Bank>> getBankByIfsc(String ifsc) {

		Optional<Bank> opt = bankRepository.findByIfsc(ifsc);

		if (opt.isPresent()) {

			ResponseStructure<Bank> res = new ResponseStructure<>();

			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Bank Found Successfully");
			res.setData(opt.get());

			return new ResponseEntity<ResponseStructure<Bank>>(res, HttpStatus.OK);
		}

		throw new NoRecordAvailableException("No Bank Found With Given IFSC");
	}
	
	public ResponseEntity<ResponseStructure<Bank>> getBankByContactNumber(long contactNumber) {

		Optional<Bank> opt = bankRepository.findByContactNumber(contactNumber);

		if (opt.isPresent()) {

			ResponseStructure<Bank> res = new ResponseStructure<>();

			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Bank Found Successfully");
			res.setData(opt.get());

			return new ResponseEntity<ResponseStructure<Bank>>(res, HttpStatus.OK);

		}

		throw new NoRecordAvailableException("No Bank Found With Given Contact Number");

	}
	
	public ResponseEntity<ResponseStructure<Page<Bank>>> getBankByPaginationAndSorting(int pageNumber,
			int pageSize, String sortBy) {

		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));

		Page<Bank> page = bankRepository.findAll(pageable);

		if (page.isEmpty()) {
			throw new NoRecordAvailableException("No Bank Records Available");
		}

		ResponseStructure<Page<Bank>> res = new ResponseStructure<>();

		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Bank Records Fetched Successfully");
		res.setData(page);

		return new ResponseEntity<ResponseStructure<Page<Bank>>>(res, HttpStatus.OK);

	}
}