import React from "react";
import {
  AppBar,
  Toolbar,
  Button,
  TextField,
  Box,
  IconButton,
} from "@mui/material";
import { Link } from "react-router-dom";
import SearchIcon from "@mui/icons-material/Search";
import MenuIcon from "@mui/icons-material/Menu";

export default function Navbar({ toggleSidebar }) {
  return (
    <AppBar
      position="static"
      style={{ backgroundColor: "#f8f9f9", color: "black" }}
    >
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
            src={require("../../Images/Logo.png")}
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
            style={{ textTransform: "none", fontSize: "16px",marginLeft: "-150px", }}
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
          InputProps={{
            style: { color: "black" },
            startAdornment: (
              <SearchIcon style={{ marginRight: "10px", color: "gray" }} />
            ),
          }}
        />

        <Box style={{ display: "flex", gap: "15px" }}>
          <Button
            color="inherit"
            component={Link}
            to="/users/login"
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
