import React from 'react';
import './App.css';
import {UsaReport, CaliforniaReport, StatesReport, CaliforniaCountyReport} from './reports'
import usaData from './data/usa-data'
import usaCaData from './data/usa-california-data'
import usaCaCountyData from './data/usa-california-by-county-data'
import usaByStateData from './data/usa-by-state-data'

function App() {
  return (
    <div className="App">
      <UsaReport data={usaData}/>
      <StatesReport data={usaByStateData}/>
      <CaliforniaReport data={usaCaData}/>
      <CaliforniaCountyReport data={usaCaCountyData}/>
    </div>
  );
}

export default App;
