package com.ordermodule.springboot.controller;

import com.ordermodule.domain.order.OrderCommand;
import com.ordermodule.domain.order.OrderCommand.OrderCreateCommand;
import com.ordermodule.domain.order.OrderCommand.OrderUpdateCommand;
import com.ordermodule.springboot.dto.OrderViewModel;
import com.ordermodule.springboot.facade.OrderFacade;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderFacade orderFacade;

    @GetMapping("/getOrders")
    public ResponseEntity<List<OrderViewModel>> getOrders() {
        List<OrderViewModel> orders = orderFacade.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/getOrder/{orderId}")
    public ResponseEntity<OrderViewModel> getOrder(@PathVariable Long orderId) {
        OrderViewModel order = orderFacade.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/orderProduct")
    public ResponseEntity<OrderViewModel> orderProduct(@RequestBody OrderCreateCommand command) {
        OrderViewModel newOrder = orderFacade.placeOrder(command);
        return ResponseEntity.ok(newOrder);
    }

    @PutMapping("/changeOrder/{orderId}")
    public ResponseEntity<OrderViewModel> changeOrder(@PathVariable Long orderId, @RequestBody OrderUpdateCommand command) {
        OrderViewModel updatedOrder = orderFacade.changeOrder(orderId, command);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/deleteOrder/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderFacade.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}