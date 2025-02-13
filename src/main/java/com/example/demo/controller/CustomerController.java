package com.example.demo.controller;

import com.example.demo.dto.requests.CustomerRequest;
import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepo;
import com.example.demo.service.CustomerService;
import com.example.demo.service.ExportService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.Pattern;
import org.hibernate.Internal;
import org.hibernate.annotations.IdGeneratorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private ExportService exportService;

    @PostMapping("/addCustomer")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<Customer> addCustomer(
            @Valid @RequestPart("customer") CustomerRequest customerRequest,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        try {
            byte[] imageData = null;
            if (image != null && !image.isEmpty()) {
                imageData = image.getBytes();
            }

            Customer customer = new Customer(customerRequest.getId(),
                    customerRequest.getName(),
                    customerRequest.getAge(),
                    customerRequest.getEmail(),
                    imageData
            );

            Customer savedCustomer = customerService.addCustomer(customer);
            return ResponseEntity.ok(savedCustomer);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getCustomer")
    @CrossOrigin(origins = "http://localhost:5173")
    public Page<Map<String, Object>> getCustomer(@RequestParam(required = false) String search,
                                                 @RequestParam(required = false) String sortBy,
                                                 @RequestParam(required = false) String order,
                                                 @RequestParam(required = false) Integer filterAge,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "3") int size,
                                                 @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        System.out.println("Authorization Header: " + authorizationHeader); // Log the header
        if (authorizationHeader == null) {
            System.out.println("Authorization header is missing");
        }
        return customerService.getCustomer(search, sortBy, order, filterAge, page, size);
    }


    @PostMapping("/updateCustomer")
    @CrossOrigin(origins = "http://localhost:5173")
    public Customer updateCustomer(@Valid @RequestBody CustomerRequest customerRequest) {
        int id = customerRequest.getId();
        String name = customerRequest.getName();
        int age = customerRequest.getAge();
        String email = customerRequest.getEmail();
        Customer customer = new Customer(id, name, age, email, null);
        return customerService.updateCustomer(customer);
    }

    @GetMapping("/deleteCustomer/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<Boolean> deleteCustomer(@Valid @PathVariable int id) {
        return ResponseEntity.ok(customerService.deleteCustomer(id));
    }

    @GetMapping("/viewCustomer/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<Map<String, Object>> viewCustomer(@PathVariable int id) {
        Optional<Customer> customerOpt = customerRepo.findById(id);

        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            Map<String, Object> customerData = new HashMap<>();

            customerData.put("id", customer.getId());
            customerData.put("name", customer.getName());
            customerData.put("age", customer.getAge());
            customerData.put("email", customer.getEmail());

            // Convert byte array to Base64 string
            if (customer.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(customer.getImage());
                customerData.put("image", "data:image/jpeg;base64," + base64Image);
            } else {
                customerData.put("image", null);
            }

            return ResponseEntity.ok(customerData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/export/pdf")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<byte[]> exportToPDF() throws IOException {
        return exportService.exportToPDF();
    }

    @GetMapping("/export/excel")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<byte[]> exportToExcel() {
        return exportService.exportToExcel();
    }

}
