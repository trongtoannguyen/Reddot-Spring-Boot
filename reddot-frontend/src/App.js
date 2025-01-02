import React, { useState } from "react";
import { BrowserRouter as useLocation } from "react-router-dom";
import { ThemeProvider } from "@emotion/react";
import { CssBaseline } from "@mui/material";
import Routers from "./Routers/Routers";
import Navbar from "./Components/Toolbar/Navbar";
import Sidebar from "./Components/Toolbar/Sidebar";
import { Theme } from "./Themes/Theme";

function App() {
    const [sidebarOpen, setSidebarOpen] = useState(true);

    const toggleSidebar = () => {
        setSidebarOpen(!sidebarOpen);
    };

  const location = useLocation();
  const excludeRoutes = ["/users/login", "/users/register"];
  const isSidebarExcluded = excludeRoutes.includes(location.pathname);

  return (
      <ThemeProvider theme={Theme}>
        <CssBaseline />
        <Navbar toggleSidebar={toggleSidebar} />
        {!isSidebarExcluded && (
          <Sidebar open={sidebarOpen} toggleSidebar={toggleSidebar} />
        )}
        <div
          style={{
            marginLeft: !isSidebarExcluded && sidebarOpen ? 240 : 0,
            padding: "20px",
            transition: "margin-left 0.3s ease",
          }}
        >
          <Routers />
        </div>
      </ThemeProvider>
  );
}

export default App;
