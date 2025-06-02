package controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import service.EmployeService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employes")
 public class EmployeController {

         private final EmployeService employeService;

         }