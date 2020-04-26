import React from 'react'
import PropTypes from 'prop-types';
import {Bar, BarChart, Legend, Tooltip, XAxis, YAxis,} from 'recharts';
import ReportContainer from "../report-container";

function CovidBarChartReport({data, xDataKey, title}) {
    return (
        <ReportContainer title={title}>
            <BarChart
                width={window.innerWidth / 2}
                height={800}
                data={data}
                margin={{top: 20, right: 20, bottom: 50, left: 50}}
            >
                <XAxis dataKey={xDataKey} angle={-45} textAnchor="end" /*interval={0}*//>
                <YAxis/>
                <Tooltip/>
                <Legend verticalAlign="middle" align="center"/>
                <Bar dataKey="active" fill="#f00000"/>
                <Bar dataKey="deaths" fill="#131414"/>
            </BarChart>
        </ReportContainer>
    );
}

CovidBarChartReport.propTypes = {
    data: PropTypes.arrayOf(Object),
    title: PropTypes.string.isRequired,
    xDataKey: PropTypes.string.isRequired,
};

CovidBarChartReport.defaultProps = {
    data: []
};


export default CovidBarChartReport;
