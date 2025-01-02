const {createTheme} = require("@mui/material");

export const Theme = createTheme({
    palette: {
        mode: "light",
        primary: {
            main: "#f42424",
        },
        secondary: {
            main: "#0077cc",
        },
        error: {
            main: "#d32f2f",
        },
        warning: {
            main: "#fbc02d",
        },
        info: {
            main: "#0288d1",
        },
        success: {
            main: "#388e3c",
        },
        background: {
            default: "#f8f9f9",
            paper: "#ffffff",
        },
        text: {
            primary: "#000000",
            secondary: "#555555",
        },
        grey: {
            main: "#bdbdbd",
        },
    },
    typography: {
        fontFamily: "'Segoe UI', 'Roboto', 'Arial', sans-serif",
        h1: {
            fontSize: "2.5rem",
            fontWeight: 700,
        },
        h2: {
            fontSize: "2rem",
            fontWeight: 600,
        },
        h3: {
            fontSize: "1.75rem",
            fontWeight: 500,
        },
        h4: {
            fontSize: "1.5rem",
            fontWeight: 400,
        },
        h5: {
            fontSize: "1.25rem",
            fontWeight: 300,
        },
        h6: {
            fontSize: "1rem",
            fontWeight: 200,
        },
        body1: {
            fontSize: "1rem",
            fontWeight: 400,
        },
        body2: {
            fontSize: "0.875rem",
            fontWeight: 400,
        },
        button: {
            fontWeight: 600,
        },
    },
});
