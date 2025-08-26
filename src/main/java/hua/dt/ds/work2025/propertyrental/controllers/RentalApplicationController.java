package hua.dt.ds.work2025.propertyrental.controllers;

import hua.dt.ds.work2025.propertyrental.entities.*;
import hua.dt.ds.work2025.propertyrental.service.PropertyForRentService;
import hua.dt.ds.work2025.propertyrental.service.RentalApplicationService;
import hua.dt.ds.work2025.propertyrental.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("rentalapplication")
public class RentalApplicationController {

    private RentalApplicationService  rentalApplicationService;
    private PropertyForRentService propertyForRentService;
    private UserService userService;

    public RentalApplicationController(RentalApplicationService rentalApplicationService, PropertyForRentService propertyForRentService,
                                       UserService userService) {
        this.rentalApplicationService = rentalApplicationService;
        this.propertyForRentService = propertyForRentService;
        this.userService = userService;
    }

    @PostMapping("/newApplication/{id}")
    public String newRentalApplication(@PathVariable Long id,
                                       @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser,
                                      @RequestParam String rentalNotes) {

        User loggedInUser = userService.findByUsername(springUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        rentalApplicationService.createApplication(id, loggedInUser.getId(), rentalNotes);
        return "redirect:/propertyforrent/tenantlist";
    }

    @GetMapping("/tenantList")
    public String listRentalApplicationsByTenant(@AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser,
                                                 Model model) {

        User loggedInUser = userService.findByUsername(springUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("firstname", loggedInUser.getFirstname());
        model.addAttribute("surname", loggedInUser.getSurname());

        List<RentalApplication> rentalApplications = rentalApplicationService.getApplicationsByUserId(loggedInUser.getId());
        model.addAttribute("rentalApplications", rentalApplications);
        model.addAttribute("showApproveRejectButtons", false);
        return "rentalapplication/rentalapplicationlist";

    }

    @GetMapping("/propertyDetails/{id}")
    public String propertDetails(@PathVariable Long id, Model model) {
        RentalApplication rentalApplication = rentalApplicationService.getRentalApplication(id);
        PropertyForRent  propertyForRent = rentalApplication.getPropertyForRent();
        Property property = propertyForRent.getProperty();
        model.addAttribute("propertyforrent", propertyForRent);
        model.addAttribute("property", property);
        return "/propertyforrent/propertydetails";
    }

    @PostMapping("/cancelApplication/{id}")
    public String deleteRentalApplication(@PathVariable Long id) {
        rentalApplicationService.deleteRentalApplication(id);
        return "redirect:/rentalapplication/tenantList";
    }

    @PostMapping("/approved/{id}")
    public String approveApplication(@PathVariable Long id) {
        RentalApplication rentalApplication = rentalApplicationService.getRentalApplication(id);

        rentalApplication.setStatus(RentalApplicationStatus.APPROVED);
        rentalApplication.setDecisionDate(LocalDateTime.now());
        rentalApplicationService.saveRentalApplication(rentalApplication);
        PropertyForRent propertyForRent = rentalApplication.getPropertyForRent();
        return "redirect:/propertyforrent/rentalapplications/" + propertyForRent.getId(); // ή όπου δείχνεις τη λίστα
    }

    @PostMapping("/rejected/{id}")
    public String rejectApplication(@PathVariable Long id) {
        RentalApplication rentalApplication = rentalApplicationService.getRentalApplication(id);

        rentalApplication.setStatus(RentalApplicationStatus.REJECTED);
        rentalApplication.setDecisionDate(LocalDateTime.now());
        rentalApplicationService.saveRentalApplication(rentalApplication);
        PropertyForRent propertyForRent = rentalApplication.getPropertyForRent();
        return "redirect:/propertyforrent/rentalapplications/" + propertyForRent.getId(); // ή όπου δείχνεις τη λίστα
    }

}
