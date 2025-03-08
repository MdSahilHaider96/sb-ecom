package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIExceptions;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repository.CartRepo;
import com.ecommerce.project.repository.CategoryRepo;
import com.ecommerce.project.repository.ProductRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private CartService cartService;
    @Autowired
    private ModelMapper modelMapper;
    public ProductDTO addProduct(Long categoryId,ProductDTO productDTO) {
        Category category = categoryRepo.findById(Math.toIntExact(categoryId))
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId", Math.toIntExact(categoryId)));
        // validation
        boolean ifProductNotPresent = true;
        List<Product> products = category.getProducts();
        for (Product value : products) {
            if (value.getProductName().equals(productDTO.getProductName())) {
                ifProductNotPresent = false;
                break;
            }
        }
        // Switch DTO to Prodtuct
        if (ifProductNotPresent) {
        Product product = modelMapper.map(productDTO, Product.class);
        product.setCategory(category);
        product.setImage("default.png");
        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepo.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
        }else throw new APIExceptions(" Product Already Exist!! ");
    }
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        // sorting and pagination
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepo.findAll(pageable);
        List<Product> products = productPage.getContent();
        // switch
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
        if (products.isEmpty()) {
            throw new APIExceptions(" No Product Exist!! ");
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOs);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getNumberOfElements());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepo.findById(Math.toIntExact(categoryId))
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId", Math.toIntExact(categoryId)));
        List<Product> products = productRepo.findByCategory(category);
        // sorting and pagination
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepo.findByCategory(category,pageable);
        List<Product> pageContent = productPage.getContent();
        // switch
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
        if (products.isEmpty()) {
            throw new APIExceptions(" No Category  Product Exist!!"+ category.getCategoryName());
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOs);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getNumberOfElements());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        // sorting and pagination
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepo.findByProductNameLikeIgnoreCase('%'+keyword+'%',pageable);
        List<Product> products = productPage.getContent();
        List<ProductDTO> productDTOS =  products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
        if (products.size() == 0) {
            throw new APIExceptions(" No Product Exist with this Keyword "+ keyword);
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getNumberOfElements());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    public ProductDTO updateProduct(long productId, ProductDTO productDTO) {
        //Get the existing Products
       Product productFromDb =  productRepo.findById(productId)
               .orElseThrow(() -> new ResourceNotFoundException("product","productId", Math.toIntExact(productId)));
       //Update the Product with the one in Request Body
        Product updatedProduct = modelMapper.map(productDTO, Product.class);
        productFromDb.setProductName(updatedProduct.getProductName());
        productFromDb.setDescription(updatedProduct.getDescription());
        productFromDb.setQuantity(updatedProduct.getQuantity());
        productFromDb.setDiscount(updatedProduct.getDiscount());
        productFromDb.setPrice(updatedProduct.getPrice());
        productFromDb.setSpecialPrice(updatedProduct.getSpecialPrice());
        Product savedProduct =productRepo.save(productFromDb);
        List<Cart> carts = cartRepo.findCartsByProductId(productId);

        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<ProductDTO> products = cart.getCartItem().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

            cartDTO.setProducts(products);

            return cartDTO;

        }).collect(Collectors.toList());

        cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));

        return  modelMapper.map(savedProduct, ProductDTO.class);
    }

    public ProductDTO deleteProduct(long productId) {
        Product product =  productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product","productId", Math.toIntExact(productId)));
        // DELETE
        List<Cart> carts = cartRepo.findCartsByProductId(productId);
        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

        productRepo.delete(product);
        return  modelMapper.map(product, ProductDTO.class);
    }

    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        // gEt the Product from Db
        Product productFromDb = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product","productId", Math.toIntExact(productId)));
        //upload image to server
        //get the file name of uploaded image
        String path = "images/";
        String filename = uploadImage(path,image);
        //updating the new file name to the product
        productFromDb.setImage(filename);
        //save Updated Product
        Product savedProduct = productRepo.save(productFromDb);
        // returning DTO after mappping product to DTO
        return  modelMapper.map(savedProduct, ProductDTO.class);
        }

    private String uploadImage(String path, MultipartFile file) throws IOException {
        //Get The File Name of Current / original File
        String originalFilename = file.getOriginalFilename();
        // Generate a Unique File Name
        String randromId = UUID.randomUUID().toString();
        String fileName = randromId.concat(originalFilename.substring(originalFilename.lastIndexOf('.')));
        String filePath = path + File.separator + fileName;
        //Check if path exist otherwise create the path
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        //upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));
        // return file NAme
        return fileName;
    }

}
