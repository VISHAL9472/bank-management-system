package jsp.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.Address;
import jsp.springboot.service.AddressService;


@RestController
@RequestMapping("/addresses")
public class AddressController {
	
	@Autowired
	private AddressService addressService;
	
	@GetMapping("/{addressId}")
	public ResponseEntity<ResponseStructure<Address>> getAddressById(
			@PathVariable Integer addressId){

		return addressService.getAddressById(addressId);
	}
	
	@PutMapping("/{addressId}")
	public ResponseEntity<ResponseStructure<Address>> updateAddress(

			@PathVariable Integer addressId,
			@RequestBody Address address){

		return addressService.updateAddress(addressId, address);
	}
	
	@GetMapping("/bank/{bankId}")
	public ResponseEntity<ResponseStructure<Address>> getAddressByBank(
			@PathVariable Integer bankId){

		return addressService.getAddressByBank(bankId);
	}
	
	@GetMapping("/city/{city}")
	public ResponseEntity<ResponseStructure<List<Address>>> getAddressByCity(
			@PathVariable String city) {

		return addressService.getAddressByCity(city);
	}
	
	@GetMapping("/city/{city}/street/{street}")
	public ResponseEntity<ResponseStructure<List<Address>>> getAddressByCityAndStreet(
			@PathVariable String city,
			@PathVariable String street) {

		return addressService.getAddressByCityAndStreet(city, street);
	}

}
