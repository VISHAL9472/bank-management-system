package jsp.springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.entity.Address;
import jsp.springboot.entity.Bank;
import jsp.springboot.exception.IdNotFoundException;
import jsp.springboot.exception.NoRecordAvailableException;
import jsp.springboot.repository.AddressRepository;
import jsp.springboot.repository.BankRepository;

@Service
public class AddressService {
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private BankRepository bankRepository;
	
	public ResponseEntity<ResponseStructure<Address>> getAddressById(Integer addressId){

		ResponseStructure<Address> res=new ResponseStructure<>();

		Optional<Address> opt=addressRepository.findById(addressId);

		if(opt.isPresent()) {

			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Address Fetched Successfully");
			res.setData(opt.get());

			return new ResponseEntity<>(res,HttpStatus.OK);
		}

		throw new IdNotFoundException("Address Id Not Found");
	}
	
	public ResponseEntity<ResponseStructure<Address>> updateAddress(Integer addressId, Address address){

		ResponseStructure<Address> res=new ResponseStructure<>();

		Optional<Address> opt=addressRepository.findById(addressId);

		if(opt.isPresent()) {

			Address dbAddress=opt.get();

			dbAddress.setStreet(address.getStreet());
			dbAddress.setCity(address.getCity());
			dbAddress.setState(address.getState());
			dbAddress.setPinCode(address.getPinCode());

			addressRepository.save(dbAddress);

			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Address Updated Successfully");
			res.setData(dbAddress);

			return new ResponseEntity<>(res,HttpStatus.OK);
		}

		throw new IdNotFoundException("Address Id Not Found");
	}
	
	public ResponseEntity<ResponseStructure<Address>> getAddressByBank(Integer bankId){

		ResponseStructure<Address> res = new ResponseStructure<>();

		Optional<Bank> opt = bankRepository.findById(bankId);

		if(opt.isPresent()) {

			Address address = opt.get().getAddress();

			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Address Fetched Successfully");
			res.setData(address);

			return new ResponseEntity<>(res,HttpStatus.OK);
		}

		throw new IdNotFoundException("Bank Id Not Found");
	}
	
	public ResponseEntity<ResponseStructure<List<Address>>> getAddressByCity(String city) {

		ResponseStructure<List<Address>> res = new ResponseStructure<>();

		List<Address> addresses = addressRepository.findByCity(city);

		if (!addresses.isEmpty()) {

			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Addresses Fetched Successfully");
			res.setData(addresses);

			return new ResponseEntity<>(res, HttpStatus.OK);
		}

		throw new NoRecordAvailableException("No Address Found For Given City");
	}
	
	public ResponseEntity<ResponseStructure<List<Address>>> getAddressByCityAndStreet(String city, String street) {

		ResponseStructure<List<Address>> res = new ResponseStructure<>();

		List<Address> addresses = addressRepository.findByCityAndStreet(city, street);

		if (!addresses.isEmpty()) {

			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Addresses Fetched Successfully");
			res.setData(addresses);

			return new ResponseEntity<>(res, HttpStatus.OK);
		}

		throw new NoRecordAvailableException("No Address Found For Given City And Street");
	}

}
