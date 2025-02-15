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
        label: 'Search Problem',
        description: `Set the type of your solution and which algorithm you want to use to optimize it.`,
        jsx: (ctx) => (
            <div>
                <Grid container columns={16} spacing={2}>
                    <Grid xs={12} sm={8} md={6} sx={{pb: 2}}>
                        <Tooltip title="Use Double for no integer objective functions and binary for composed solutions"
                                 placement={'top'}>
                            <FormControl size="medium" fullWidth>
                                <InputLabel id="obj-type-label">Type</InputLabel>
                                <Select
                                    labelId="obj-type-label"
                                    className={styles.Select}
                                    value={ctx.state.generate.problem.type}
                                    name='generate.problem.type'
                                    onChange={ctx.updateState}
                                    label="Type"
                                    variant="outlined"
                                >
                                    <MenuItem value="BINARY">BINARY</MenuItem>
                                    <MenuItem value="INTEGER">INTEGER</MenuItem>
                                    <MenuItem value="DOUBLE">DOUBLE</MenuItem>
                                </Select>
                            </FormControl>
                        </Tooltip>
                    </Grid>
                    <Grid xs={12} sm={8} md={6} sx={{pb: 2}}>
                        <Tooltip title="Select your algorithm" placement={'top'}>
                            <FormControl size="medium" fullWidth>
                                <InputLabel id="obj-type-label">Search Algorithm</InputLabel>
                                <Select
                                    labelId="obj-type-label"
                                    className={styles.Select}
                                    value={ctx.state.generate.searchAlgorithm.runner}
                                    name='generate.searchAlgorithm.runner'
                                    onChange={ctx.updateState}
                                    label="Type"
                                    variant="outlined"
                                >
                                    <MenuItem value="NSGAII">NSGAII</MenuItem>
                                    <MenuItem value="NSGAIII">NSGAIII</MenuItem>
                                </Select>
                            </FormControl>
                        </Tooltip>
                    </Grid>
                </Grid>
            </div>
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
                        <br/><br/>
                        <Grid container spacing={2} columns={16}>
                            <Grid item xs={12} sm={4} md={4} sx={{pb: 2}}>
                                <TextField
                                    id={`obj-fn-name-${index}`} // ✅ Unique but stable ID
                                    label="Name"
                                    fullWidth
                                    style={{marginLeft: '15px'}}
                                    // InputProps={{
                                    //     endAdornment: (
                                    //         <InputAdornment position="end">
                                    //             <Tooltip title="Put a name without spaces and uppercase at beginning">
                                    //                 <InfoIcon />
                                    //             </Tooltip>
                                    //         </InputAdornment>
                                    //     ),
                                    // }}
                                    onChange={(event) => ctx.changeObjectiveType('name', index, event, obj.name)} // ✅ Calls updated function
                                    variant="outlined"
                                    value={obj.name}
                                />
                            </Grid>

                            <Grid xs={12} sm={4} md={4} sx={{pb: 2, pl: 2}}>
                                <Tooltip title="Use Double for no integer objective functions" placement="top">
                                    <FormControl fullWidth>
                                        <InputLabel id={`obj-type-label-${index}`}>Type</InputLabel>
                                        <Select
                                            fullWidth
                                            labelId={`obj-type-label-${index}`}
                                            className={styles.Select}
                                            value={obj.type}
                                            label="Type"
                                            variant="outlined"
                                            onChange={(event) => ctx.changeObjectiveType('type', index, event)} // ✅ Added onChange
                                        >
                                            <MenuItem value="double">Double</MenuItem>
                                            <MenuItem value="int">Int</MenuItem>
                                        </Select>
                                    </FormControl>
                                </Tooltip>
                            </Grid>

                            <Grid xs={12} sm={4} md={4} sx={{pb: 2, pl: 2}}>
                                <Tooltip title="Select what you will use to increment the objective function value ">
                                    <Autocomplete
                                        fullWidth
                                        id="free-solo-demo"
                                        freeSolo
                                        disablePortal
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
                                <Grid xs={12} sm={4} md={4} sx={{pb: 2, pl: 2}}>
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
                                <Grid xs={12} sm={4} md={4} sx={{pb: 2, pl: 2}}>
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
                                <Grid xs={12} sm={12} md={4} sx={{pb: 2, pl: 2}}>
                                    <Tooltip
                                        placement="top"
                                        title="This is the formula of the objective function. For example, Cost / Importance"
                                    >
                                        <FormControl sx={{m: 1, minWidth: 300}} fullWidth>
                                            <Autocomplete
                                                multiple
                                                freeSolo
                                                id="expression-autocomplete"
                                                options={ctx.expressionItems(obj.calculate.expression)} // ✅ Provides selectable options
                                                value={Array.isArray(obj.calculate.expression) ? obj.calculate.expression : []} // ✅ Ensures correct format
                                                onChange={(event, newValue, reason, details) => ctx.changeExpression(index, newValue, ctx.state, reason, details)} // ✅ Calls updated function
                                                renderTags={(selected, getTagProps) =>
                                                    selected.map((option, index) => {
                                                        return (
                                                            <Chip
                                                                key={`${option}-${index}`} // ✅ Ensures duplicate values can exist
                                                                label={option}
                                                                {...getTagProps({index})} // ✅ Spread props correctly
                                                            />
                                                        );
                                                    })
                                                }
                                                renderInput={(params) => (
                                                    <TextField {...params} label="Expression" variant="outlined"/>
                                                )}
                                            />
                                        </FormControl>


                                    </Tooltip>
                                </Grid>

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
    {
        label: 'Interaction',
        description:
            'Set the interactive parameters of your approach.',
        jsx: (ctx) => (
            <>
                <Grid container columns={16} spacing={2}>
                    <Grid xs={12} sm={4} md={4} sx={{pb: 2}}>
                        <TextField id="outlined-basic" label="Max interactions" variant="outlined"
                                   style={{"padding-right": "5px"}}
                                   name='generate.interaction.max'
                                   InputProps={{
                                       endAdornment: (<InputAdornment position={"end"}><Tooltip
                                           title="Specify the amount of interactions"><InfoIcon/></Tooltip></InputAdornment>)
                                   }}
                                   value={ctx.state.generate.interaction.max} onChange={ctx.updateState}/>
                    </Grid>
                    <Grid xs={12} sm={4} md={4} sx={{pb: 2}}>
                        <TextField id="outlined-basic" label="First interaction" variant="outlined"
                                   style={{"padding-right": "5px"}}
                                   name='generate.interaction.first'
                                   InputProps={{
                                       endAdornment: (<InputAdornment position={"end"}><Tooltip
                                           title="Specify the first interaction"><InfoIcon/></Tooltip></InputAdornment>)
                                   }}
                                   value={ctx.state.generate.interaction.first} onChange={ctx.updateState}/>

                    </Grid>
                    <Grid xs={12} sm={4} md={4} sx={{pb: 2}}>
                        <TextField id="outlined-basic" label="Interval of interaction" variant="outlined"
                                   name='generate.interaction.interval'
                                   InputProps={{
                                       endAdornment: (<InputAdornment position={"end"}><Tooltip
                                           title="Specify the interval of interaction"><InfoIcon/></Tooltip></InputAdornment>)
                                   }}
                                   value={ctx.state.generate.interaction.interval} onChange={ctx.updateState}/>
                    </Grid>
                </Grid>
            </>
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
            "problem": {
                "type": "DOUBLE"
            },
            "searchAlgorithm": {
                "runner": "NSGAII"
            },
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
                        "expression": ['sum', '/', 'Solution'],
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
            },
            "interaction": {
                "max": 3,
                "first": 3,
                "interval": 3
            }
        }
    }

    changeObjectiveType = (field, index, event, previous) => {
        const value = event.target.value;
        previous = previous + "";
        this.setState(prevState => {
            // Criamos um novo array para manter a imutabilidade e evitar recriação desnecessária do componente
            const updatedObjectives = [...prevState.generate.objectives];
            updatedObjectives[index] = {...updatedObjectives[index], [field]: value};
            for (let i = 0; i < updatedObjectives.length; i++) {
                if (updatedObjectives[i].process.incrementWith === previous)
                    updatedObjectives[i].process.incrementWith = value;
                if (updatedObjectives[i]?.calculate?.expression)
                    for (let j = 0; j < updatedObjectives[i].calculate.expression.length; j++) {
                        if (updatedObjectives[i].calculate.expression[j] === previous) {
                            updatedObjectives[i].calculate.expression[j] = value;
                        }
                    }

            }

            console.log("updateObjecte", prevState)


            // Retornamos o novo estado garantindo que a referência do array pai não seja recriada
            return {
                ...prevState,
                generate: {
                    ...prevState.generate,
                    objectives: updatedObjectives,
                    element: {
                        ...prevState.generate.element,
                        objectives: prevState.generate.element.objectives.map(elem => {
                            if (elem === previous) {
                                elem = value
                            }
                            return elem
                        })
                    }
                }
            };
        }, () => {
            // 🔥 Forçar o React a manter o foco no input
            document.getElementById(`obj-fn-name-${index}`).focus();
        });
    };


    expressionItems = (currentExpression) => {
        let vars = this.state.generate.objectives.map((option) => option.name + "")
            .concat(['Solution', 'Element', 'sum']);
        let operators = ['+', '-', '*', '/'];
        if (!currentExpression || currentExpression.length <= 0)
            return ['-'].concat(vars);
        else if (this.isMathOperator(currentExpression[currentExpression.length - 1]))
            return vars;
        else return operators;
    }

    handleRemoveChip = (chipToDelete) => {
    };

    isMathOperator(str) {
        return ['+', '-', '*', '/'].includes(str);
    }

    changeExpression = (index, value, state, reason, details) => {
        let current = state.generate.objectives[index].calculate.expression || [];
        current = current instanceof String ? current.split(',') : current
        if (!Array.isArray(state.generate.objectives[index].calculate.expression)) {
            state.generate.objectives[index].calculate.expression = []; // Ensure it's an array
        }
        if (current[current.length - 1] === details.option) {
            state.generate.objectives[index].calculate.expression = value
            this.setState(state);
            return
        }

        // ✅ Allow duplicates by appending the new value instead of replacing the array
        const newExpressions = [...state.generate.objectives[index].calculate.expression];

        // if (Array.isArray(value)) {
        //     newExpressions.push(value[value.length - 1]); // ✅ Append only the last selected value
        // } else if (typeof value === "string") {
        // }
        newExpressions.push(details.option); // ✅ Append string values if manually typed

        state.generate.objectives[index].calculate.expression = newExpressions;

        this.setState(state);

        // console.log("---------------------", index, value)
        // state.generate.objectives[index].calculate.expression = value instanceof String ? value.split(',') : value
        // this.setState(state)
    };


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
