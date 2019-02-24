import React, { Component } from 'react';
import QRCode from 'qrcode.react';
import './App.css';
import {InputGroup, FormControl, Row, Col, Button, ToggleButtonGroup, ButtonGroup, ToggleButton} from 'react-bootstrap';
// import axios from 'axios';
import $ from 'jquery';
// const userLogin = {
//   email: "ivigheni@uni.coventry.ac.uk",
//   password: "password"
// }
// const qrcodeInput = JSON.stringify(userLogin);
class App extends Component {
  state = {
    color:'#ffff',
    userLogin:{
      email:'',
      password:'',
    },
    studentID:'',
    something:'',
    qrvalue:'',
    error:'',
  }
  generateQr = () => {

    
    $.ajax({
      method:'GET',
      url:`https://ziemni.me/rwp/hasher.php?sid=${this.state.studentID}`,
      headers: {'Access-Control-Allow-Origin': '*'},
      crossDomain:true,
      dataType: 'json',
      success: (res) => {
        this.setState({qrvalue:res.hash});
      }
    })
    // `https://ziemni.me/rwp/hasher.php?sid=${this.state.studentID}`
    // const value = JSON.stringify(this.state.userLogin);
    // this.setState({qrvalue:value})
  }

  makeItBlueBaby = () => {
    this.setState({
      color:'#0000ff'
    })
  }
  makeItRedBaby = () => {
    this.setState({
      color:'#ee3f04'
    })
  }
  makeItStandardBaby = () => {
    this.setState({
      color:'#ffff'
    })
  }
  makeItGreenBaby = () => {
    this.setState({
      color:'#008000'
    })
  }
  render() {
    return (
      <div className="App">
        <h3>Alternative Login System</h3>
        <Row>
          <Col></Col>
        <Col xs={12} sm={8} md={8}>
        <InputGroup className="mb-3">
          <FormControl placeholder="Student ID" onChange={event => this.setState({studentID:event.target.value}) }/>
          {/* <FormControl placeholder="Password" type="password" onChange={event=> this.setState({userLogin:{...this.state.userLogin,password:event.target.value}})} /> */}
          <InputGroup.Append>
          <Button variant="outline-primary" onClick={this.generateQr}>Update QR code</Button>
          </InputGroup.Append>
          </InputGroup>
          </Col>
          <Col></Col>

        </Row>
        <Row>
          <Col></Col>
          <Col>
          <QRCode value={this.state.qrvalue} bgColor={this.state.color} size='80' /> 
          </Col>
          <Col></Col>
        </Row>
        <Row>
          <Col></Col>
          <Col>
          <ButtonGroup toggle className="mt-3">
          <ToggleButtonGroup type="radio" name="options" defaultValue="Standard">
            <ToggleButton type="radio" name="radio" value={1} onClick={this.makeItGreenBaby}>
              Green
            </ToggleButton>
            <ToggleButton type="radio" name="radio" value="Blue"  onClick={this.makeItBlueBaby}>
              Blue
            </ToggleButton>
            <ToggleButton type="radio" name="radio" value="Red" onClick={this.makeItRedBaby}>
              Red
            </ToggleButton>
            <ToggleButton type="radio" name="radio" value="Standard" onClick={this.makeItStandardBaby}>
              Standard
            </ToggleButton>
            </ToggleButtonGroup>
          </ButtonGroup>
          </Col>
          <Col></Col>
        </Row>
        <p>
          This QR code contains student ID's hash
        </p>
      <p>{this.state.userLogin.email}</p>
      <p>{this.state.userLogin.password}</p>
      <p>{this.state.qrvalue}</p>
      <p>{this.state.error}</p>
      </div>
    );
  }
}
export default App;
