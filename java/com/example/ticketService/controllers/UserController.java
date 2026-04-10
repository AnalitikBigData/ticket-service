package com.example.ticketService.controllers;

import com.example.ticketService.contracts.GetUserByIdResponse;
import com.example.ticketService.contracts.LoginUserRequest;
import com.example.ticketService.contracts.RegisterUserRequest;
import com.example.ticketService.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/registerUser")
    @Operation(summary = "Регистрация пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "201")
    })
    public ResponseEntity<?> registerUser(@Valid RegisterUserRequest request) {
        userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/loginUser")
    @Operation(summary = "Логин")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401")
    })
    public ResponseEntity<String> loginUser(@Valid LoginUserRequest request){
        var response = userService.LoginUser(request);
        if (response){
            return ResponseEntity.ok("Вход пользователя выполнен успешно");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Неверные параметра входа");
    }

    @GetMapping("/getUserById/{id}")
    @Operation(summary = "Получение информации о пользователе")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = GetUserByIdResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema())
            })
    })
    public ResponseEntity<GetUserByIdResponse> getUserById(@PathVariable Long id){
        var response = userService.getUserById(id);
        if (response != null){
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/analytics")
    @Operation(summary = "Аналитика для админов")
    public ResponseEntity<?> analytics(){
        userService.analytics();
        return ResponseEntity.ok("Отправка аналитики");
    }
}
