package com.ecommerce.project.Controller;

import com.ecommerce.project.model.Users;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repository.AddressRepo;
import com.ecommerce.project.service.AddressService;
import com.ecommerce.project.utils.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> addAddress(@Valid @RequestBody AddressDTO addressDTO) {
        Users user = authUtil.loggedInUser();
        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO,user);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("getAllAddresses")
    public ResponseEntity<List<AddressDTO>> getAddresses() {
        List<AddressDTO> addressDTOList = addressService.getAddresses();
    return new ResponseEntity<>(addressDTOList, HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId){
        AddressDTO addressDTO = addressService.getAddressesById(addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @GetMapping("userAddress")
    public ResponseEntity<List<AddressDTO>> getUserAddresses( ) {
        Users user = authUtil.loggedInUser();
        List<AddressDTO> addressDTOList = addressService.getAddressesByUserId(user);
        return new ResponseEntity<>(addressDTOList, HttpStatus.OK);
    }
    @PutMapping("/updateAddresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddressById(@PathVariable Long addressId, @RequestBody AddressDTO addressDTO) {
        AddressDTO updatedAddress = addressService.updateAddress(addressId, addressDTO);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }
    @DeleteMapping("deleteAddress/{addressId}")
    public ResponseEntity<String> deleteAddressById(@PathVariable Long addressId) {
        String status = addressService.deleteAddress(addressId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
