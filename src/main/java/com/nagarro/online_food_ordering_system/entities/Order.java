package com.nagarro.online_food_ordering_system.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_order")
public class Order {

	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne
	    @JoinColumn(name = "user_id")
	    private User user;

	    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<OrderItem> orderItems = new ArrayList<>();

	    @Column(name="total_amount")
	    private double totalAmount;
	    
	    @Column(name="order_number")
	    private String orderNumber; 

	    @Column(name="order_date")
	    private Date orderDate; 

	    @Column(name="delivery_address")
	    private String deliveryAddress;  

	    @Column(name="delivery_status")
	    private String deliveryStatus;  
	    
	    @Column(name="order_status")
	    private String orderStatus; 
	    
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
	
		public List<OrderItem> getOrderItems() {
			return orderItems;
		}
		public void setOrderItems(List<OrderItem> orderItems) {
			this.orderItems = orderItems;
		}
		public double getTotalAmount() {
			return totalAmount;
		}
		public void setTotalAmount(double totalAmount) {
			this.totalAmount = totalAmount;
		}
		
		 public String getOrderNumber() {
			return orderNumber;
		}
		public void setOrderNumber(String orderNumber) {
			this.orderNumber = orderNumber;
		}
		public Date getOrderDate() {
			return orderDate;
		}
		public void setOrderDate(Date orderDate) {
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
		
		
		public String getOrderStatus() {
			return orderStatus;
		}
		public void setOrderStatus(String orderStatus) {
			this.orderStatus = orderStatus;
		}
		public void addOrderItem(OrderItem orderItem) {
		        orderItems.add(orderItem);
		        orderItem.setOrder(this);
		    }

		    public void removeOrderItem(OrderItem orderItem) {
		        orderItems.remove(orderItem);
		        orderItem.setOrder(null);
		    }
		    
	    
}
