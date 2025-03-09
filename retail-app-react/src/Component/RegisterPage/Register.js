import React, { useEffect, useState } from 'react';
import { Fragment } from "react";
import { Button, Col, Container, Form, FormGroup, Input, Row } from "reactstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import { useNavigate } from 'react-router-dom';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import base_url from '../../Api/BootApi';
import axios from 'axios';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { format } from 'date-fns';

function Register() {
    const navigate = useNavigate();
    const adminName = localStorage.getItem('adminName'); // Retrieve admin name from localStorage

    const [formData, setFormData] = useState({
        name: '',
        address: '',
        dob: null,
        email: '',
        contactNo: '',
        panNumber: '',
        aadhaarNumber: '',
        password: '',
        role: 'CUSTOMER',
        customerStatus: 'ACTIVE',
        createdBy: adminName, // Add createdBy field
        updatedBy: adminName  // Add updatedBy field
    });
    const [errors, setErrors] = useState({});

    const handleChange = (e) => {
        const { id, value } = e.target;
        let newValue = value;
        if (id === 'panNumber') {
            newValue = value.toUpperCase();
        } else if (id === 'aadhaarNumber') {
            newValue = value.replace(/\D/g, ''); // Remove non-numeric characters
        } else if (id === 'contactNo') {
            newValue = value.replace(/\D/g, '').slice(0, 10); // Remove non-numeric characters and limit to 10 digits
        }
        setFormData((prevData) => ({
            ...prevData,
            [id]: newValue
        }));
    };

    const handleClear = () => {
        setFormData({
            name: '',
            address: '',
            dob: null,
            email: '',
            contactNo: '',
            panNumber: '',
            aadhaarNumber: '',
            password: '',
            role: 'CUSTOMER',
            customerStatus: 'ACTIVE',
            createdBy: adminName,
            updatedBy: adminName
        });
        setErrors({});
    };

    const handleDateChange = (date) => {
        setFormData((prevData) => ({
            ...prevData,
            dob: date
        }));
    };

    const validateField = (name, value) => {
        let error;
        switch (name) {
            case 'name':
            case 'address':
            case 'dob':
                case 'password':
                    if (!value) {
                        error = `${name.charAt(0).toUpperCase() + name.slice(1)} is required`;
                    } else if (name === 'password' && !/(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*[0-9]).{8,}/.test(value)) {
                        error = 'Password must contain at least one uppercase letter, one special character, one numerical digit, and be at least 8 characters long';
                    }
                    break;
            case 'email':
                if (!value) {
                    error = 'Email is required';
                } else if (!/\S+@\S+\.\S+/.test(value)) {
                    error = 'Email is invalid';
                }
                break;
            case 'contactNo':
                if (!value) {
                    error = 'Contact No is required';
                } else if (!/^\d{10}$/.test(value)) {
                    error = 'Contact No must be 10 digits';
                }
                break;
            case 'panNumber':
                if (!value) {
                    error = 'Pan Number is required';
                } else if (!/^[A-Z]{5}[0-9]{5}$/.test(value)) {
                    error = 'Pan Number must be 10 characters (5 alphabets, 5 digits)';
                }
                break;
            case 'aadhaarNumber':
                if (!value) {
                    error = 'Aadhaar Number is required';
                } else if (!/^\d{12}$/.test(value)) {
                    error = 'Aadhaar Number must be 12 digits';
                }
                break;
            default:
                break;
        }
        return error;
    };

    const handleBlur = (e) => {
        const { id, value } = e.target;
        const newErrors = { ...errors, [id]: validateField(id, value) };
        setErrors(newErrors);
    };

    const validateForm = () => {
        const newErrors = {};
        Object.keys(formData).forEach((key) => {
            const error = validateField(key, formData[key]);
            if (error) {
                newErrors[key] = error;
            }
        });
        return newErrors;
    };

    const signIn = (e) => {
        e.preventDefault();
        const newErrors = validateForm();
        if (Object.keys(newErrors).length === 0) {
            const formattedData = {
                ...formData,
                dob: formData.dob ? format(formData.dob, 'yyyy-MM-dd') : ''
            };
            axios.post(`${base_url}/customers/register`, formattedData).then(
                (response) => {
                    console.log(response.data);
                    toast.success(response.data.message, {
                        position: "top-right"
                    });
                    handleClear(); 
                },
                (error) => {
                    console.log(error);
                    toast.error("Error! Something went wrong!!", {
                        position: "top-right"
                    });
                }
            )
        } else {
            setErrors(newErrors);
            toast.error('Please fill out all required fields.');
        }
    }

    return (
        <div style={{ padding: '10px' }}>
            <Fragment>
                <ToastContainer />
                <h2>Customer Details</h2>
                <Row>
                    <Form className="text-left-custom" onSubmit={signIn}>
                        <FormGroup>
                            <label htmlFor="name">Full Name:</label>
                            <Input type="text" placeholder="Enter name here" id="name" value={formData.name} onChange={handleChange} onBlur={handleBlur} className={errors.name ? 'is-invalid' : ''}/>
                            {errors.name && <div className='invalid-feedback'>{errors.name}</div>}
                        </FormGroup>
                        <FormGroup>
                            <label htmlFor="address">Address:</label>
                            <Input type="textarea" placeholder="Enter address here" id="address" value={formData.address} onChange={handleChange} onBlur={handleBlur} className={errors.address ? 'is-invalid' : ''} />
                            {errors.address && <div className="invalid-feedback">{errors.address}</div>}
                        </FormGroup>
                        <FormGroup>
                            <label htmlFor="dob">Date of Birth (yyyy-MM-dd):</label>
                            <DatePicker 
                                selected={formData.dob}
                                onChange={handleDateChange}
                                dateFormat="yyyy-MM-dd"
                                className={`form-control ${errors.dob ? 'is-invalid' : ''}`}
                                placeholderText="Enter date of birth here"
                                showYearDropdown
                                yearDropdownItemNumber={100}
                                scrollableYearDropdown
                                wrapperClassName="form-control"
                                maxDate={new Date()} // Prevent future dates
                            />
                            {errors.dob && <div className="invalid-feedback">{errors.dob}</div>}
                        </FormGroup>
                        <FormGroup>
                            <label htmlFor="email">Email:</label>
                            <Input type="text" placeholder="Enter email here" id="email" value={formData.email} onChange={handleChange} onBlur={handleBlur} className={errors.email ? 'is-invalid' : ''} />
                            {errors.email && <div className="invalid-feedback">{errors.email}</div>}
                        </FormGroup>
                        <FormGroup>
                            <label htmlFor="contactNo">Contact No:</label>
                            <Input type="number" placeholder="Enter contact no. here" id="contactNo" value={formData.contactNo} onChange={handleChange} onBlur={handleBlur} className={errors.contactNo ? 'is-invalid' : ''} />
                            {errors.contactNo && <div className="invalid-feedback">{errors.contactNo}</div>}
                        </FormGroup>
                        <FormGroup>
                            <label htmlFor="panNumber">Pan No:</label>
                            <Input type="text" placeholder="Enter pan no. here" id="panNumber" value={formData.panNumber} maxLength="10" onChange={handleChange} onBlur={handleBlur} className={errors.panNumber ? 'is-invalid' : ''}/>
                            {errors.panNumber && <div className="invalid-feedback">{errors.panNumber}</div>}
                        </FormGroup>
                        <FormGroup>
                            <label htmlFor="aadhaarNumber">Aadhaar No:</label>
                            <Input type="text" placeholder="Enter Aadhaar no. here" id="aadhaarNumber" value={formData.aadhaarNumber} maxLength="12" onChange={handleChange} onBlur={handleBlur} className={errors.aadhaarNumber ? 'is-invalid' : ''}/>
                            {errors.aadhaarNumber && <div className="invalid-feedback">{errors.aadhaarNumber}</div>}
                        </FormGroup>
                        <FormGroup>
                            <label htmlFor="password">Password:</label>
                            <Input type="password" placeholder="Enter password here" id="password" value={formData.password} onChange={handleChange} onBlur={handleBlur} className={errors.password ? 'is-invalid' : ''}/>
                            {errors.password && <div className="invalid-feedback">{errors.password}</div>}
                        </FormGroup>
                        <Container className="text-center">
                            <Button type="submit" color="success" style={{ marginRight: '10px' }}>Submit</Button>
                            <Button type="reset" color="warning" onClick={handleClear}>Clear</Button>
                        </Container>
                    </Form>
                </Row>
            </Fragment>
        </div>
    )
}

export default Register;
