import React from 'react';
import './App.css';
import {CaliforniaCountyReport, CaliforniaReport, SfBayAreaReport, StatesReport, UsaReport} from './reports'
import usaData from './data/usa-data'
import usaCaData from './data/usa-california-data'
import usaCaCountyData from './data/usa-california-by-county-data'
import usaCaCountiesData from './data/usa-california-counties-data.json'
import usaByStateData from './data/usa-by-state-data'
import usaCaSfBayData from './data/california-sf-bay-data.json'
import {makeStyles} from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import {CovidLineChartReport} from "./reports/components";

const useStyles = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
    },
    paper: {
        padding: theme.spacing(2),
        textAlign: 'center',
        color: theme.palette.text.secondary,
    },
}));


function App() {
    const classes = useStyles();

    return (
        <div className={classes.root}>
            <Grid container spacing={3}>
                <Grid item xs={6}>
                    <UsaReport data={usaData}/>
                </Grid>
                <Grid item xs={6}>
                    <StatesReport data={usaByStateData}/>
                </Grid>
            </Grid>
            <Grid container spacing={3}>
                <Grid item xs={6}>
                    <CaliforniaReport data={usaCaData}/>
                </Grid>
                <Grid item xs={6}>
                    <CaliforniaCountyReport data={usaCaCountyData}/>
                </Grid>
            </Grid>
            <Grid container spacing={3}>
                {
                    Object
                        .keys(usaCaCountiesData)
                        .map(county => (
                            <Grid item xs={6}>
                                <CovidLineChartReport data={usaCaCountiesData[county]} title={county}/>
                            </Grid>
                        ))
                }
            </Grid>
            <Grid container spacing={3}>
                <Grid item xs={6}>
                    <SfBayAreaReport data={usaCaSfBayData}/>
                </Grid>
            </Grid>
        </div>
    );
}

export default App;
