import React, { Component } from 'react';
import logo from './serpent.svg';
import './App.css';
import jsonData from './example.json';
import Item from './components/item';
import Classes from './components/classes'
import Method from './components/method'
import List from '@material-ui/core/List';
import ListSubheader from '@material-ui/core/ListSubheader';



class App extends Component {

  constructor(props) {
    super(props)
    this.state = {methodsToDisplay: [], currentFilter: '', dynamicData: jsonData.dynamic}
    this.classHandler = this.classHandler.bind(this)
    this.methodHandler = this.methodHandler.bind(this)
  }

  classHandler = (methods) => {
    this.setState({methodsToDisplay: methods, currentFilter: ''})
  }

  methodHandler = (method) => {
    this.setState({currentFilter: method})
  }

  makeClass(c) {
    let name = Object.keys(c)[0];
    let methods = c[name];
    return <Classes name={name} methods={methods} handler={this.classHandler}/>
  }

  makeMethod(m) {
    return <Method name={m} handler={this.methodHandler}/>
  }

  makeItems() {
    let filtered = this.state.dynamicData.filter(i => (i.caller+i.called).includes(this.state.currentFilter))
    let items = filtered.map( i => <Item caller={i.caller} called={i.called} params={i.params} /> )
    return items;
  }

  render() {
    console.log(jsonData.dynamic)
    let s = jsonData.static;
    let classList = s.map(c => this.makeClass(c))
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          PythonExplorer - Click on a class to get started!
        </header>
        <div className="App-body">
          <div className="classes">
            <List subheader={<ListSubheader>Classes</ListSubheader>} >
              {classList}
            </List>
          </div>
          <div className="divider"/>
          <div className="methods">
            <List subheader={<ListSubheader>Methods</ListSubheader>} >
              {!this.state.methodsToDisplay ? <div/> : this.state.methodsToDisplay.map(m => this.makeMethod(m))}
            </List>
          </div>
          <div className="divider"/>
          <div className="items">
            <List subheader={<ListSubheader>Usage</ListSubheader>} >
              {!this.state.currentFilter ? <div/> : this.makeItems()}
            </List>
          </div>
        </div>
      </div>
    );
  }
}

export default App;
