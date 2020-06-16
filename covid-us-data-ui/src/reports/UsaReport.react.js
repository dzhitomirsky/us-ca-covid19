import React from 'react';
import {CovidLineChartData} from "../prop-types";
import {CovidLineChartReport} from "./components";

function UsaReport({data}) {
    return <CovidLineChartReport data={data} title={"USA total cases by date"}/>
}

UsaReport.propTypes = {
    data: CovidLineChartData
};

UsaReport.defaultProps = {
    data: []
};

export default UsaReport;
