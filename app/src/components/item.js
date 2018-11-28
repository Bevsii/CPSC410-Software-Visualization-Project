import React, { Component } from 'react';
import './item.css';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Divider from '@material-ui/core/Divider';
import Button from '@material-ui/core/Button';


class Item extends Component {
	constructor(props) {
		super(props);
		this.state = {selected: false};
	}

	onClick = () => {
		this.setState({selected: false})
	}

	render() {
		let parameterString = 'Arguments: '
		if (this.props.params) {
			for (let i = 0; i < this.props.params.length; i++) {
				parameterString = parameterString + '[' + this.props.params[i] + '] '
			}
		}
		return (
			<div>
				<ListItem>
					<ListItemText 
						primary={this.props.called} 
						secondary={'Called By: ' + this.props.caller } />
					<Button variant="outlined" onClick={() => this.props.handler(this.props.caller)}>
						{'Go to: ' + this.props.caller}
					</Button>
				</ListItem>
				<ListItem>
					<ListItemText 
						secondary={!this.props.params.length ? 'No Arguments' : parameterString} />
				</ListItem>
				<Divider />
			</div>
		)
	}
}
export default Item;