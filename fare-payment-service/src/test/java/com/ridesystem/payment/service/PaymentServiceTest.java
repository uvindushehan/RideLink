package com.ridesystem.payment.service;

import com.ridesystem.payment.dto.*;
import com.ridesystem.payment.exception.DuplicatePaymentException;
import com.ridesystem.payment.exception.InvalidPaymentOperationException;
import com.ridesystem.payment.exception.PaymentNotFoundException;
import com.ridesystem.payment.model.Payment;
import com.ridesystem.payment.model.PaymentMethod;
import com.ridesystem.payment.model.PaymentStatus;
import com.ridesystem.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private FareCalculationService fareCalculationService;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void createPayment_Success() {
        CreatePaymentRequest req = new CreatePaymentRequest();
        req.setRideId("RIDE1");
        req.setPassengerId("P1");
        req.setDriverId("D1");
        req.setDistanceKm(new BigDecimal("10"));
        req.setDurationMinutes(new BigDecimal("20"));
        req.setPaymentMethod(PaymentMethod.CARD);

        when(paymentRepository.existsByRideId("RIDE1")).thenReturn(false);
        
        FareCalculationResult fareResult = FareCalculationResult.builder()
                .baseFare(new BigDecimal("200.00"))
                .distanceFare(new BigDecimal("800.00"))
                .timeFare(new BigDecimal("100.00"))
                .totalAmount(new BigDecimal("1100.00"))
                .build();
        when(fareCalculationService.calculate(new BigDecimal("10"), new BigDecimal("20"))).thenReturn(fareResult);
        
        Payment saved = new Payment();
        saved.setId("PAY1");
        saved.setRideId("RIDE1");
        saved.setPassengerId("P1");
        saved.setDriverId("D1");
        saved.setPaymentStatus(PaymentStatus.PENDING);
        saved.setTotalAmount(new BigDecimal("1100.00"));
        
        when(paymentRepository.save(any(Payment.class))).thenReturn(saved);

        PaymentResponse res = paymentService.createPayment(req);
        
        assertNotNull(res);
        assertEquals("PAY1", res.getId());
        assertEquals(PaymentStatus.PENDING, res.getPaymentStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void createPayment_DuplicatePaymentRejection() {
        CreatePaymentRequest req = new CreatePaymentRequest();
        req.setRideId("RIDE1");
        when(paymentRepository.existsByRideId("RIDE1")).thenReturn(true);

        assertThrows(DuplicatePaymentException.class, () -> {
            paymentService.createPayment(req);
        });
        
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void getPaymentById_Success() {
        Payment p = new Payment();
        p.setId("PAY1");
        when(paymentRepository.findById("PAY1")).thenReturn(Optional.of(p));

        PaymentResponse res = paymentService.getPaymentById("PAY1");
        assertNotNull(res);
        assertEquals("PAY1", res.getId());
    }

    @Test
    void getPaymentById_NotFound() {
        when(paymentRepository.findById("PAY1")).thenReturn(Optional.empty());
        assertThrows(PaymentNotFoundException.class, () -> {
            paymentService.getPaymentById("PAY1");
        });
    }

    @Test
    void updatePaymentStatus_Success() {
        Payment p = new Payment();
        p.setId("PAY1");
        p.setPaymentStatus(PaymentStatus.PENDING);
        
        when(paymentRepository.findById("PAY1")).thenReturn(Optional.of(p));
        when(paymentRepository.save(any(Payment.class))).thenReturn(p);

        UpdatePaymentStatusRequest req = new UpdatePaymentStatusRequest();
        req.setPaymentStatus(PaymentStatus.COMPLETED);

        PaymentResponse res = paymentService.updatePaymentStatus("PAY1", req);
        assertEquals(PaymentStatus.COMPLETED, res.getPaymentStatus());
    }

    @Test
    void refundPayment_Success() {
        Payment p = new Payment();
        p.setId("PAY1");
        p.setPaymentStatus(PaymentStatus.COMPLETED);
        p.setTotalAmount(new BigDecimal("1000.00"));

        when(paymentRepository.findById("PAY1")).thenReturn(Optional.of(p));
        when(paymentRepository.save(any(Payment.class))).thenReturn(p);

        RefundRequest req = new RefundRequest();
        req.setRefundAmount(new BigDecimal("500.00"));

        PaymentResponse res = paymentService.refundPayment("PAY1", req);
        assertEquals(PaymentStatus.REFUNDED, res.getPaymentStatus());
        assertEquals(new BigDecimal("500.00"), res.getRefundAmount());
        assertNotNull(res.getRefundedAt());
    }

    @Test
    void refundPayment_RejectedIfNotCompleted() {
        Payment p = new Payment();
        p.setId("PAY1");
        p.setPaymentStatus(PaymentStatus.PENDING);
        when(paymentRepository.findById("PAY1")).thenReturn(Optional.of(p));

        RefundRequest req = new RefundRequest();
        req.setRefundAmount(new BigDecimal("500.00"));

        assertThrows(InvalidPaymentOperationException.class, () -> {
            paymentService.refundPayment("PAY1", req);
        });
    }

    @Test
    void refundPayment_RejectedIfAmountExceedsTotal() {
        Payment p = new Payment();
        p.setId("PAY1");
        p.setPaymentStatus(PaymentStatus.COMPLETED);
        p.setTotalAmount(new BigDecimal("1000.00"));
        when(paymentRepository.findById("PAY1")).thenReturn(Optional.of(p));

        RefundRequest req = new RefundRequest();
        req.setRefundAmount(new BigDecimal("1500.00"));

        assertThrows(InvalidPaymentOperationException.class, () -> {
            paymentService.refundPayment("PAY1", req);
        });
    }
    
    @Test
    void refundPayment_RejectedIfAlreadyRefunded() {
        Payment p = new Payment();
        p.setId("PAY1");
        p.setPaymentStatus(PaymentStatus.REFUNDED);
        p.setTotalAmount(new BigDecimal("1000.00"));
        when(paymentRepository.findById("PAY1")).thenReturn(Optional.of(p));

        RefundRequest req = new RefundRequest();
        req.setRefundAmount(new BigDecimal("500.00"));

        assertThrows(InvalidPaymentOperationException.class, () -> {
            paymentService.refundPayment("PAY1", req);
        });
    }
}
