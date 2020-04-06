import React from 'react';
import PropTypes from 'prop-types';

function ReportContainer({title, children}) {
    return (
        <div className="report-container">
            <h3>{title}</h3>
            {children}
        </div>
    );
}

ReportContainer.propTypes = {
    title: PropTypes.string.isRequired,
    children: PropTypes.oneOfType([
        PropTypes.element,
        PropTypes.arrayOf(PropTypes.element),
    ]).isRequired
};

export default ReportContainer;
