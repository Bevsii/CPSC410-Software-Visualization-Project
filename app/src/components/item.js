import React, { Component } from 'react';
import './item.css';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Divider from '@material-ui/core/Divider';

class Item extends Component {
	constructor(props) {
		super(props);
		this.state = {selected: false};
	}

	onClick = () => {
		this.setState({selected: false})
	}

	render() {
		let parameterString = 'Parameters: '
		if (this.props.params) {
			for (let i = 0; i < this.props.params.length; i++) {
				parameterString = parameterString + '[' + this.props.params[i] + '] '
			}
		}
		return (
			<div className="item">
				<ListItem>
					<ListItemText 
						primary={this.props.called} 
						secondary={'Called By: ' + this.props.caller } />
				</ListItem>
				<ListItem>
					<ListItemText 
						primary={!this.props.params.length ? 'No Parameters' : parameterString} />
				</ListItem>
				<Divider />
			</div>
		)
	}
}
export default Item;