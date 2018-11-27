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
			<Button fullWidth={true} onClick={() => this.props.handler(this.props.name)}>
				{this.props.name}
			</Button>
		)
	}
}
export default Method;