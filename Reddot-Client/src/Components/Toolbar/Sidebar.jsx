import "react";
import PropTypes from "prop-types";
import {
  Divider,
  Drawer,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
} from "@mui/material";
import { Link } from "react-router-dom";
import {
  Home,
  People,
  QuestionAnswer,
  Settings,
  Tag,
} from "@mui/icons-material";

export default function Sidebar({ open, toggleSidebar }) {
  return (
    <div>
      {/* Sidebar under the Navbar */}
      <Drawer
        variant="persistent"
        anchor="left"
        open={open}
        onClose={toggleSidebar}
        sx={{
          width: 240,
          flexShrink: 0,
          position: "absolute",
          top: "64px", // Adjust this value if navbar height changes
          "& .MuiDrawer-paper": {
            width: 240,
            backgroundColor: "#f8f9f9",
            color: "#000000",
            borderRight: "1px solid #ddd",
            top: "64px", // Align sidebar just below the navbar
            height: "calc(100vh - 64px)", // Full height minus navbar height
          },
        }}
      >
        <List>
          {/* Home */}
          <ListItem button component={Link} to="/">
            <ListItemIcon>
              <Home />
            </ListItemIcon>
            <ListItemText primary="Home" />
          </ListItem>

          {/* Questions */}
          <ListItem button component={Link} to="/questions">
            <ListItemIcon>
              <QuestionAnswer />
            </ListItemIcon>
            <ListItemText primary="Questions" />
          </ListItem>

          {/* Tags */}
          <ListItem button component={Link} to="/tags">
            <ListItemIcon>
              <Tag />
            </ListItemIcon>
            <ListItemText primary="Tags" />
          </ListItem>

          {/* Users */}
          <ListItem button component={Link} to="/users">
            <ListItemIcon>
              <People />
            </ListItemIcon>
            <ListItemText primary="Users" />
          </ListItem>

          {/* Settings */}
          <ListItem button component={Link} to="/settings">
            <ListItemIcon>
              <Settings />
            </ListItemIcon>
            <ListItemText primary="Settings" />
          </ListItem>
        </List>
        <Divider />
      </Drawer>
    </div>
  );
}

Sidebar.propTypes = {
  open: PropTypes.bool.isRequired,
  toggleSidebar: PropTypes.func.isRequired,
};
