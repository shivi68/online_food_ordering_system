package com.nagarro.online_food_ordering_system.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class OrderDto {

		@JsonIgnore
		private Long id; 
    	
		private Long orderId; 
	    private String orderNumber;  
	    private String orderDate; 

	    private Long customerId;
	    
	    private String customerName;
	    
	    private double totalAmount;	      
	    
	    private String deliveryAddress;  
	    
	    private String deliveryStatus; 
	    
	    private String orderStatus;

	    private List<OrderItemDto> items;
	    
		public Long getId() {
			return id;
		}
		
		public void setId(Long id) {
			this.id = id;
		}
		
		public Long getCustomerId() {
			return customerId;
		}
		
		public void setCustomerId(Long customerId) {
			this.customerId = customerId;
		}
		
		public List<OrderItemDto> getItems() {
			return items;
		}
		
		public void setItems(List<OrderItemDto> items) {
			this.items = items;
		}
		
		public double getTotalAmount() {
			return totalAmount;
		}
		
		public void setTotalAmount(double totalAmount) {
			this.totalAmount = totalAmount;
		}
		
		public String getCustomerName() {
			return customerName;
		}
		
		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}
		
		public String getOrderNumber() {
			return orderNumber;
		}
		
		public void setOrderNumber(String orderNumber) {
			this.orderNumber = orderNumber;
		}
		
		public String getOrderDate() {
			return orderDate;
		}
		
		public void setOrderDate(String orderDate) {
			this.orderDate = orderDate;
		}
		
		public String getDeliveryAddress() {
			return deliveryAddress;
		}
		
		public void setDeliveryAddress(String deliveryAddress) {
			this.deliveryAddress = deliveryAddress;
		}
		
		public String getDeliveryStatus() {
			return deliveryStatus;
		}
		
		public void setDeliveryStatus(String deliveryStatus) {
			this.deliveryStatus = deliveryStatus;
		}
		
		public Long getOrderId() {
			return orderId;
		}
		
		public void setOrderId(Long orderId) {
			this.orderId = orderId;
		}
		
		public String getOrderStatus() {
			return orderStatus;
		}
		
		public void setOrderStatus(String orderStatus) {
			this.orderStatus = orderStatus;
		}
	    
		
	    
	
}
