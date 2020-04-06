import React from 'react';
import {CovidBarChartReport} from "./components";
import {CaliforniaProvinceBarCharData} from "../prop-types";

function CaliforniaCountyReport({data}) {
    return <CovidBarChartReport data={data} xDataKey="province" title={"Last update statistics by California county"}/>
}

CaliforniaCountyReport.propTypes = {
    data: CaliforniaProvinceBarCharData
};

CaliforniaCountyReport.defaultProps = {
    data: []
};

export default CaliforniaCountyReport
