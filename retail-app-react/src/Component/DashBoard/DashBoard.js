import React from 'react'
import NavBar from '../SubComponents/NavBar'
import Transaction from '../SubComponents/Transaction'
import Debit from '../SubComponents/Debit'
import TotalAmount from '../SubComponents/TotalAmount'
import Transfer from '../SubComponents/Transfer'
import Credit from '../SubComponents/Credit'
import { CDCommonDiv, MidDiv } from '../../Styles/DebitCreditStyle'
import { DashBoardContainerStyle } from '../../Styles/DashBoardContainer'

function DashBoard() {
  return (
    <DashBoardContainerStyle>
        {/* <TitleBar/> */}
        <NavBar/>
        <MidDiv>
          <CDCommonDiv>
            <Debit/>
            <Credit/>
          </CDCommonDiv>
          <CDCommonDiv>
            <TotalAmount/>
          </CDCommonDiv>
        </MidDiv>
        <MidDiv>
          <Transfer/>
          <Transaction/>
        </MidDiv>
    </DashBoardContainerStyle>
  )
}

export default DashBoard