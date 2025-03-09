import React, { useEffect, useState } from 'react'
import NavBar from '../SubComponents/NavBar'
import Transaction from '../SubComponents/Transaction'
import Account from '../Account/Account'
import { jwtDecode } from 'jwt-decode';
import { ToastContainer } from 'react-toastify';
import axios from 'axios';

function AdminHome() {

  const [customerTitle, setCustomerTitle] = useState('Welcome'); // State for the title

  const getCurrentGreeting = () => {
    const currentHour = new Date().getHours();
    if (currentHour < 12) {
      return 'Good Morning';
    } else if (currentHour < 18) {
      return 'Good Afternoon';
    } else {
      return 'Good Evening';
    }
  };

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
          
          const customerName = response.data.name;
          console.log('Customer Name:', customerName);
          // Set the title with customer name
          setCustomerTitle(`Hello, ${customerName}`);
          localStorage.setItem('adminName', customerName);

        } else {
          console.error('Bearer token not found in local storage.');
        }
      } catch (error) {
        console.error('Error fetching customer data:', error);
      }
    };

    fetchCustomerData();
  }, []);



  return (
    <div style={{ padding: '10px' }}>
      <ToastContainer />
      <h2>{customerTitle}</h2>
      <br/>
      <h3>{getCurrentGreeting()}</h3>
    </div>
  )
}

export default AdminHome