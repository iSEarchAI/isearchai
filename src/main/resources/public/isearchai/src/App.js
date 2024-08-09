import './App.css';

import React, {Component} from "react";
import Generator from "./components/Generator/Generator";
import {Button, Card} from "@mui/material";
import GitHubIcon from '@mui/icons-material/GitHub';
import DescriptionIcon from '@mui/icons-material/Description';
import GroupIcon from '@mui/icons-material/Group';
// https://create-react-app.dev/docs/adding-a-stylesheet
// https://mui.com/material-ui/getting-started/usage/


class App extends Component {

    render() {
        return (
            <div className={App}>
                <Card className='CardGenerate Header' style={{'padding':'20px'}}>
                    <img src="https://isearchai.github.io/logo/logo.png" alt="" height='48'/>
                    <div>
                        <Button variant="outlined" startIcon={<GitHubIcon/>}>Repository</Button>
                        <Button variant="outlined" startIcon={<DescriptionIcon/>}>Thesis</Button>
                        <Button variant="outlined" startIcon={<GroupIcon/>}>Team</Button>
                    </div>
                </Card>
                <Card className='CardGenerate'>
                    <Generator/>
                </Card>
            </div>
        );
    }
}

export default App;
