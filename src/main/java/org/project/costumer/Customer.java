package org.project.costumer;

public class Customer {
    String fileName;
    String customerid;
    String firstname;
    String lastname;
    String company;
    String city;
    String country;
    String phone1;
    String phone2;
    String email;
    String subscriptiondate;
    String website;

    public Customer(String fileName, String customerid, String firstname, String lastname, String company,
                    String city, String country, String phone1, String phone2, String email, String subscriptiondate, String website) {
        this.fileName = fileName;
        this.customerid = customerid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.company = company;
        this.city = city;
        this.country = country;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.email = email;
        this.subscriptiondate = subscriptiondate;
        this.website = website;
    }

    public String getFileName(){
        return this.fileName;
    }

    public String getCustomerid(){
        return this.customerid;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "fileName=" + fileName +
                ", customerid='" + customerid + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", company='" + company + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", phone1='" + phone1 + '\'' +
                ", phone2='" + phone2 + '\'' +
                ", email='" + email + '\'' +
                ", subscriptiondate='" + subscriptiondate + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}