import React, { Component } from 'react';
import logo from './serpent.svg';
import './App.css';
import jsonData from './example.json';
import Item from './components/item';
import Classes from './components/classes'
import Method from './components/method'
import List from '@material-ui/core/List';
import ListSubheader from '@material-ui/core/ListSubheader';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Divider from '@material-ui/core/Divider';


class App extends Component {

  constructor(props) {
    super(props)
    this.state = {methodsToDisplay: [], currentFilter: '', dynamicData: jsonData.dynamic, currentClass: ''}
    this.classHandler = this.classHandler.bind(this)
    this.methodHandler = this.methodHandler.bind(this)
    this.itemHandler = this.itemHandler.bind(this)
  }

  classHandler = (methods, name) => {
    this.setState({methodsToDisplay: methods, currentFilter: '', currentClass: name})
  }

  methodHandler = (method) => {
    this.setState({currentFilter: method})
  }

  itemHandler = (calledBy) => {
    let c = calledBy.split('.')[0]
    let method = calledBy.split('.')[1]
    let mToDisplay = []
    let s = jsonData.static;
    for (let i = 0; i < s.length; i++) {
      let name = Object.keys(s[i])[0]
      if (name == c) {
        mToDisplay = s[i][name]
      }
    }
    this.setState({currentClass: c, currentFilter: method, methodsToDisplay: mToDisplay})
  }

  makeClass(c) {
    let name = Object.keys(c)[0];
    let methods = c[name];
    return (
    <div className="classItem">
      <Classes selected={(this.state.currentClass == name)} name={name} methods={methods} handler={this.classHandler}/>
    </div>)
  }

  makeMethod(m) {
    return <Method name={m} selected={(this.state.currentFilter == m)} handler={this.methodHandler}/>
  }

  makeItems() {
    let filtered = this.state.dynamicData.filter(i => i.called.includes(this.state.currentFilter))
    let items = filtered.map( i => <Item caller={i.caller} called={i.called} params={i.params} handler={this.itemHandler} /> )
    return items;
  }

  render() {
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
            <List subheader={<ListSubheader style={{fontSize: '25px'}}>Classes</ListSubheader>} >
              <Divider />
              {classList}
            </List>
          </div>
          <div className="divider"/>
          <div className="methods">
            <List subheader={<ListSubheader style={{fontSize: '25px'}}>Methods</ListSubheader>} >
              <Divider />
              {!this.state.methodsToDisplay ? <div/> : this.state.methodsToDisplay.map(m => this.makeMethod(m))}
            </List>
          </div>
          <div className="divider"/>
          <div className="items">
            <List subheader={<ListSubheader style={{fontSize: '25px'}}>Usage</ListSubheader>} >
              <Divider />
              {!this.state.currentFilter ? <div/> : this.makeItems()}
            </List>
          </div>
        </div>
      </div>
    );
  }
}

export default App;
