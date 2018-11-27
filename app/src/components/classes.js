import React, { Component } from 'react';
import './classes.css';
import Button from '@material-ui/core/Button';

class Classes extends Component {
	constructor(props) {
		super(props);
	}

	render() {
		return (
			<Button fullWidth={true} onClick={() => this.props.handler(this.props.methods)}>
				{this.props.name}
			</Button>
		)
	}
}
export default Classes;