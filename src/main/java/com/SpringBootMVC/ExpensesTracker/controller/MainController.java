package com.SpringBootMVC.ExpensesTracker.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.SpringBootMVC.ExpensesTracker.DTO.ExpenseDTO;
import com.SpringBootMVC.ExpensesTracker.DTO.FilterDTO;
import com.SpringBootMVC.ExpensesTracker.entity.Category;
import com.SpringBootMVC.ExpensesTracker.entity.Client;
import com.SpringBootMVC.ExpensesTracker.entity.Expense;
import com.SpringBootMVC.ExpensesTracker.service.CategoryService;
import com.SpringBootMVC.ExpensesTracker.service.ExpenseService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
    ExpenseService expenseService;
    CategoryService categoryService;

    @Autowired
    public MainController(ExpenseService expenseService, CategoryService categoryService) {
        this.expenseService = expenseService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String landingPage(HttpSession session, Model model){
        Client client = (Client) session.getAttribute("client");
        model.addAttribute("sessionClient", client);
        return "landing-page";
    }

    @GetMapping("/showAdd")
    public String addExpense(Model model){
        model.addAttribute("expense", new ExpenseDTO());
        return "add-expense";
    }

    @PostMapping("/submitAdd")
    public String submitAdd(@ModelAttribute("expense") ExpenseDTO expenseDTO, HttpSession session){
        Client client = (Client) session.getAttribute("client");
        expenseDTO.setClientId(client.getId());
        expenseService.save(expenseDTO);
        return "redirect:/list";
    }



    // @GetMapping("/list")
    // public String list(Model model, HttpSession session){
    //     Client client = (Client) session.getAttribute("client");
    //     int clientId = client.getId();
    //     List<Expense> expenseList = expenseService.findAllExpensesByClientId(clientId);
    //     for (Expense expense : expenseList){
    //         expense.setCategoryName(categoryService.findCategoryById(expense.getCategory().getId()).getName());
    //         expense.setDate(LocalDateTime.parse(expense.getDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate().toString());
    //         expense.setTime(LocalDateTime.parse(expense.getDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalTime().toString());
    //     }
    //     model.addAttribute("expenseList", expenseList);
    //     model.addAttribute("filter", new FilterDTO());
    //     return "list-page";
    // }


    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        Client client = (Client) session.getAttribute("client");

        if (client == null) {
            return "redirect:/"; // Redirect to login/landing page
        }

        int clientId = client.getId();
        List<Expense> expenseList = expenseService.findAllExpensesByClientId(clientId);

        for (Expense expense : expenseList) {
            // Check if category is null before accessing its name
            Category category = expense.getCategory();
            if (category != null) {
                expense.setCategoryName(expense.getCategory().getName());
            } else {
                expense.setCategoryName("Unknown"); // Handle missing category gracefully
            }

            // Parse the date and time
            LocalDateTime dateTime = LocalDateTime.parse(expense.getDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            expense.setDate(dateTime.toLocalDate().toString());
            expense.setTime(dateTime.toLocalTime().toString());
        }

        model.addAttribute("expenseList", expenseList);
        model.addAttribute("filter", new FilterDTO());

        return "list-page";
    }

    @GetMapping("/showUpdate")
    public String showUpdate(@RequestParam("expId") int id, Model model){
        Expense expense = expenseService.findExpenseById(id);
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setAmount(expense.getAmount());
        expenseDTO.setCategory(expense.getCategory().getName());
        expenseDTO.setDescription(expense.getDescription());
        expenseDTO.setDateTime(expense.getDateTime());

        model.addAttribute("expense", expenseDTO);
        model.addAttribute("expenseId", id);
        return "update-page";
    }

    @PostMapping("/submitUpdate")
    public String update(@RequestParam("expId") int id, @ModelAttribute("expense") ExpenseDTO expenseDTO, HttpSession session){
        Client client = (Client) session.getAttribute("client");
        expenseDTO.setExpenseId(id);
        expenseDTO.setClientId(client.getId());
        expenseService.update(expenseDTO);
        return "redirect:/list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("expId") int id){
        expenseService.deleteExpenseById(id);
        return "redirect:/list";
    }


    // @PostMapping("/processFilter")
    // public String processFilter(@ModelAttribute("filter") FilterDTO filter, Model model){
    //     System.out.println("--------------------------------------------------------------");
    //     System.out.println("filter values : " + filter);
    //     List<Expense> expenseList = expenseService.findFilterResult(filter);
    //     System.out.println("size ----> " + expenseList.size());
    //     System.out.println(expenseList);

    //     for (Expense expense : expenseList){
    //         expense.setCategoryName(categoryService.findCategoryById(expense.getCategory().getId()).getName());
    //         expense.setDate(LocalDateTime.parse(expense.getDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate().toString());
    //         expense.setTime(LocalDateTime.parse(expense.getDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalTime().toString());
    //     }
    //     model.addAttribute("expenseList", expenseList);
    //     return "filter-result";
    // }

    @PostMapping("/processFilter")
    public String processFilter(@ModelAttribute("filter") FilterDTO filter, Model model) {
        System.out.println("--------------------------------------------------------------");
        System.out.println("filter values : " + filter);
        
        // Get filtered expense list
        List<Expense> expenseList = expenseService.findFilterResult(filter);
        System.out.println("size ----> " + expenseList.size());
        
        // Check for valid expense list
        if (expenseList == null || expenseList.isEmpty()) {
            System.out.println("No expenses found for the given filter.");
        } else {
            System.out.println(expenseList);
        }

        // Process each expense item
        for (Expense expense : expenseList) {
            if (expense.getCategory() != null) {
                try {
                    // Ensure that category is not null and we can fetch its name
                    String categoryName = categoryService.findCategoryById(expense.getCategory().getId()).getName();
                    expense.setCategoryName(categoryName);
                } catch (Exception e) {
                    System.out.println("Error fetching category for expense: " + expense.getId() + ". " + e.getMessage());
                    expense.setCategoryName("Unknown");
                }
            } else {
                expense.setCategoryName("Unknown");
            }

            // Handle date and time parsing
            try {
                if (expense.getDateTime() != null) {
                    LocalDateTime dateTime = LocalDateTime.parse(expense.getDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    expense.setDate(dateTime.toLocalDate().toString()); // Set date
                    expense.setTime(dateTime.toLocalTime().toString()); // Set time
                }
            } catch (Exception e) {
                System.out.println("Error parsing date/time for expense " + expense.getId() + ": " + e.getMessage());
            }
        }

        // Add expense list to the model
        model.addAttribute("expenseList", expenseList);
        return "filter-result";
    }



}
