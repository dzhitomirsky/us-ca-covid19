import React from 'react';
import {CovidLineChartReport} from "./components";
import {CovidLineChartData} from '../prop-types'

function CaliforniaReport({data}) {
    return <CovidLineChartReport data={data} title={"California cases by date"}/>
}

CaliforniaReport.propTypes = {
    data: CovidLineChartData
};

CaliforniaReport.defaultProps = {
    data: []
};

export default CaliforniaReport
