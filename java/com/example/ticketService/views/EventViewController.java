package com.example.ticketService.views;

import com.example.ticketService.contracts.AddOrUpdateEventRequest;
import com.example.ticketService.contracts.GetEventByIdResponse;
import com.example.ticketService.contracts.GetEventsResponse;
import com.example.ticketService.entities.Constants;
import com.example.ticketService.services.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class EventViewController {
    @Autowired
    private EventService eventService;


    @GetMapping("/")
    public String getIndexPage(Model model) {
        var event = eventService.getNearestEvent();
        model.addAttribute("hotEventName", event.getName());
        model.addAttribute("hotEventDate", event.getDate());
        return "index";
    }

    @GetMapping("/events")
    public String getEventsPage(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateEnd,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "6") Integer count,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        List<GetEventsResponse> events = eventService.getEvents(category, dateStart, dateEnd, page, count, sortBy, direction);

        model.addAttribute("events", events);
        model.addAttribute("currentPage", page);
        model.addAttribute("category", category);
        model.addAttribute("dateStart", dateStart);
        model.addAttribute("dateEnd", dateEnd);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        return "events-list";
    }

    @GetMapping("/events/{id}")
    public String getEventDetails(@PathVariable Long id, Model model) {
        
        GetEventByIdResponse event = eventService.getEventById(id);

        if (event == null) {
            return "redirect:/events"; 
        }

        model.addAttribute("event", event);
        return "event-details";
    }

    @GetMapping("/contacts")
    public String getContactsPage(Model model) {
        return "contacts";
    }


    @GetMapping("/admin/events")
    public String listAdminEvents(Model model) {
        model.addAttribute("events", eventService.getEvents(null, null, null, 0, 100, "date", "asc"));
        return "admin-events";
    }


    @GetMapping("/admin/events/new")
    public String showCreateForm(Model model) {
        model.addAttribute("eventRequest", new AddOrUpdateEventRequest());
        model.addAttribute("categories", Constants.Category.values());
        model.addAttribute("isEdit", false);
        return "event-form";
    }


    @GetMapping("/admin/events/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        GetEventByIdResponse event = eventService.getEventById(id);


        AddOrUpdateEventRequest request = new AddOrUpdateEventRequest();
        request.setEventId(id);
        request.setName(event.getName());
        request.setCity(event.getCity());
        request.setCategory(event.getCategory());
        request.setDate(event.getDate());
        request.setNumberSeats(event.getNumberSeats());
        request.setStatus(event.getStatus());

        model.addAttribute("eventRequest", request);
        model.addAttribute("categories", Constants.Category.values());
        model.addAttribute("isEdit", true);
        return "event-form";
    }

    @PostMapping("/admin/events/save")
    public String saveEvent(@Valid @ModelAttribute("eventRequest") AddOrUpdateEventRequest request,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", Constants.Category.values());
            model.addAttribute("isEdit", request.getEventId() != null);
            return "event-form";
        }
        eventService.addOrUpdateEvent(request);
        return "redirect:/admin/events";
    }


}
