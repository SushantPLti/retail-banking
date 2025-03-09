import './App.css';
import DashBoard from './Component/DashBoard/DashBoard';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './Component/LoginPage/Login';
import Register from './Component/RegisterPage/Register';
import Profile from './Component/Profile/Profile';
import Account from './Component/Account/Account';
import AdminDashBoard from './Component/Admin/AdminDashboard';
import AdminHome from './Component/Admin/AdminHome';
import GetCustomers from './Component/Admin/GetCustomer';
import Title from './Component/SubComponents/Title';

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/dashboard" element={<DashBoard />} />
          <Route path="/" element={<Login />} />
          <Route path='/admin/*' element={<AdminDashBoard />} />
          <Route path="/home" element={<AdminHome />} />
          <Route path="/register" element={<Register />} />
          <Route path="/account" element={<Account />} />
          <Route path="/profile" element={<Profile />} />
          {/* <Route path="/view-customers" element={<GetCustomers />} /> */}
        </Routes>
      </div>
    </Router>
  );
}

export default App;