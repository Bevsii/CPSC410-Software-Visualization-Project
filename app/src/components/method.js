import React, { Component } from 'react';
import './method.css';
import Button from '@material-ui/core/Button';

class Method extends Component {
	constructor(props) {
		super(props);
		this.state = {selected: false};
	}

	onClick = () => {
		this.setState({selected: false})
	}

	render() {
		return (
			<div>
				<Button fullWidth={true}
					style={this.props.selected ? {color: '#9fa6ad', backgroundColor: '#34404c'} : {}}
					classes={{root: 'button'}}
					onClick={() => this.props.handler(this.props.name)}>
					{this.props.name}
				</Button>
				<div style={{height: 5}} />
			</div>
		)
	}
}
export default Method;