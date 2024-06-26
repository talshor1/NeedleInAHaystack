package org.project.customer;

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

    public Customer(String fileName, String[] data) {
        this.fileName = fileName;
        this.customerid = data[1];
        this.firstname = data[2];
        this.lastname = data[3];
        this.company = data[4];
        this.city = data[5];
        this.country = data[6];
        this.phone1 = data[7];
        this.phone2 = data[8];
        this.email = data[9];
        this.subscriptiondate = data[10];
        this.website = data[11];
    }

    public String getFileName(){
        return this.fileName;
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