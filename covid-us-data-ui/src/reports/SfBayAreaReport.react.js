import React from 'react';
import {CovidLineChartReport} from "./components";
import {CovidLineChartData} from '../prop-types'

function SfBayAreaReport({data}) {
    return <CovidLineChartReport data={data} title={"Sf Bay Area by date"}/>
}

SfBayAreaReport.propTypes = {
    data: CovidLineChartData
};

SfBayAreaReport.defaultProps = {
    data: []
};

export default SfBayAreaReport
