import React, { useState, useEffect } from 'react';
import DataTable from 'react-data-table-component';
import 'bootstrap/dist/css/bootstrap.min.css';

const Customers = () => {
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    const token = localStorage.getItem('bearerToken').replace(/"/g, '');
    if (token) {
      const eventSource = new EventSource(`http://localhost:8000/customers?token=${token}`);

      eventSource.onmessage = function(event) {
        const newCustomer = JSON.parse(event.data);
        setCustomers(prevCustomers => [...prevCustomers, newCustomer]);
        setLoading(false); // Set loading to false once data starts loading
      };

      eventSource.onerror = function(error) {
        console.error('Error with event source:', error);
        if (error.readyState === EventSource.CLOSED || error.status === 401) {
          window.location.href = 'http://localhost:3000/';
        }
        eventSource.close();
      };

      return () => {
        eventSource.close();
      };
    } else {
      console.error('Bearer token not found in local storage.');
      window.location.href = 'http://localhost:3000/';
    }
  }, []);

  const columns = [
    { name: 'Customer ID', selector: row => row.custId, sortable: true },
    { name: 'Name', selector: row => row.name, sortable: true },
    { name: 'Address', selector: row => row.address, sortable: true },
    { name: 'Date of Birth', selector: row => row.dob, sortable: true },
    { name: 'Email', selector: row => row.email, sortable: true },
    { name: 'Contact No', selector: row => row.contactNo, sortable: true },
    { name: 'PAN Number', selector: row => row.panNumber, sortable: true },
    { name: 'Aadhaar Number', selector: row => row.aadhaarNumber, sortable: true },
    { name: 'Created At', selector: row => row.createdAt, sortable: true },
    { name: 'Updated At', selector: row => row.updatedAt, sortable: true },
    { name: 'Age Category', selector: row => row.ageCategory, sortable: true },
    { name: 'Customer Status', selector: row => row.customerStatus, sortable: true }
  ];

  const filteredCustomers = customers.filter(customer =>
    customer.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    customer.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
    String(customer.contactNo).toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div className="container">
      <h2>All Customer Details</h2>
      <input 
        type="text" 
        placeholder="Search..." 
        value={searchTerm}
        onChange={e => setSearchTerm(e.target.value)}
        className="form-control mb-3"
      />
      <DataTable
        columns={columns}
        data={filteredCustomers}
        pagination
        highlightOnHover
        striped
      />
    </div>
  );
};

export default Customers;
