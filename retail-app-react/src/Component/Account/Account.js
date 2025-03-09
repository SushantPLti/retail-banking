import React, { useEffect, useState } from 'react';
import react, { Fragment } from "react";
import { Button, Col, Container, Form, FormGroup, Input, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';

// import { useNavigate } from 'react-router-dom';
// import {useDispatch, useSelector} from 'react-redux';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import base_url from '../../Api/BootApi';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';


function Account() {
    // Retrieve an item from localStorage
    const adminName = localStorage.getItem('adminName');

    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        accountType: '',
        balance: '',
        currency: '',
        status: 'ACTIVE',
        custId: '',
        createdBy: adminName,
        updatedBy: adminName
    });
    // const {custId,loading, error} = useSelector(state => state.Customer);


    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.id]: e.target.value
        });
    };

    const handleClear = () => {
        setFormData({
            accountType: '',
            balance: '',
            currency: '',
            status: 'ACTIVE',
            custId: '',
            createdBy: '', 
            updatedBy: ''
        });
    };

    // sending data to server
    const createAccount = (e) => {
        e.preventDefault();
        const token = localStorage.getItem('bearerToken').replace(/"/g, '');
        // API call
        axios.post(`${base_url}/accounts`, formData, {
            headers: {
                Authorization: `Bearer ${token}`,
              },
        }).then(
            (response) => {
                console.log(response.data);
                toast.success(response.data.message, {
                    position: "top-right"
                });
                handleClear(); // Clear the form after successful submission
                // Delay the navigation to let the toast message appear
                // setTimeout(() => {
                //     navigate('/login');
                // }, 4000); // 4 seconds delay

            },
            (error) => {
                console.log(error.message);
                toast.error(error.response.data.message, {
                    position: "top-right"
                });
            }
        )
    }


    return (
        <div style={{ padding: '10px' }}>
            <Fragment>
                <ToastContainer />
                <h2> Account Details</h2>
                <Row>

                    <Form className="text-left-custom" onSubmit={createAccount}>
                        <FormGroup>
                            <label htmlFor="custId">Customer Id:</label>
                            <Input type="number" placeholder="Enter customer id here" id="custId" value={formData.custId} onChange={handleChange} />
                        </FormGroup>
                        <FormGroup>
                            <label htmlFor="accountType">Account Type:</label>
                            <Input type="select" id="accountType" value={formData.accountType} onChange={handleChange}>
                                <option value="">Select Account Type</option>
                                <option value="CURRENT">CURRENT</option>
                                <option value="SAVINGS">SAVINGS</option>
                                <option value="SALARY">SALARY</option>
                            </Input>
                        </FormGroup>
                        <FormGroup>
                            <label htmlFor="balance">Balance:</label>
                            <Input type="number" placeholder="Enter opening balance here" id="balance" value={formData.balance} onChange={handleChange} />
                        </FormGroup>
                        <FormGroup>
                            <label htmlFor="currency">Currency:</label>
                            <Input type="select" id="currency" value={formData.currency} onChange={handleChange}>
                                <option value="">Select Currency</option>
                                <option value="INR">INR</option>
                            </Input>
                        </FormGroup>

                        <Container className="text-center">
                            <Button type="submit" color="success" style={{ marginRight: '10px' }}>Submit</Button>
                            <Button type="reset" color="warning">Clear</Button>
                        </Container>
                    </Form>

                </Row>
            </Fragment>

        </div>
    )
}

export default Account