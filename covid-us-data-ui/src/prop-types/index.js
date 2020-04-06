import PropTypes from 'prop-types';

const CovidLineCharReportRow = PropTypes.shape({
    date: PropTypes.string.isRequired,
    active: PropTypes.number.isRequired,
    recovered: PropTypes.number.isRequired,
    deaths: PropTypes.number.isRequired,
});

const CovidLineChartData = PropTypes.arrayOf(CovidLineCharReportRow)

const CovidBarChartReportRow = PropTypes.shape({
  state: PropTypes.string.isRequired,
  active: PropTypes.number.isRequired,
  deaths: PropTypes.number.isRequired,
  recovered: PropTypes.number.isRequired,
});


const CaliforniaProvinceBarChartRow = PropTypes.shape({
    province: PropTypes.string.isRequired,
    active: PropTypes.number.isRequired,
    deaths: PropTypes.number.isRequired,
    recovered: PropTypes.number.isRequired,
});

const CaliforniaProvinceBarCharData = PropTypes.arrayOf(CaliforniaProvinceBarChartRow);

const CovidBarChartReportData = PropTypes.arrayOf(CovidBarChartReportRow);

export {
    CovidLineChartData,
    CovidLineCharReportRow,
    CovidBarChartReportData,
    CovidBarChartReportRow,
    CaliforniaProvinceBarCharData
};
