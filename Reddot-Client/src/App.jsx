import { useState } from "react";
import "./App.css";
import { BrowserRouter as Router } from "react-router-dom";
import { Theme } from "./Themes/Theme";
import Navbar from "./Components/Toolbar/Navbar";
import Sidebar from "./Components/Toolbar/Sidebar";
import Routers from "./Routers/Routers";
import { CssBaseline, ThemeProvider } from "@mui/material";

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
        <div
          style={{
            marginLeft: sidebarOpen ? 240 : 0,
            padding: "20px",
            transition: "margin-left 0.3s ease",
          }}
        >
          <Routers />
        </div>
      </ThemeProvider>
    </Router>
  );
}

export default App;
