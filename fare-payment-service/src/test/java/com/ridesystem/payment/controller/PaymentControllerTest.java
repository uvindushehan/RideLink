package com.ridesystem.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridesystem.payment.dto.*;
import com.ridesystem.payment.exception.DuplicatePaymentException;
import com.ridesystem.payment.exception.PaymentNotFoundException;
import com.ridesystem.payment.model.PaymentMethod;
import com.ridesystem.payment.model.PaymentStatus;
import com.ridesystem.payment.service.FareCalculationService;
import com.ridesystem.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private FareCalculationService fareCalculationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPayment_Success() throws Exception {
        CreatePaymentRequest req = new CreatePaymentRequest("RIDE1", "P1", "D1", new BigDecimal("10"), new BigDecimal("20"), PaymentMethod.CARD);

        PaymentResponse res = PaymentResponse.builder()
                .id("PAY1")
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        Mockito.when(paymentService.createPayment(any(CreatePaymentRequest.class))).thenReturn(res);

        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("PAY1"))
                .andExpect(jsonPath("$.paymentStatus").value("PENDING"));
    }

    @Test
    void createPayment_DuplicatePayment() throws Exception {
        CreatePaymentRequest req = new CreatePaymentRequest("RIDE1", "P1", "D1", new BigDecimal("10"), new BigDecimal("20"), PaymentMethod.CARD);

        Mockito.when(paymentService.createPayment(any(CreatePaymentRequest.class)))
                .thenThrow(DuplicatePaymentException.forRide("RIDE1"));

        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }

    @Test
    void createPayment_ValidationError() throws Exception {
        CreatePaymentRequest req = new CreatePaymentRequest("", "P1", "D1", new BigDecimal("-10"), new BigDecimal("20"), null);

        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPaymentById_Success() throws Exception {
        PaymentResponse res = PaymentResponse.builder().id("PAY1").build();

        Mockito.when(paymentService.getPaymentById("PAY1")).thenReturn(res);

        mockMvc.perform(get("/api/payments/PAY1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("PAY1"));
    }

    @Test
    void getPaymentById_NotFound() throws Exception {
        Mockito.when(paymentService.getPaymentById("PAY1"))
                .thenThrow(PaymentNotFoundException.byId("PAY1"));

        mockMvc.perform(get("/api/payments/PAY1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePaymentStatus_Success() throws Exception {
        UpdatePaymentStatusRequest req = new UpdatePaymentStatusRequest(PaymentStatus.COMPLETED);
        
        PaymentResponse res = PaymentResponse.builder().id("PAY1").paymentStatus(PaymentStatus.COMPLETED).build();
        
        Mockito.when(paymentService.updatePaymentStatus(eq("PAY1"), any(UpdatePaymentStatusRequest.class))).thenReturn(res);

        mockMvc.perform(patch("/api/payments/PAY1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentStatus").value("COMPLETED"));
    }

    @Test
    void refundPayment_Success() throws Exception {
        RefundRequest req = new RefundRequest(new BigDecimal("500"));
        
        PaymentResponse res = PaymentResponse.builder().id("PAY1").paymentStatus(PaymentStatus.REFUNDED).refundAmount(new BigDecimal("500")).build();

        Mockito.when(paymentService.refundPayment(eq("PAY1"), any(RefundRequest.class))).thenReturn(res);

        mockMvc.perform(post("/api/payments/PAY1/refund")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentStatus").value("REFUNDED"))
                .andExpect(jsonPath("$.refundAmount").value(500));
    }
}
