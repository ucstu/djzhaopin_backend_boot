package com.ucstu.guangbt.djzhaopin.controller.user;

import java.util.UUID;

import com.ucstu.guangbt.djzhaopin.entity.user.UserInformation;
import com.ucstu.guangbt.djzhaopin.model.PageResult;
import com.ucstu.guangbt.djzhaopin.model.ResponseBody;
import com.ucstu.guangbt.djzhaopin.service.UserInformationService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Validated
@CrossOrigin
@RestController
@RequestMapping("/userInfos")
public class UserInformationController {

    @Resource
    private UserInformationService userInformationService;

    @PostMapping("")
    public ResponseEntity<ResponseBody<UserInformation>> createUserInformation(
            @Valid @RequestBody UserInformation userInformation) {
        return ResponseBody.handle(userInformationService.createUserInformation(userInformation));
    }

    @DeleteMapping("/{userInformationId}")
    public ResponseEntity<ResponseBody<UserInformation>> deleteUserInformationByUserInformationId(
            @PathVariable("userInformationId") @NotNull UUID userInformationId) {
        return ResponseBody.handle(userInformationService.deleteUserInformationByUserInformationId(userInformationId));
    }

    @PutMapping("/{userInfoId}")
    public ResponseEntity<ResponseBody<UserInformation>> updateUserInformationByUserInformationId(
            @PathVariable("userInfoId") @NotNull UUID userInformationId,
            @Valid @RequestBody UserInformation userInformation) {
        return ResponseBody.handle(userInformationService
                .updateUserInformationByUserInformationId(userInformationId, userInformation));
    }

    @GetMapping("")
    public ResponseEntity<ResponseBody<PageResult<UserInformation>>> getUserInformations(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseBody.handle(userInformationService
                .getUserInformations(pageable));
    }

    @GetMapping("/{userInfoId}")
    public ResponseEntity<ResponseBody<UserInformation>> getUserInformationByUserInformationId(
            @PathVariable("userInfoId") @NotNull UUID userInformationId) {
        return ResponseBody.handle(userInformationService
                .getUserInformationByUserInformationId(userInformationId));
    }

}
