import React, { useState } from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import { ThemeProvider } from "@emotion/react";
import { CssBaseline } from "@mui/material";
import Routers from './Routers/Routers';
import Navbar from './Components/Toolbar/Navbar';
import Sidebar from './Components/Toolbar/Sidebar';
import { Theme } from './Themes/Theme';

function App() {
  const [sidebarOpen, setSidebarOpen] = useState(true);

  const toggleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
  };

  return (
    <Router>
      <ThemeProvider theme={Theme}>
        <CssBaseline />
        <Navbar toggleSidebar={toggleSidebar} />
        <Sidebar open={sidebarOpen} toggleSidebar={toggleSidebar} />
        <div style={{ marginLeft: sidebarOpen ? 240 : 0, padding: '20px', transition: 'margin-left 0.3s ease' }}>
          <Routers />
        </div>
      </ThemeProvider>
    </Router>
  );
}

export default App;
