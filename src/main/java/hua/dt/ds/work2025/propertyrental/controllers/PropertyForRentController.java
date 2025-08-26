package hua.dt.ds.work2025.propertyrental.controllers;

import hua.dt.ds.work2025.propertyrental.entities.*;
import hua.dt.ds.work2025.propertyrental.service.PropertyForRentService;
import hua.dt.ds.work2025.propertyrental.service.PropertyService;
import hua.dt.ds.work2025.propertyrental.service.RentalApplicationService;
import hua.dt.ds.work2025.propertyrental.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("propertyforrent")
public class PropertyForRentController {

    private final RentalApplicationService rentalApplicationService;
    private final PropertyService propertyService;
    private PropertyForRentService propertyForRentService;
    private UserService userService;

    public PropertyForRentController(PropertyForRentService propertyForRentService, UserService userService, RentalApplicationService rentalApplicationService, PropertyService propertyService) {
        this.propertyForRentService = propertyForRentService;
        this.userService = userService;
        this.rentalApplicationService = rentalApplicationService;
        this.propertyService = propertyService;
    }


    @RequestMapping()
    public String show(Model model) {
        model.addAttribute("propertyforrent", new  PropertyForRent());
        model.addAttribute("propertiesforrent", propertyForRentService.getPropertiesForRent());
        return "/propertyforrent/propertyforrentlist";
    }

    @GetMapping("/new")
    public String addPropetyForRent(Model model){
        PropertyForRent propertyForRent = new PropertyForRent();
        model.addAttribute("propertyforrent", propertyForRent);
        return "/propertyforrent/propertyforrentform";

    }

    @PostMapping("/save")
    public String savePropertyForRent(
            @Valid @ModelAttribute("propertyforrent") PropertyForRent propertyForRent,
            BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            System.out.println("error");
            return "/propertyforrent/propertyform";
        }

        propertyForRentService.savePropertyForRent(propertyForRent);
        model.addAttribute("propertiesforrent", propertyForRentService.getPropertiesForRent());
        model.addAttribute("successMessage", "PropertyForRent added successfully!");
        return "propertyforrent/propertyforrentlist";
    }

    @GetMapping("/list")
    public String listProperties(Model model,
                                 @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {

        if (springUser == null) {
            System.out.println("No user logged in!");
            return "redirect:/login";
        }

        User loggedInUser = userService.findByUsername(springUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found in DB"));


        model.addAttribute("firstname", loggedInUser.getFirstname());
        model.addAttribute("surname", loggedInUser.getSurname());
        List<PropertyForRent> myPropertiesForRent = propertyForRentService.getPropertiesForOwner(loggedInUser);
        model.addAttribute("propertiesforrent", myPropertiesForRent);
        return "propertyforrent/propertyforrentlist";
    }

    @GetMapping("/ownerlist")
    public String listOwnerProperties(Model model,
                                      @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {

        if (springUser == null) {
            System.out.println("No user logged in!");
            return "redirect:/login";
        }

        User loggedInUser = userService.findByUsername(springUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found in DB"));


        model.addAttribute("firstname", loggedInUser.getFirstname());
        model.addAttribute("surname", loggedInUser.getSurname());

        List<PropertyForRent> myPropertiesForRent = propertyForRentService.getPropertiesForOwner(loggedInUser);

        model.addAttribute("propertiesforrent", myPropertiesForRent);
        return "propertyforrent/propertyforrentlist";
    }

    @GetMapping("/tenantlist")
    public String listTenantProperties(Model model,
                                       @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {
        if (springUser == null) {
            System.out.println("No user logged in!");
            return "redirect:/login";
        }

        User loggedInUser = userService.findByUsername(springUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("firstname", loggedInUser.getFirstname());
        model.addAttribute("surname", loggedInUser.getSurname());

        List<PropertyForRent> availablePropertiesForRent = propertyForRentService.getAvailablePropertiesForRent();
        List<Long> appliedPropertyIds = rentalApplicationService.getAppliedPropertyIdsByUserId(loggedInUser.getId());

        model.addAttribute("propertiesforrent", availablePropertiesForRent);
        model.addAttribute("appliedPropertyIds", appliedPropertyIds);

        return "propertyforrent/propertyforrentlist";
    }

    @GetMapping("/edit/{id}")
    public String editPropertyForRent(@PathVariable Long id, Model model) {
        PropertyForRent propertyforrent = propertyForRentService.getPropertyForRent(id);
        Long propertyId = propertyforrent.getProperty().getId();
        return "redirect:/properties/edit/" +  propertyId;
    }

    @GetMapping("/details/{id}")
    public String detailsPropertyForRent(@PathVariable Long id, Model model) {
        PropertyForRent propertyforrent = propertyForRentService.getPropertyForRent(id);
        Property property = propertyforrent.getProperty();
        model.addAttribute("propertyforrent", propertyforrent);
        model.addAttribute("property", property);
        model.addAttribute("showCompleteRentalForm", true);
        return "/propertyforrent/propertydetails";
    }

    @PostMapping("/delete/{id}")
    public String deletePropertyForRent(@PathVariable Long id) {
        Property property = propertyForRentService.getPropertyForRent(id).getProperty();
        propertyService.deleteProperty(property.getId());
        propertyForRentService.deletePropertyForRent(id);
        return "redirect:/propertyforrent/list";
    }


    @GetMapping("/rentalapplications/{id}")
    public String showRentalApplications(@PathVariable("id") Integer id, Model model,
                                         @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser){

        if (springUser == null) {
            System.out.println("No user logged in!");
            return "redirect:/login";
        }

        User loggedInUser = userService.findByUsername(springUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("firstname", loggedInUser.getFirstname());
        model.addAttribute("surname", loggedInUser.getSurname());

        PropertyForRent propertyForRent = propertyForRentService.getPropertyForRent(id);
        model.addAttribute("rentalApplications", propertyForRent.getRentalApplications());
        model.addAttribute("showApproveRejectButtons", true);
        return "rentalapplication/rentalapplicationlist";
    }


    @GetMapping("/resultslist")
    public String listProperties(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Integer minSquareMeters,
            @RequestParam(required = false) Integer maxSquareMeters,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear,
            @RequestParam(required = false) Integer floor,
            @RequestParam(required = false) Integer bedrooms,
            @RequestParam(required = false) Boolean parking,
            @RequestParam(required = false) Boolean autonomousHeating,
            @RequestParam(required = false) String clear,
            Model model,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {

        if (springUser == null) {
            return "redirect:/login";
        }

        User loggedInUser = userService.findByUsername(springUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("firstname", loggedInUser.getFirstname());
        model.addAttribute("surname", loggedInUser.getSurname());

        if (clear != null) {
            city = null;
            minSquareMeters = null;
            maxSquareMeters = null;
            minPrice = null;
            maxPrice = null;
            minYear = null;
            maxYear = null;
            floor = null;
            bedrooms = null;
            parking = null;
            autonomousHeating = null;
        }

        if (clear != null) {
            model.addAttribute("propertiesforrent", Collections.emptyList());
            model.addAttribute("appliedPropertyIds", Collections.emptyList());
            return "/auth/tenant-dashboard";
        }


        List<PropertyForRent> propertiesForRent = propertyForRentService.searchPropertiesForRent(
                city, minSquareMeters, maxSquareMeters, minPrice, maxPrice,
                minYear, maxYear, floor, bedrooms, parking, autonomousHeating
        );

        List<Long> appliedPropertyIds = rentalApplicationService.getAppliedPropertyIdsByUserId(loggedInUser.getId());

        model.addAttribute("propertiesforrent", propertiesForRent);
        model.addAttribute("appliedPropertyIds", appliedPropertyIds);
        return "/auth/tenant-dashboard";
    }

}


