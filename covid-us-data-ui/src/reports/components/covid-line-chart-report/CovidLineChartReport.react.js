import React from 'react';
import PropTypes from 'prop-types'
import {CartesianGrid, Label, Legend, Line, LineChart, Tooltip, XAxis, YAxis} from 'recharts';
import ReportContainer from "../report-container";

function CovidLineChartReport({data, title}) {
    return (
        <ReportContainer title={title}>
            <LineChart width={window.innerWidth} height={800} data={data} margin={{top: 20, right: 20, bottom: 50, left: 50}}>
                <Line type="monotone" dataKey="active" stroke="#f00000"/>
                <Line type="monotone" dataKey="deaths" stroke="#131414"/>
                <CartesianGrid stroke="#ccc" strokeDasharray="5 5"/>
                <XAxis dataKey="date" interval={0} angle={-45} textAnchor="end"/>
                <YAxis>
                    <Label
                        value="Active / Deaths"
                        angle={-90}
                        offset={-15}
                        position="insideLeft"/>
                </YAxis>
                <Tooltip/>
                <Legend verticalAlign="top" height={36}/>
            </LineChart>
        </ReportContainer>
    );
}


CovidLineChartReport.propTypes = {
    data: PropTypes.arrayOf(Object),
    title: PropTypes.string.isRequired
};

CovidLineChartReport.defaultProps = {
    data: []
};

export default CovidLineChartReport;
