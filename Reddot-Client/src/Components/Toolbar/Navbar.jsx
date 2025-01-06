import "react";
import logo from "../../Images/Logo.png";
import {
  AppBar,
  Box,
  Button,
  IconButton,
  TextField,
  Toolbar,
} from "@mui/material";
import PropTypes from "prop-types";
import { Link } from "react-router-dom";
import MenuIcon from "@mui/icons-material/Menu";
import SearchIcon from "@mui/icons-material/Search";

export default function Navbar({ toggleSidebar }) {
  return (
    <AppBar style={{ backgroundColor: "#f8f9f9", color: "black" }}>
      {/* Stack Overflow background color */}
      <Toolbar
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
        }}
      >
        {/* Sidebar Toggle Button (Hamburger menu) */}
        <IconButton color="inherit" onClick={toggleSidebar}>
          <MenuIcon />
        </IconButton>

        {/* Logo */}
        <Link to="/" style={{ textDecoration: "none" }}>
          <img
            src={logo}
            alt="Logo"
            style={{
              height: "40px",
              transition: "transform 0.3s",
              marginLeft: "-200px", // Adjust this value to move logo to the left
            }}
            onMouseEnter={(e) => (e.target.style.transform = "scale(1.1)")}
            onMouseLeave={(e) => (e.target.style.transform = "scale(1)")}
          />
        </Link>

        {/* Navigation Buttons */}
        <Box style={{ display: "flex", gap: "15px" }}>
          <Button
            color="inherit"
            component={Link}
            to="/About"
            style={{
              textTransform: "none",
              fontSize: "16px",
              marginLeft: "-150px",
            }}
          >
            About
          </Button>
          <Button
            color="inherit"
            component={Link}
            to="/questions"
            style={{ textTransform: "none", fontSize: "16px" }}
          >
            Questions
          </Button>
        </Box>

        {/* Search Bar */}
        <TextField
          variant="outlined"
          placeholder="Search..."
          size="small"
          style={{
            width: "400px",
            backgroundColor: "#fff",
            borderRadius: "4px",
          }}
          slotProps={{
            input: {
              style: { color: "black" },
              startAdornment: (
                <SearchIcon style={{ marginRight: "10px", color: "gray" }} />
              ),
            },
          }}
        />

        <Box style={{ display: "flex", gap: "15px" }}>
          <Button
            color="inherit"
            component={Link}
            to="/About"
            style={{ textTransform: "none", fontSize: "16px" }}
          >
            Login
          </Button>
          <Button
            color="inherit"
            component={Link}
            to="/questions"
            style={{ textTransform: "none", fontSize: "16px" }}
          >
            Sign Up
          </Button>
        </Box>
      </Toolbar>
    </AppBar>
  );
}

Navbar.propTypes = {
  toggleSidebar: PropTypes.func.isRequired,
};
