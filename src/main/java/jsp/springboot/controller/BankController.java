package jsp.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.Bank;
import jsp.springboot.service.BankService;

@RestController
public class BankController{
	
	@Autowired
	private BankService bankService;
	
	@PostMapping("/banks")
	public ResponseEntity<ResponseStructure<Bank>> saveBank(@RequestBody Bank bank){
		return bankService.saveBank(bank);
	}
	
	@GetMapping("/banks")
	public ResponseEntity<ResponseStructure<List<Bank>>> getAllBanks() {
		return bankService.getAllBanks();
	}
	
	@GetMapping("/banks/{id}")
	public ResponseEntity<ResponseStructure<Bank>> getBankById(@PathVariable Integer id){

		return bankService.getBankById(id);

	}
	
	@PutMapping("/banks/{id}")
	public ResponseEntity<ResponseStructure<Bank>> updateBank(@PathVariable Integer id,
			@RequestBody Bank bank){

		return bankService.updateBank(id, bank);

	}
	
	@DeleteMapping("/banks/{id}")
	public ResponseEntity<ResponseStructure<Bank>> deleteBank(@PathVariable Integer id){

		return bankService.deleteBank(id);

	}
	
	@GetMapping("/banks/ifsc/{ifsc}")
	public ResponseEntity<ResponseStructure<Bank>> getBankByIfsc(@PathVariable String ifsc) {

		return bankService.getBankByIfsc(ifsc);

	}
	
	@GetMapping("/banks/contact/{contactNumber}")
	public ResponseEntity<ResponseStructure<Bank>> getBankByContactNumber(@PathVariable long contactNumber) {

		return bankService.getBankByContactNumber(contactNumber);

	}
	
	@GetMapping("/banks/page")
	public ResponseEntity<ResponseStructure<Page<Bank>>> getBankByPaginationAndSorting(
			@RequestParam int pageNumber,
			@RequestParam int pageSize,
			@RequestParam String sortBy) {

		return bankService.getBankByPaginationAndSorting(pageNumber, pageSize, sortBy);

	}
	
}