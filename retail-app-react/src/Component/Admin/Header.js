import React from "react";
import { Card, CardBody } from "reactstrap";

function Header(name, title){
    return (
        <div>
            <Card className="my-2" style={{ backgroundColor: '#db0011' }}>
                <CardBody>
                <h1 className="text-center my-2" style={{ color: 'white' }}>Welcome to Retail Banking</h1>
                </CardBody>
            </Card>
            
        </div>
    )
}

export default Header;


// function Header({ name, title }){
//     return (
//         <div style={ {background:'yellow', padding:20, width:400} }>
//             <h1>Header</h1>
//             <h1>{ title }</h1>
//             <h1>{ name }</h1>
//             <p>This is header for learning purpose</p>

//         </div>
//     )
// }