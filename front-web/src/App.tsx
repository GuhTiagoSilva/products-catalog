import React from 'react';
import './core/assets/styles/custom.scss'
import './app.scss'
import Navbar from './core/components/Navbar';
import Routes from './Routes';
const App = () => {
  return (
      <Routes/> // Putting the routes here in the app, we are saying that my application is basead in routes
    );
}

export default App;