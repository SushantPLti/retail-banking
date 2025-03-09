import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import 'bootstrap/dist/css/bootstrap.min.css';

const Customer = () => {
  const [customer, setCustomer] = useState(null);

  useEffect(() => {
    const fetchCustomerData = async () => {
      try {
        const token = localStorage.getItem('bearerToken').replace(/"/g, '');
        if (token) {
          const decodedToken = jwtDecode(token);
          const customerId = decodedToken.custId;

          const response = await axios.get(`http://localhost:7050/customers/customerById/${customerId}`, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          setCustomer(response.data);
        } else {
          console.error('Bearer token not found in local storage.');
        }
      } catch (error) {
        console.error('Error fetching customer data:', error);
      }
    };

    fetchCustomerData();
  }, []);

  if (!customer) {
    return <div>Loading...</div>;
  }

  return (
    <div className="container">
      <h2>Customer Details</h2>
      <table className="table table-striped">
        <tbody>
          <tr>
            <th>Customer ID</th>
            <td>{customer.custId}</td>
          </tr>
          <tr>
            <th>Name</th>
            <td>{customer.name}</td>
          </tr>
          <tr>
            <th>Address</th>
            <td>{customer.address}</td>
          </tr>
          <tr>
            <th>Date of Birth</th>
            <td>{customer.dob}</td>
          </tr>
          <tr>
            <th>Email</th>
            <td>{customer.email}</td>
          </tr>
          <tr>
            <th>Contact No</th>
            <td>{customer.contactNo}</td>
          </tr>
          <tr>
            <th>PAN Number</th>
            <td>{customer.panNumber}</td>
          </tr>
          <tr>
            <th>Aadhaar Number</th>
            <td>{customer.aadhaarNumber}</td>
          </tr>
          <tr>
            <th>Created At</th>
            <td>{customer.createdAt}</td>
          </tr>
          <tr>
            <th>Updated At</th>
            <td>{customer.updatedAt}</td>
          </tr>
          <tr>
            <th>Age Category</th>
            <td>{customer.ageCategory}</td>
          </tr>
          <tr>
            <th>Customer Status</th>
            <td>{customer.customerStatus}</td>
          </tr>
        </tbody>
      </table>
    </div>
  );
};

export default Customer;
