import React, {Component} from 'react';
import styles from './Generator.module.css';
import {
    Autocomplete,
    Box,
    Button,
    CircularProgress,
    FormControl,
    Grid,
    InputAdornment,
    InputLabel,
    MenuItem,
    OutlinedInput,
    Paper,
    Select,
    Step,
    StepButton,
    StepContent,
    Stepper,
    TextField,
    Tooltip,
    Typography
} from "@mui/material";
import NavigateNextIcon from '@mui/icons-material/NavigateNext';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import InfoIcon from '@mui/icons-material/Info';
import Chip from '@mui/material/Chip';

const steps = [
    {
        label: 'Instructions',
        description: <b>Welcome to the ISEarchAI Generator. Firsly, read the instructions provided below.</b>,
        jsx: (ctx) => (
            <div>
                <ul>
                    <li>This interface allows you to generate a Java Project using ISearchAI.</li>
                    <li>For that, you need to give a name to solution you will optimize.</li>
                    <li>The solution is composed with items. So, you need to name these items.</li>
                    <li>
                        As every search problem, you need to name your objetive functions.
                        <ul>
                            <li>Note that every objective function has a type and the definition of how it is
                                calculated.
                            </li>
                            <li>The formula generated will be at form (invert or not) * (A [*,/,+,-] B ...)</li>
                            <li><b>* In the formula, 'sum' represents the objective function itself *</b></li>
                        </ul>
                    </li>
                    <li>Every field has a tooltip to help you to fill information accordingly.</li>
                </ul>


            </div>
        ),
        onContinue: (ctx) => {
            console.log("onContinueee", ctx)
        }
    },
    {
        label: 'Solution to optimize',
        description: `Set the name of your solution. For example, for Next Release Problem you can name de solution as "Requirement".
        The name must start in uppercase.`,
        jsx: (ctx) => (
            <div>
                {/*{ctx.state.generate.solution.name}*/}
                <TextField id="outlined-basic" label="Solution Name" variant="outlined"
                           name='generate.solution.name'
                           InputProps={{
                               endAdornment: (<InputAdornment position={"end"}><Tooltip
                                   title="Put a name without spaces and uppercase at beginning"><InfoIcon/></Tooltip></InputAdornment>)
                           }}
                           value={ctx.state.generate.solution.name} onChange={ctx.updateState}/>
            </div>
        ),
        onContinue: (ctx) => {
            console.log("onContinueee", ctx)
        }
    },
    {
        label: 'Composition of your solution',
        description:
            'Set the composition of your solution. For example, a Requirement is composed of tasks, so you can name it as "Task".',
        jsx: (ctx) => (
            <>
                {/*{ctx.state.generate.solution.name}*/}
                <TextField id="outlined-basic" label="Element Name" variant="outlined"
                           name='generate.element.name'
                           InputProps={{
                               endAdornment: (<InputAdornment position={"end"}><Tooltip
                                   title="Put a name without spaces and uppercase at beginning"><InfoIcon/></Tooltip></InputAdornment>)
                           }}
                           value={ctx.state.generate.element.name} onChange={ctx.updateState}/>
            </>
        ),
        onContinue: (ctx) => {
            console.log("onContinueee", ctx)
        }
    },
    {
        label: 'Objective functions',
        description: `Set the objective functions needed to optimize`,
        jsx: (ctx) => (
            <div className={styles.ObjectiveFunctionContent}>
                {ctx.state.generate.objectives.map((obj, index) => <div key={obj.name}
                                                                        className={styles.ObjectiveFunctionItem}>
                    <Paper elevation={1} className={styles.Paper}>
                        <div className={styles.Label}>{obj.name}</div>
                        <Grid container spacing={1} columns={16}>
                            <Grid xs={4}>
                                <TextField id="outlined-basic" label="Name"
                                           style={{'marginLeft':'15px'}}
                                           InputProps={{
                                               endAdornment: (<InputAdornment position={"end"}><Tooltip
                                                   title="Put a name without spaces and uppercase at beginning"><InfoIcon/></Tooltip></InputAdornment>)
                                           }}
                                           variant="outlined" value={obj.name}/>
                            </Grid>
                            <Grid xs={2} spacing={1}>
                                <Tooltip title="Use Double for no integer objective functions" placement={'top'}>
                                    <FormControl size="small">
                                        <InputLabel id="obj-type-label">Type</InputLabel>
                                        <Select
                                            labelId="obj-type-label"
                                            className={styles.Select}
                                            value={obj.type}
                                            label="Type"
                                            variant="outlined"
                                        >
                                            <MenuItem value="double">Double</MenuItem>
                                            <MenuItem value="int">Int</MenuItem>
                                        </Select>
                                    </FormControl>
                                </Tooltip>
                            </Grid>
                            <Grid xs={4}>
                                <Tooltip title="Select what you will use to increment the objective function value ">
                                    <Autocomplete
                                        id="free-solo-demo"
                                        freeSolo
                                        value={obj.process.incrementWith}
                                        options={ctx.state.generate.objectives.map((option) => option.name + "").concat(['1', '2', '3'])}
                                        renderInput={(params) => <TextField {...params} label="Increment With"/>}
                                    />
                                </Tooltip>
                            </Grid>
                        </Grid>
                        <div className={styles.Label}>The formula to calculate the objective function value</div>
                        <div>
                            <Grid container columns={16}>
                                <Grid xs={2}>
                                    {/*<TextField id="outlined-basic" label="Invert" variant="outlined"*/}
                                    {/*           value={obj.calculate.invert || 'false'}/>*/}
                                    <Tooltip
                                        title="'True' sets the objective function to maximization and 'False' sets minimization">
                                        <FormControl fullWidth size="small">
                                            <InputLabel id="obj-type-label">Maximize</InputLabel>
                                            <Select
                                                labelId="obj-type-label"
                                                className={styles.Select}
                                                value={obj.maximize || 'true'}
                                                label="Maximize"
                                                variant="outlined"
                                            >
                                                <MenuItem value="true">True</MenuItem>
                                                <MenuItem value="false">False</MenuItem>
                                            </Select>
                                        </FormControl>
                                    </Tooltip>
                                </Grid>
                                <Grid xs={2}>
                                    {/*<TextField id="outlined-basic" label="Invert" variant="outlined"*/}
                                    {/*           value={obj.calculate.invert || 'false'}/>*/}
                                    <Tooltip title="'True' adds an inversion in the formula (-1 in the beginning)">
                                        <FormControl fullWidth size="small">
                                            <InputLabel id="obj-type-label">Invert formula</InputLabel>
                                            <Select
                                                labelId="obj-type-label"
                                                className={styles.Select}
                                                value={obj.calculate.invert || 'false'}
                                                label="Invert formula"
                                                variant="outlined"
                                            >
                                                <MenuItem value="true">True</MenuItem>
                                                <MenuItem value="false">False</MenuItem>
                                            </Select>
                                        </FormControl>
                                    </Tooltip>
                                </Grid>
                                <Grid xs={10}>
                                    <Tooltip
                                        placement={"top"}
                                        title="This is the formula of the objective function. For example, Cost / Importance">
                                        <FormControl sx={{m: 1, minWidth: 400}}>
                                            <InputLabel id="demo-multiple-chip-label">Expression</InputLabel>
                                            <Select
                                                labelId="demo-multiple-chip-label"
                                                className={styles.Select}
                                                id="demo-multiple-chip"
                                                multiple

                                                value={obj.calculate.expression}
                                                onChange={event => ctx.changeExpression(index, event.target.value, ctx.state)}
                                                input={<OutlinedInput id="select-multiple-chip" label="Chip"/>}
                                                renderValue={(selected) => (
                                                    <Box sx={{
                                                        display: 'flex',
                                                        flexWrap: 'wrap',
                                                        gap: 0,
                                                        height: '10px'
                                                    }}>
                                                        {selected.map((value) => (
                                                            <Chip key={value} label={value}/>
                                                        ))}
                                                    </Box>
                                                )}
                                                MenuProps={{
                                                    PaperProps: {
                                                        style: {
                                                            maxHeight: 225,
                                                            width: 250,
                                                        },
                                                    },
                                                }}
                                            >
                                                {ctx.expressionItems(obj.calculate.expression)
                                                    .map((name) => (
                                                        <MenuItem
                                                            key={name}
                                                            value={name}
                                                        >
                                                            {name}
                                                        </MenuItem>
                                                    ))}
                                            </Select>
                                        </FormControl>
                                    </Tooltip>
                                </Grid>
                                {/*<Grid xs={4}>*/}
                                {/*    <Tooltip*/}
                                {/*        title="The first part of the formula. Remember that 'sum' represents the objective function itself.">*/}
                                {/*        <Autocomplete*/}
                                {/*            id="free-solo-demo"*/}
                                {/*            freeSolo*/}
                                {/*            value={obj.calculate.a.value}*/}
                                {/*            options={ctx.state.generate.objectives.map((option) => option.name + "").concat([ctx.state.generate.element.name, ctx.state.generate.solution.name, 'sum'])}*/}
                                {/*            renderInput={(params) => <TextField {...params}*/}
                                {/*                                                label="First part of formula"/>}*/}
                                {/*        />*/}
                                {/*    </Tooltip>*/}
                                {/*</Grid>*/}
                                {/*<Grid xs={2}>*/}
                                {/*    <Tooltip*/}
                                {/*        title="The operator used in the middle of the formula (A [operator] B).">*/}
                                {/*        <FormControl size="small">*/}
                                {/*            <InputLabel id="obj-type-label">Type</InputLabel>*/}
                                {/*            <Select*/}
                                {/*                labelId="obj-type-label"*/}
                                {/*                value={obj.calculate.type}*/}
                                {/*                label="Type"*/}
                                {/*                variant="outlined"*/}
                                {/*            >*/}
                                {/*                <MenuItem value="/">Divide (/)</MenuItem>*/}
                                {/*                <MenuItem value="*">Multiple (*)</MenuItem>*/}
                                {/*                <MenuItem value="*">Sum (+)</MenuItem>*/}
                                {/*                <MenuItem value="*">Reduce (-)</MenuItem>*/}
                                {/*            </Select>*/}
                                {/*        </FormControl>*/}
                                {/*    </Tooltip>*/}
                                {/*</Grid>*/}
                                {/*<Grid xs={4}>*/}
                                {/*    <Tooltip*/}
                                {/*        title="The second part of the formula. Remember that 'sum' represents the objective function itself.">*/}
                                {/*        <Autocomplete*/}
                                {/*            id="free-solo-demo"*/}
                                {/*            freeSolo*/}
                                {/*            value={obj.calculate.b.value}*/}
                                {/*            options={ctx.state.generate.objectives.map((option) => option.name + "").concat([ctx.state.generate.element.name, ctx.state.generate.solution.name])}*/}
                                {/*            renderInput={(params) => <TextField {...params}*/}
                                {/*                                                label="Second part of formula"/>}*/}
                                {/*        />*/}
                                {/*    </Tooltip>*/}
                                {/*</Grid>*/}

                            </Grid>
                        </div>
                        <div className={styles.EndButton}>
                            <Button
                                variant="contained"
                                color={"warning"}
                                startIcon={<DeleteIcon/>}
                                onClick={() => ctx.removeObjectiveFunction(index)}
                                sx={{mt: 1, mr: 1}}
                            >
                                Remove
                            </Button>
                        </div>
                    </Paper>
                </div>)}
                <div className={styles.EndButton}>
                    <Button
                        variant="contained"
                        startIcon={<AddIcon/>}
                        color={"info"}
                        onClick={() => ctx.addObjectiveFunction()}
                        sx={{mt: 1, mr: 1}}
                    >
                        Add Objective function
                    </Button>
                </div>
            </div>
        ),
        onContinue: (ctx) => {
            console.log("onContinueee", ctx)
        }
    },
];

class Generator extends Component {

    state = {
        activeStep: 0,
        loading: false,
        generate: {
            "objectives": [
                {
                    "name": "Cost",
                    "type": "double",
                    "process": {
                        "incrementWith": "Cost"
                    },
                    "calculate": {
                        "type": "/",
                        "expression": ['sum', '/', 'Cost']
                    }
                },
                {
                    "name": "Importance",
                    "type": "double",
                    "process": {
                        "incrementWith": "Importance"
                    },
                    "calculate": {
                        "expression": ['sum', '/', 'Importance'],
                        "type": "/",
                        "invert": "true"
                    }
                },
                {
                    "name": "Profit",
                    "type": "double",
                    "process": {
                        "incrementWith": "Profit"
                    },
                    "calculate": {
                        "expression": ['sum', '/', 'Profit'],
                        "type": "/",
                        "invert": "true",
                    }
                },

                {
                    "name": "Size",
                    "type": "int",
                    "process": {
                        "incrementWith": '1'
                    },
                    "calculate": {
                        "expression": ['sum', '/', 'Requirement'],
                        "type": "/",
                        "invert": "true"
                    }
                }
            ],
            "element": {
                "name": "Task",
                "objectives": ["Cost", "Profit", "Importance"]
            },
            "solution": {
                "name": "Requirement"
            }
        }
    }

    expressionItems = (currentExpression) => {
        let vars = this.state.generate.objectives.map((option) => option.name + "")
            .concat([this.state.generate.element.name, this.state.generate.solution.name, 'sum']);
        let operators = ['+', '-', '*', '/'];
        if (currentExpression.length === 0 || this.isMathOperator(currentExpression[currentExpression.length - 1]))
            return vars;
        else return operators;
    }

    isMathOperator(str) {
        return ['+', '-', '*', '/'].includes(str);
    }

    changeExpression = (index, value, state) => {
        state.generate.objectives[index].calculate.expression = value instanceof String ? value.split(',') : value
        this.setState(state)
        console.log("---------------------", this.state, value)
    }

    updateState = (event) => {
        let path = event.target.name;
        let value = event.target.value;
        this.updateStateAt(path, value);
    }

    updateStateAt = (path, value) => {
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

    addObjectiveFunction = () => {
        this.state.generate.objectives.push({
            "name": "NewObjectiveFunction",
            "type": "int",
            "process": {
                "incrementWith": 1
            },
            "calculate": {
                "type": "/",
                "invert": "true",
                "a": {
                    "value": "sum"
                },
                "b": {
                    "value": "Solution"
                }
            }
        })
        this.setState(this.state)
    }

    removeObjectiveFunction = (index) => {
        this.state.generate.objectives.splice(index, 1)
        this.setState(this.state)
    }

    handleNext = (callback) => {
        this.setState({activeStep: this.state.activeStep + 1})
        callback(this)
    }

    finish = async () => {
        this.setState({activeStep: this.state.activeStep + 1})
        this.updateStateAt('loading', true)
        await fetch('http://localhost:8080/generate', {
            method: 'POST',
            headers: {
                'Accept': 'application/zip',
                'Content-Type': 'application/json',
            }, body: JSON.stringify(this.state.generate)
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
            <div className={styles.Generator} data-testid="Generator">
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


//
// </div>
// <Box
//     component="form"
//     sx={{
//         '& > :not(style)': { m: 1, width: '25ch' },
//     }}
//     noValidate
//     autoComplete="off"
// >
//     <TextField id="outlined-basic" label="Outlined" variant="outlined" />
//     <TextField id="filled-basic" label="Filled" variant="filled" />
//     <TextField id="standard-basic" label="Standard" variant="standard" />
// </Box>

Generator.propTypes = {};

Generator.defaultProps = {};

export default Generator;
