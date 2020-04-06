import React from 'react';
import {CovidBarChartReportData} from '../prop-types'
import {CovidBarChartReport} from "./components";

function StatesReport({data}) {
    return <CovidBarChartReport data={data} xDataKey="state" title={"Last update statistics by state"}/>
}

StatesReport.propTypes = {
    data: CovidBarChartReportData
};

StatesReport.defaultProps = {
    data: []
};

export default StatesReport
