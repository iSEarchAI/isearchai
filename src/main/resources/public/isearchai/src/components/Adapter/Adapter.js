import React, {Component} from 'react';
import styles from './Adapter.module.css';
import {
    Autocomplete,
    Box,
    Button,
    CircularProgress,
    Paper,
    Step,
    StepButton,
    StepContent,
    Stepper,
    styled,
    TextField,
    Typography
} from "@mui/material";
import NavigateNextIcon from '@mui/icons-material/NavigateNext';

import CloudUploadIcon from '@mui/icons-material/CloudUpload'

const VisuallyHiddenInput = styled('input')({
    clip: 'rect(0 0 0 0)',
    clipPath: 'inset(50%)',
    height: 1,
    overflow: 'hidden',
    position: 'absolute',
    bottom: 0,
    left: 0,
    whiteSpace: 'nowrap',
    width: 1,
})

const steps = [
    {
        label: 'Instructions',
        description: <b>Welcome to the ISEarchAI Adapter. Firsly, read the instructions provided below.</b>,
        jsx: (ctx) => (
            <div>
                <ul>
                    <li>This interface allows you to adapt a Java Project using ISearchAI.</li>
                    <li>For that, you need to upload your zip java project and follow the steps.</li>
                </ul>
            </div>
        ),
        onContinue: (ctx) => {
            console.log("onContinueee", ctx)
        }
    },
    {
        label: 'Java Project',
        description: `Upload your java project compacted with the extension .zip.`,
        jsx: (ctx) => (
            <div>
                {/*{ctx.state.generate.solution.name}*/}
                {/*<TextField id="outlined-basic" label="Solution Name" variant="outlined"*/}
                {/*           name='generate.solution.name'*/}
                {/*           InputProps={{*/}
                {/*               endAdornment: (<InputAdornment position={"end"}><Tooltip*/}
                {/*                   title="Put a name without spaces and uppercase at beginning"><InfoIcon/></Tooltip></InputAdornment>)*/}
                {/*           }}*/}
                {/*           value={ctx.state.generate.solution.name} onChange={ctx.updateState}/>*/}
                <Button
                    component="label"
                    role={undefined}
                    disabled={ctx.state.uploaded}
                    variant="contained"
                    tabIndex={-1}
                    startIcon={<CloudUploadIcon/>}
                >
                    Upload file
                    <VisuallyHiddenInput type="file" onChange={ctx.handleFileUpload}/>
                </Button>
            </div>
        ),
        onContinue: (ctx) => {
            console.log("onContinueee", ctx)
        }
    },
    {
        label: 'Classes to adapt',
        description:
            'Select the classes of your project to adapter. They are: an Element representation, a Search Solution, the Solution Set and finally the Search Algorithm.',
        jsx: (ctx) => (
            <>
                {/*{ctx.state.generate.solution.name}*/}
                <Autocomplete
                    disablePortal
                    options={ctx.files}
                    value={ctx.state.adapter.elementClazz}
                    sx={{width: '100%'}}
                    onChange={(event, value) => ctx.updateStateAt('adapter.elementClazz', value)}
                    renderInput={(params) => <TextField {...params} label="Element Class"/>}
                />
                <br/>
                <Autocomplete
                    disablePortal
                    options={ctx.files}
                    value={ctx.state.adapter.solutionClazz}
                    sx={{width: '100%'}}
                    onChange={(event, value) => ctx.updateStateAt('adapter.solutionClazz', value)}
                    renderInput={(params) => <TextField {...params} label="Solution Class"/>}
                />
                <br/>
                <Autocomplete
                    disablePortal
                    options={ctx.files}
                    value={ctx.state.adapter.solutionSetClazz}
                    sx={{width: '100%'}}
                    onChange={(event, value) => ctx.updateStateAt('adapter.solutionSetClazz', value)}
                    renderInput={(params) => <TextField {...params} label="Solution Set Class"/>}
                />
                <br/>
                <Autocomplete
                    disablePortal
                    options={ctx.files}
                    value={ctx.state.adapter.searchAlgorithmClazz}
                    sx={{width: '100%'}}
                    onChange={(event, value) => ctx.updateStateAt('adapter.searchAlgorithmClazz', value)}
                    renderInput={(params) => <TextField {...params} label="Search Algorithm Class"/>}
                />
            </>
        ),
        onContinue: (ctx) => {
            console.log("onContinueee", ctx)
        }
    }
];

class Adapter extends Component {

    state = {
        adapter: {
            elementClazz: 'generated/adapt/OPLA-Tool/modules/architecture-representation/src/main/java/br/otimizes/oplatool/architecture/representation/Element.java',
            solutionClazz: 'generated/adapt/OPLA-Tool/modules/opla-core/src/main/java/br/otimizes/oplatool/core/jmetal4/core/Solution.java',
            solutionSetClazz: 'generated/adapt/OPLA-Tool/modules/opla-core/src/main/java/br/otimizes/oplatool/core/jmetal4/core/SolutionSet.java',
            searchAlgorithmClazz: 'generated/adapt/OPLA-Tool/modules/opla-core/src/main/java/br/otimizes/oplatool/core/jmetal4/metaheuristics/nsgaII/NSGAII.java'
        },
        uploaded: false
    }

    files = []

    constructor(props) {
        super(props);
        fetch('http://localhost:8080/adapt/files', {
            method: 'GET'
        }).then(async result => {
            this.files = await result.json();
        })
    }

    handleFileUpload = async (event) => {
        const file = event.target.files[0];
        if (file) {
            const formData = new FormData();
            formData.append('file', file);

            try {

                const response = await fetch('http://localhost:8080/upload/zip/adapt', {
                    method: 'POST', body: formData
                })
                this.updateStateAt('uploaded', true)
                console.log('File uploaded successfully:', response.data);
            } catch (error) {
                console.error('Error uploading file:', error);
            }
        }
    };


    updateState = (event, param) => {
        let path = event.target.name;
        let value = event.target.value;
        this.updateStateAt(path, value);
    }

    updateStateAt = (path, value) => {
        console.log("------", path, value)
        this.setState((prevState) => {
            const newState = {...prevState};
            const keys = path.split('.');

            keys.reduce((acc, key, index) => {
                if (index === keys.length - 1) {
                    acc[key] = value;
                } else {
                    acc[key] = {...acc[key]};
                }
                return acc[key];
            }, newState);

            return newState;
        });
    }

    finish = async () => {
        this.setState({activeStep: this.state.activeStep + 1})
        this.updateStateAt('loading', true)
        let adapter = Object.assign({}, this.state.adapter);
        let solutionSetClazz = adapter.solutionSetClazz.split("src");
        adapter.solutionSetClazz = "src" + solutionSetClazz[1];
        adapter.solutionSetProject = solutionSetClazz[0];
        let solutionClazz = adapter.solutionClazz.split("src");
        adapter.solutionClazz = "src" + solutionClazz[1];
        adapter.solutionProject = solutionClazz[0];
        let elementClazz = adapter.elementClazz.split("src");
        adapter.elementClazz = "src" + elementClazz[1];
        adapter.elementProject = elementClazz[0];
        let sc = adapter.searchAlgorithmClazz.split("src");
        adapter.searchAlgorithmClazz = "src" + sc[1];
        adapter.searchAlgorithmProject = sc[0];

        await fetch('http://localhost:8080/adapt', {
            method: 'POST',
            headers: {
                'Accept': 'application/zip',
                'Content-Type': 'application/json',
            }, body: JSON.stringify(adapter)
        }).then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            this.updateStateAt('loading', false)
            return response.blob(); // Convert the response to a blob
        })
            .then(blob => {
                // Create a link element
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.style.display = 'none';
                a.href = url;
                a.download = 'nautilus-framework-plugin.zip'; // Specify the filename for the download

                // Append the link to the body
                document.body.appendChild(a);
                a.click(); // Trigger the download by simulating a click

                // Clean up and remove the link
                window.URL.revokeObjectURL(url);
                document.body.removeChild(a);
                this.updateStateAt('loading', false)
            })
            .catch(error => {
                this.updateStateAt('loading', false)
                console.error('There was an error with the fetch operation:', error);
            });
    }

    handleBack = () => {
        this.setState({activeStep: this.state.activeStep - 1})
    }

    handleReset = () => {
        this.setState({activeStep: 0})
    }

    handleStep = (step) => () => {
        this.setState({activeStep: step})
    };

    render() {
        let {activeStep} = this.state;
        return (
            <div className={styles.Adapter} data-testid="Adapter">
                <Box sx={{maxWidth: 1024}}>
                    <Stepper nonLinear activeStep={activeStep} orientation="vertical">
                        {steps.map((step, index) => (
                            <Step key={step.label}>
                                <StepButton color="inherit" onClick={this.handleStep(index)}>
                                    {step.label}
                                </StepButton>
                                {/*<StepLabel*/}
                                {/*    optional={*/}
                                {/*        index === 2 ? (*/}
                                {/*            <Typography variant="caption">Last step</Typography>*/}
                                {/*        ) : null*/}
                                {/*    }*/}
                                {/*>*/}
                                {/*    {step.label}*/}
                                {/*</StepLabel>*/}
                                <StepContent>
                                    <Typography><i>{step.description}</i></Typography>
                                    <Box sx={{mb: 2}} className={styles.StepContent}>
                                        <div className={styles.jsx}>{step.jsx(this)}</div>
                                        <div>
                                            <Button
                                                variant="contained"
                                                disabled={this.state.loading}
                                                onClick={() => index === steps.length - 1 ? this.finish() : this.handleNext(step.onContinue)}
                                                endIcon={!this.state.loading ? <NavigateNextIcon/> : null}
                                                sx={{mt: 1, mr: 1}}
                                            >
                                                {index === steps.length - 1 ? 'Finish' : 'Continue'}
                                                {this.state.loading ?
                                                    <CircularProgress className={styles.CircularProgress}
                                                                      color="warning"/> : null}
                                            </Button>
                                            <Button
                                                disabled={index === 0}
                                                onClick={this.handleBack}
                                                sx={{mt: 1, mr: 1}}
                                            >
                                                Back
                                            </Button>
                                        </div>
                                    </Box>
                                </StepContent>
                            </Step>
                        ))}
                    </Stepper>
                    {activeStep === steps.length && (
                        <Paper square elevation={0} sx={{p: 3}}>
                            <Typography>All steps completed - you&apos;re finished. Wait the generation of the
                                project.</Typography>
                            <Button onClick={this.handleReset} sx={{mt: 1, mr: 1}} disabled={this.state.loading}>
                                Reset
                                {this.state.loading ?
                                    <CircularProgress className={styles.CircularProgress} color="warning"/> : null}
                            </Button>
                        </Paper>
                    )}
                </Box>
            </div>
        );
    }
}

Adapter.propTypes = {};

Adapter.defaultProps = {};

export default Adapter;
