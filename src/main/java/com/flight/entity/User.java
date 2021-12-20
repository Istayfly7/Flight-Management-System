package com.flight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name="Passengers")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
/*@JsonSubTypes({@JsonSubTypes.Type(value = CustomerUser.class, name = "CUSTOMER"),
        @JsonSubTypes.Type(value = EmployeeUser.class, name = "EMPLOYEE")
})*/
public abstract class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int passenger_id;
	
	@Column(nullable=false)
	private String first_name;
	
	private String second_name;
	
	@Column(nullable=false)
	private String last_name;
	
	@Column(nullable=true)
	private int phone_number = 000-000-0000; //int range (-2,147,483,648 to 2,147,483,647)
	
	@Column(nullable=false)
	private String email_address;
	
	@Column(nullable=false)
	private String address_lines;
	
	@Column(nullable=false)
	private String city;
	
	@Column(nullable=false)
	private String state_province_county;
	
	@Column(nullable=false)
	private String country;
	
	private String other_passenger_details;
	
	/*@Column(insertable=false, updatable=false)
	private String type;*/
	
	public abstract String getType();
	
}
