import React from 'react'
import NavBar from '../SubComponents/NavBar'
import Transaction from '../SubComponents/Transaction'
import Account from '../Account/Account'
import Register from '../RegisterPage/Register'
import AdminHome from './AdminHome'
import AdminMenus from './AdminMenu'
import { Route, Router, Routes, useNavigate, Switch } from 'react-router-dom'
import { ToastContainer } from 'react-toastify'
import { Col, Container, Row } from 'reactstrap'
import Header from './Header'
import GetCustomers from './GetCustomer'

function AdminDashBoard() {

  const navigate = useNavigate();

  return (
    <div>
    {/* <Router>  */}
        <ToastContainer />
        <Container>
          <Header />
          <Row>
            <Col md={4}>
              <AdminMenus />
            </Col>
            <Col md={8}>
            
                <Routes>
                <Route path="home" Component={AdminHome} exact />
                <Route path="register" Component={Register} exact />
                <Route path="account" Component={Account} exact />
                <Route path="view-customers" Component={GetCustomers} exact />
              </Routes>
            </Col>
          </Row>
        </Container>
       {/* </Router>  */}
    </div>
  )
}

export default AdminDashBoard