package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.Users;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repository.AddressRepo;
import com.ecommerce.project.repository.UserRepo;
import com.ecommerce.project.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    AddressRepo addressRepo;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private AuthUtil authUtil;

    public AddressDTO createAddress(AddressDTO addressDTO, Users user) {
        Address address = modelMapper.map(addressDTO, Address.class);
        address.setUser(user);
        List<Address> addressesList = user.getAddresses();
        addressesList.add(address);
        user.setAddresses(addressesList);
        Address savedAddress = addressRepo.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    public List<AddressDTO> getAddresses() {
        List<Address> addressesList = addressRepo.findAll();
        return addressesList.stream().map(address -> modelMapper.map(address, AddressDTO.class)).collect(Collectors.toList());
    }

    public AddressDTO getAddressesById(Long addressId) {
        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", Math.toIntExact(addressId)));
        return modelMapper.map(address, AddressDTO.class);
    }

    public List<AddressDTO> getAddressesByUserId(Users user) {
        List<Address> addressesList = user.getAddresses();
        return addressesList.stream().map(address -> modelMapper.map(address, AddressDTO.class)).collect(Collectors.toList());
    }

    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Address addressFromDataBase = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", Math.toIntExact(addressId)));
        addressFromDataBase.setCity(addressDTO.getCity());
        addressFromDataBase.setState(addressDTO.getState());
        addressFromDataBase.setPincode(addressDTO.getPincode());
        addressFromDataBase.setCountry(addressDTO.getCountry());
        addressFromDataBase.setStreet(addressDTO.getStreet());
        addressFromDataBase.setBuildingName(addressDTO.getBuildingName());

        Address updatedAddress = addressRepo.save(addressFromDataBase);

        Users user = addressFromDataBase.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);
        userRepo.save(user);
        return modelMapper.map(addressFromDataBase, AddressDTO.class);

    }

    public String deleteAddress(Long addressId) {
        Address addressFromDataBase = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", Math.toIntExact(addressId)));
        Users user = addressFromDataBase.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        userRepo.save(user);
        addressRepo.delete(addressFromDataBase);
        return "Address successfully deleted with AddressId: " + addressId;
    }
}
