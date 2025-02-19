import './App.css';

import React, {Component} from "react";
import Generator from "./components/Generator/Generator";
import {Button, Card, ToggleButton, ToggleButtonGroup} from "@mui/material";
import GitHubIcon from '@mui/icons-material/GitHub';
import DescriptionIcon from '@mui/icons-material/Description';
import GroupIcon from '@mui/icons-material/Group';
import Adapter from "./components/Adapter/Adapter";
// https://create-react-app.dev/docs/adding-a-stylesheet
// https://mui.com/material-ui/getting-started/usage/


class App extends Component {

    state = {option: 'generator'}

    render() {
        return (
            <div className={App}>
                <Card className='CardGenerate Header' style={{'padding': '20px'}}>
                    <img src="https://isearchai.github.io/logo/logo.png" alt="" height='48'/>
                    <div>
                        <Button variant="outlined" onClick={() => window.open('https://github.com/isearchai/isearchai','_blank')} startIcon={<GitHubIcon/>}>Repository</Button>
                        <Button variant="outlined" onClick={() => window.open('https://github.com/isearchai/isearchai','_blank')} startIcon={<DescriptionIcon/>}>Thesis</Button>
                        <Button variant="outlined" onClick={() => window.open('https://otimizes.github.io' +
                            '','_blank')} startIcon={<GroupIcon/>}>Team</Button>
                    </div>
                </Card>
                <Card className='CardGenerate'>

                    <ToggleButtonGroup
                        color="primary"
                        className='Option'
                        value={this.state.option}
                        name='alignment'
                        exclusive
                        onChange={event => this.setState({option: event.target.value})}
                        aria-label="Platform"
                    >
                        <ToggleButton value="generator">Generate</ToggleButton>
                        <ToggleButton value="adapter">Adapt</ToggleButton>
                    </ToggleButtonGroup>

                    {this.state.option === 'generator' ? <Generator/> : <Adapter/>}
                </Card>
            </div>
        );
    }
}

export default App;
