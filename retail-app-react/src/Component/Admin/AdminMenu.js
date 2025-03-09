import react from "react";
import { Link } from "react-router-dom";
import { ListGroup, ListGroupItem } from "reactstrap";
import { useDispatch,useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { signOut } from '../Redux/Slices/userLoginSlice';

const AdminMenus = () => {

    const { bearerToken, loading, error } = useSelector(state => state.user);
    const navigate = useNavigate();
    const dispatch = useDispatch();


    const logout = e => {
        e.preventDefault();
        dispatch(signOut());
        if (!bearerToken) {
            navigate('/');
        }
    }

    return (
        <ListGroup>
            <Link className="list-group-item list-group-item-action" tag="a" to="/admin/home" action>Home</Link>
            <Link className="list-group-item list-group-item-action" tag="a" to="/admin/register" action>Create Customer</Link>
            <Link className="list-group-item list-group-item-action" tag="a" to="/admin/account" action>Create Account</Link>
            <Link className="list-group-item list-group-item-action" tag="a" to="/admin/view-customers" action>View Customers</Link>
            <Link className="list-group-item list-group-item-action" tag="a" to="#" action>Transactions</Link>
            <Link className="list-group-item list-group-item-action" tag="a" to="/" action onClick={logout}>Logout</Link>
        </ListGroup>

    )
}


export default AdminMenus;